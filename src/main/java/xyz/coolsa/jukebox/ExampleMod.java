package xyz.coolsa.jukebox;

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

public class ExampleMod implements ModInitializer, ClientModInitializer {
	private static final Identifier id = new Identifier("jukeboxmod", "jukebox_minecart");
	public static final Item JUKEBOX_MINECART_ITEM = new JukeboxMinecartItem(
			new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION));
	public static final EntityType<JukeboxMinecartEntity> JUKEBOX_MINECART_ENTITY =
//			FabricEntityTypeBuilder
//			.<JukeboxMinecartEntity>create().entityFactory(JukeboxMinecartEntity::new).spawnGroup(SpawnGroup.MISC)
//			.build();
//			Registry.register(Registry.ENTITY_TYPE, id,
			EntityType.Builder.<JukeboxMinecartEntity>create(JukeboxMinecartEntity::new, SpawnGroup.MISC)
					.setDimensions(0.98f, 0.7f).maxTrackingRange(8).build(id.toString());

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("jukeboxmod", "jukebox_minecart"), JUKEBOX_MINECART_ITEM);
		System.out.println("Hello Fabric world!");
	}

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(JUKEBOX_MINECART_ENTITY,
				(context) -> new MinecartEntityRenderer<JukeboxMinecartEntity>(context,
						EntityModelLayers.FURNACE_MINECART));
	}
}
