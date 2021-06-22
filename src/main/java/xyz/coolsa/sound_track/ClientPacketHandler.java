package xyz.coolsa.sound_track;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
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
		int entityId = buf.readInt();
		ItemStack record = buf.readItemStack();
		long playSeeking = buf.readLong();
		client.execute(() -> {
			boolean isJukeboxMinecart = client.world.getEntityById(entityId) instanceof JukeboxMinecartEntity;
			if (!isJukeboxMinecart) {
				return;
			}
			JukeboxMinecartEntity entity = (JukeboxMinecartEntity) client.world.getEntityById(entityId);
			SoundInstance instance = this.playingSongs.get(entity);
//			System.out.println((SoundManagerAccessor)MinecraftClient.getInstance().getSoundManager());
			SoundManager soundSystem = MinecraftClient.getInstance().getSoundManager();
			if (instance != null && soundSystem.isPlaying(instance)) {
				soundSystem.stop(instance);
				this.playingSongs.remove(instance);
			} else if (record.getItem() instanceof MusicDiscItem) {
				MusicDiscItem recordItem = (MusicDiscItem) record.getItem();
				instance = new EntityTrackingSoundInstance(recordItem.getSound(), SoundCategory.RECORDS, 4.0f, 1.0f,
						entity);

//				System.out.println(newInstance);
				this.playingSongs.put(entity, instance);
				soundSystem.play(instance);
			}
			// do stuff here. I will likely need to use the code I made for OnDeck,
			// regarding the custom sound events, to seek the audio. or lots of mixins.
		});
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
		int entityId = buf.readInt();
		BlockPos pos = buf.readBlockPos();
		Integer note = buf.readInt();
		client.execute(() -> {
			if (!(client.world.getEntityById(entityId) instanceof NoteblockMinecartEntity)) {
				return;
			}
			NoteblockMinecartEntity entity = (NoteblockMinecartEntity) client.world.getEntityById(entityId);
			Instrument.fromBlockState(entity.world.getBlockState(pos));
//			System.out.println((SoundManagerAccessor)MinecraftClient.getInstance().getSoundManager());
			SoundManager soundSystem = MinecraftClient.getInstance().getSoundManager();
			EntityTrackingSoundInstance noteInstance = new EntityTrackingSoundInstance(
					Instrument.fromBlockState(entity.world.getBlockState(pos)).getSound(), SoundCategory.RECORDS, 3.0f,
					(float) Math.pow(2.0, (double) (note - 12) / 12.0), entity);

//			System.out.println(newInstance);
			soundSystem.play(noteInstance);
			entity.world.addParticle(ParticleTypes.NOTE, (double) entity.getX(), (double) entity.getY() + 1.2,
					(double) entity.getZ(), (double) note / 24.0, 0.0, 0.0);
			// do stuff here. I will likely need to use the code I made for OnDeck,
			// regarding the custom sound events.
		});
	}

}
