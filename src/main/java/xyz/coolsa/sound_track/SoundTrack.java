package xyz.coolsa.sound_track;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SoundTrack implements ModInitializer, ClientModInitializer {
	private static ClientPacketHandler handler;

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, SoundTrackConstants.JUKEBOX_MINECART_ITEM_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ITEM);
		System.out.println("Hello Fabric world!");
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.JUKEBOX_MINECART_ENTITY_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ENTITY);
		Registry.register(Registry.ITEM, SoundTrackConstants.NOTEBLOCK_MINECART_ITEM_ID,
				SoundTrackConstants.NOTEBLOCK_MINECART_ITEM);
		System.out.println("Hello Fabric world!");
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.NOTEBLOCK_MINECART_ENTITY_ID,
				SoundTrackConstants.NOTEBLOCK_MINECART_ENTITY);
//		FabricDefaultAttributeRegistry.register(JUKEBOX_MINECART_ENTITY, );
	}

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
