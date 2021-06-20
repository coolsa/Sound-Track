package xyz.coolsa.jukebox;

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
import net.minecraft.sound.SoundCategory;
import xyz.coolsa.jukebox.mixin.SoundManagerAccessor;
import xyz.coolsa.jukebox.mixin.SoundSystemAccessor;

public class ClientPacketHandler {
	public Map<Entity, SoundInstance> playingSongs;

	public ClientPacketHandler() {
		playingSongs = new HashMap<Entity, SoundInstance>();
		ClientPlayNetworking.registerGlobalReceiver(JukeboxConstants.JUKEBOX_MINECART_PLAY,
				(client, handler, buf, responseSender) -> {
					this.jukeboxMinecartPlayUpdate(client, handler, buf, responseSender);
				});
	}

	/**
	 * Play a music disc in a Jukebox Minecart.
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
		System.out.println(entityId);
		client.execute(() -> {
			boolean isJukeboxMinecart = client.world.getEntityById(entityId) instanceof JukeboxMinecartEntity;
			if (!isJukeboxMinecart) {
				return;
			}
			JukeboxMinecartEntity entity = (JukeboxMinecartEntity) client.world.getEntityById(entityId);
			SoundInstance instance = this.playingSongs.get(entity);
//			System.out.println((SoundManagerAccessor)MinecraftClient.getInstance().getSoundManager());
			SoundManager soundSystem = MinecraftClient
					.getInstance().getSoundManager();
			if(instance != null) {
				soundSystem.stop(instance);
				this.playingSongs.remove(instance);
			}
			if (record.getItem() instanceof MusicDiscItem) {
				MusicDiscItem recordItem = (MusicDiscItem) record.getItem();
				EntityTrackingSoundInstance newInstance = new EntityTrackingSoundInstance(recordItem.getSound(), SoundCategory.RECORDS,
						1.0f, 1.0f, entity);

				System.out.println(newInstance);
				soundSystem.play(newInstance);
				this.playingSongs.put(entity, newInstance);
			}
			// do stuff here. I will likely need to use the code I made for OnDeck,
			// regarding the custom sound events.
		});
	}

}
