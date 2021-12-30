package xyz.coolsa.sound_track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.minecraft.util.registry.Registry;
import xyz.coolsa.sound_track.compat.PhonosCompatibility;
import xyz.coolsa.sound_track.compat.StormfestCompatibility;
import xyz.coolsa.sound_track.compat.phonos.PhonosCompatConstants;
import xyz.coolsa.sound_track.entity.type.JukeboxMinecartType;

public class SoundTrack implements ModInitializer {
	public static final Logger LOG = LogManager.getLogger("sound_track");
	
	public static PhonosCompatibility phonos;
	public static PhonosCompatConstants phonosConstants;
//
	public static StormfestCompatibility stormfest;
	@Override
	public void onInitialize() {
		phonos = new PhonosCompatibility();
		stormfest = new StormfestCompatibility();
		// register the items and the entities.
		Registry.register(Registry.ITEM, SoundTrackConstants.JUKEBOX_MINECART_ITEM_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.JUKEBOX_MINECART_ENTITY_ID,
				SoundTrackConstants.JUKEBOX_MINECART_ENTITY);
		Registry.register(Registry.ITEM, SoundTrackConstants.NOTE_BLOCK_MINECART_ITEM_ID,
				SoundTrackConstants.NOTE_BLOCK_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, SoundTrackConstants.NOTE_BLOCK_MINECART_ENTITY_ID,
				SoundTrackConstants.NOTE_BLOCK_MINECART_ENTITY);
		MinecartComparatorLogicRegistry.register(SoundTrackConstants.JUKEBOX_MINECART_ENTITY, new JukeboxMinecartType());
		if (phonos.supportsEntities()) {
			SoundTrack.phonosConstants = new PhonosCompatConstants();
		}
	}
}
