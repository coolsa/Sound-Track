package xyz.coolsa.sound_track.client;

import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import xyz.coolsa.sound_track.SoundTrack;
import xyz.coolsa.sound_track.SoundTrackConstants;
import xyz.coolsa.sound_track.client.networking.ClientPacketHandler;
//import xyz.coolsa.sound_track.compat.phonos.client.PhonosCompatClient;
import xyz.coolsa.sound_track.entity.JukeboxMinecartEntity;
import xyz.coolsa.sound_track.entity.NoteBlockMinecartEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SoundTrackClient implements ClientModInitializer {
	@SuppressWarnings("unused")
	private static ClientPacketHandler handler;
	@SuppressWarnings("unused")
//    private static PhonosCompatClient phonosClient;
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(SoundTrackConstants.JUKEBOX_MINECART_ENTITY,
				(context) -> new MinecartEntityRenderer<JukeboxMinecartEntity>(context,
						EntityModelLayers.FURNACE_MINECART));
		EntityRendererRegistry.register(SoundTrackConstants.NOTE_BLOCK_MINECART_ENTITY,
				(context) -> new MinecartEntityRenderer<NoteBlockMinecartEntity>(context,
						EntityModelLayers.FURNACE_MINECART));
		handler = new ClientPacketHandler();
//		if (SoundTrack.phonos.supportsEntities()) {
//			SoundTrackClient.phonosClient = new PhonosCompatClient();
//		}
	}
}
