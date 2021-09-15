package xyz.coolsa.sound_track;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class SoundTrack implements ModInitializer {
	public static PhonosCompatibility phonos;
	@Override
	public void onInitialize() {
		phonos = new PhonosCompatibility();
		//register the items and the entities.
		Registry.register(Registry.ITEM, SoundTrackConstants.JUKEBOX_MINECART_ITEM_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.JUKEBOX_MINECART_ENTITY_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ENTITY);
		Registry.register(Registry.ITEM, SoundTrackConstants.NOTE_BLOCK_MINECART_ITEM_ID,
				SoundTrackConstants.NOTE_BLOCK_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.NOTE_BLOCK_MINECART_ENTITY_ID,
				SoundTrackConstants.NOTE_BLOCK_MINECART_ENTITY);
//		FabricDefaultAttributeRegistry.register(JUKEBOX_MINECART_ENTITY, );
	}
}
