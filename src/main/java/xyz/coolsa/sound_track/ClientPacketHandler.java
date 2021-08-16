package xyz.coolsa.sound_track;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.enums.Instrument;

public class ClientPacketHandler {
	public Map<Entity, SoundInstance> playingSongs;

	public ClientPacketHandler() {
		playingSongs = new HashMap<Entity, SoundInstance>();
		ClientPlayNetworking.registerGlobalReceiver(SoundTrackConstants.JUKEBOX_MINECART_PLAY,
				(client, handler, buf, responseSender) -> {
					this.jukeboxMinecartPlayUpdate(client, handler, buf, responseSender);
				});
		ClientPlayNetworking.registerGlobalReceiver(SoundTrackConstants.NOTEBLOCK_MINECART_PLAY,
				(client, handler, buf, responseSender) -> {
					this.noteblockMinecartPlayUpdate(client, handler, buf, responseSender);
				});
	}

	/**
	 * Play a music disc in a Jukebox Minecart. Stops it if its already playing.
	 * 
	 * @param client
	 * @param handler
	 * @param buf
	 * @param responseSender
	 */
	private void jukeboxMinecartPlayUpdate(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
			PacketSender responseSender) {
		int entityId = buf.readInt(); // read the entity id
		ItemStack record = buf.readItemStack(); // read the item stack from the packet.
		@SuppressWarnings("unused")
		long playSeeking = buf.readLong(); // TODO: Add seeking ability, so jukebox minecarts can start playback at any
											// time.
		client.execute(() -> {
			// verify the entity id we were given is a jukebox minecart entity.
			if (!(client.world.getEntityById(entityId) instanceof JukeboxMinecartEntity)) {
				return; // otherwise exit.
			}
			// if it is an jukeboxminecart entity, lets go ahead and get it.
			JukeboxMinecartEntity entity = (JukeboxMinecartEntity) client.world.getEntityById(entityId);
			this.clientJukeboxMinecartPlay(client, entity, record);
		});
	}

	@SuppressWarnings({ "unlikely-arg-type", "resource" })
	private void clientJukeboxMinecartPlay(MinecraftClient client, JukeboxMinecartEntity entity, ItemStack record) {
		// get the currently playing songs on the client.
		SoundInstance instance = this.playingSongs.get(entity);
		// as well as the client's sound system.
		SoundManager soundSystem = MinecraftClient.getInstance().getSoundManager();
		if (soundSystem == null)
			return; // return if there is no sound system
		Text subtitle = null; // otherwise, lets go and try to play the sound and
		// if there is currently a sound playing
		if (instance != null && soundSystem.isPlaying(instance)) {
			soundSystem.stop(instance); // lets stop that sound
			this.playingSongs.remove(instance); // remove it from this class's currently playing songs
			instance = null; // and make sure its seen as null.
			// otherwise
		} else if (record.getItem() instanceof MusicDiscItem) {
			// if the record is a vanilla (or extension of) music disc,
			MusicDiscItem recordItem = (MusicDiscItem) record.getItem(); // get the record item
			instance = new EntityTrackingSoundInstance(recordItem.getSound(), SoundCategory.RECORDS, 4.0f, 1.0f,
					entity); // create a new sound instance tracking the jukebox minecart
			subtitle = recordItem.getDescription(); // and read the description of the music disc.
			// otherwise
		} else if (SoundTrack.phonos.isLoaded() && SoundTrack.phonos.isCustomMusicDisc(record.getItem())) {
			// if the record is a CustomMusicDiscItem from Phonos, read the sound event.
			SoundEvent event = Registry.SOUND_EVENT
					.get(Identifier.tryParse(record.getOrCreateSubNbt("MusicData").getString("SoundId")));
			if (event != null) // if the event exists, create an instance.
				instance = new EntityTrackingSoundInstance(event, SoundCategory.RECORDS, 4.0f, 1.0f, entity);
			try { // also lets try to read the subtitle from the item
				subtitle = instance.getSoundSet(MinecraftClient.getInstance().getSoundManager()).getSubtitle();
				if(subtitle == null) throw new Exception(); // fail if subtitle is null
			} catch (Exception e) { // if it fails, lets just make it a "custom music disc"
				subtitle = new TranslatableText("item.phonos.custom_music_disc");
			}
		}
		if (subtitle != null) // if there exists a subtitle, lets display the "now playing" subtitle.
			MinecraftClient.getInstance().inGameHud
					.setOverlayMessage(new TranslatableText("record.nowPlaying", subtitle), true);
		// lets put the sound into the currently playing map,
		this.playingSongs.put(entity, instance);
		// and if that sound is not null, lets attempt to play it via the sound system.
		if (instance != null)
			soundSystem.play(instance);
		// no seaking of audio needed, because OOF is it hard for ogg files in java.
	}

	/**
	 * Plays a noteblock from a Noteblock Minecart.
	 * 
	 * @param client
	 * @param handler
	 * @param buf
	 * @param responseSender
	 */
	private void noteblockMinecartPlayUpdate(MinecraftClient client, ClientPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		int entityId = buf.readInt(); // read the entity id
		BlockPos pos = buf.readBlockPos(); // read the position of the block below the entity.
		Integer note = buf.readInt(); // and read the tuning of the noteblock minecart.
		client.execute(() -> {
			if (!(client.world.getEntityById(entityId) instanceof NoteblockMinecartEntity)) {
				return; // if the entity is not a minecart entity, lets skip.
			}
			NoteblockMinecartEntity entity = (NoteblockMinecartEntity) client.world.getEntityById(entityId); // otherwise,
			Instrument.fromBlockState(entity.world.getBlockState(pos)); // read the instrument from the target block
			SoundManager soundSystem = MinecraftClient.getInstance().getSoundManager(); // get the sound manager
			EntityTrackingSoundInstance noteInstance = new EntityTrackingSoundInstance( // track the entity, play the
																						// instrument.
					Instrument.fromBlockState(entity.world.getBlockState(pos)).getSound(), SoundCategory.RECORDS, 3.0f,
					(float) Math.pow(2.0, (double) (note - 12) / 12.0), entity);
			// music is wierd, note frequencies increment in an exponential way, but there
			// are 12 notes, so each increase is really the above.
			soundSystem.play(noteInstance); // now lets play the note.
			entity.world.addParticle(ParticleTypes.NOTE, (double) entity.getX(), (double) entity.getY() + 1.2,
					(double) entity.getZ(), (double) note / 24.0, 0.0, 0.0); // and also particles!
		});
	}

}
