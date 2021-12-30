package xyz.coolsa.sound_track.compat.phonos.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import xyz.coolsa.sound_track.SoundTrack;
import xyz.coolsa.sound_track.compat.phonos.PhonosSpeakerMinecartEntity;

public class PhonosCompatClient {
	public PhonosCompatClient() {
		EntityRendererRegistry.register(SoundTrack.phonosConstants.PHONOS_SPEAKER_MINECART_ENTITY,
				(context) -> new MinecartEntityRenderer<PhonosSpeakerMinecartEntity>(context,
						EntityModelLayers.FURNACE_MINECART));
	}
}
