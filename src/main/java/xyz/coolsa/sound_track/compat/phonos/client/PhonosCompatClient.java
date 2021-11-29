//package xyz.coolsa.sound_track.compat.phonos.client;
//
//import io.github.foundationgames.phonos.util.PhonosUtil;
//import io.github.foundationgames.phonos.world.RadioChannelState;
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
//import net.fabricmc.fabric.api.networking.v1.PacketSender;
//import net.minecraft.block.enums.Instrument;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.ClientPlayNetworkHandler;
//import net.minecraft.client.render.entity.MinecartEntityRenderer;
//import net.minecraft.client.render.entity.model.EntityModelLayers;
//import net.minecraft.client.sound.EntityTrackingSoundInstance;
//import net.minecraft.client.sound.SoundManager;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.particle.ParticleTypes;
//import net.minecraft.sound.SoundCategory;
//import net.minecraft.util.math.BlockPos;
//import xyz.coolsa.sound_track.SoundTrack;
//import xyz.coolsa.sound_track.SoundTrackConstants;
//import xyz.coolsa.sound_track.compat.phonos.PhonosSpeakerMinecartEntity;
//import xyz.coolsa.sound_track.entity.NoteBlockMinecartEntity;
//
//public class PhonosCompatClient {
//	public PhonosCompatClient() {
//		EntityRendererRegistry.INSTANCE.register(SoundTrack.phonosConstants.PHONOS_SPEAKER_MINECART_ENTITY,
//				(context) -> new MinecartEntityRenderer<PhonosSpeakerMinecartEntity>(context,
//						EntityModelLayers.FURNACE_MINECART));
//	}
//}
