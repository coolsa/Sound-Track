package xyz.coolsa.sound_track;

import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class SoundTrackClient implements ClientModInitializer {
    private static ClientPacketHandler handler;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(SoundTrackConstants.JUKEBOX_MINECART_ENTITY,
            (context) -> new MinecartEntityRenderer<JukeboxMinecartEntity>(context,
                EntityModelLayers.FURNACE_MINECART));
        EntityRendererRegistry.INSTANCE.register(SoundTrackConstants.NOTEBLOCK_MINECART_ENTITY,
            (context) -> new MinecartEntityRenderer<NoteblockMinecartEntity>(context,
                EntityModelLayers.FURNACE_MINECART));
        handler = new ClientPacketHandler();
    }
}
