package xyz.coolsa.sound_track;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class SoundTrack implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, SoundTrackConstants.JUKEBOX_MINECART_ITEM_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.JUKEBOX_MINECART_ENTITY_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ENTITY);
		Registry.register(Registry.ITEM, SoundTrackConstants.NOTEBLOCK_MINECART_ITEM_ID,
				SoundTrackConstants.NOTEBLOCK_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.NOTEBLOCK_MINECART_ENTITY_ID,
				SoundTrackConstants.NOTEBLOCK_MINECART_ENTITY);
//		FabricDefaultAttributeRegistry.register(JUKEBOX_MINECART_ENTITY, );
	}
}
