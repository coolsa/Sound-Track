package xyz.coolsa.sound_track.compat.phonos;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.coolsa.sound_track.SoundTrackConstants;
import xyz.coolsa.sound_track.item.SoundTrackMinecartItem;

public class PhonosCompatConstants {

	public final Identifier PHONOS_SPEAKER_MINECART_PLAY = new Identifier(SoundTrackConstants.MOD_NAME,
			"phonos_speaker_minecart_play");
	/**
	 * The ID of the jukebox minecart item.
	 */
	public final Identifier PHONOS_SPEAKER_MINECART_ITEM_ID = new Identifier(SoundTrackConstants.MOD_NAME,
			"phonos_speaker_minecart");
	/**
	 * The ID of the jukebox minecart entity.
	 */
	public final Identifier PHONOS_SPEAKER_MINECART_ENTITY_ID = new Identifier(SoundTrackConstants.MOD_NAME,
			"phonos_speaker_minecart");
	/**
	 * The actual jukebox minecart item.
	 */
	public final Item PHONOS_SPEAKER_MINECART_ITEM = new SoundTrackMinecartItem(new PhonosSpeakerMinecartType(),
			new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION));
	/**
	 * The actual jukebox minecart entity.
	 */
	public final EntityType<PhonosSpeakerMinecartEntity> PHONOS_SPEAKER_MINECART_ENTITY = EntityType.Builder
			.<PhonosSpeakerMinecartEntity>create(PhonosSpeakerMinecartEntity::new, SpawnGroup.MISC)
			.setDimensions(0.98f, 0.7f).maxTrackingRange(8).build(PHONOS_SPEAKER_MINECART_ENTITY_ID.toString());

	public PhonosCompatConstants() {
		Registry.register(Registry.ITEM, this.PHONOS_SPEAKER_MINECART_ITEM_ID, this.PHONOS_SPEAKER_MINECART_ITEM);
		Registry.register(Registry.ENTITY_TYPE, this.PHONOS_SPEAKER_MINECART_ENTITY_ID,
				this.PHONOS_SPEAKER_MINECART_ENTITY);
	}
}
