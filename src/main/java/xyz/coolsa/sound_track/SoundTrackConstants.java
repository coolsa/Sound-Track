package xyz.coolsa.sound_track;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
//import xyz.coolsa.sound_track.entity.JukeboxBoatEntity;
import xyz.coolsa.sound_track.entity.JukeboxMinecartEntity;
import xyz.coolsa.sound_track.entity.NoteBlockMinecartEntity;
import xyz.coolsa.sound_track.entity.type.JukeboxMinecartType;
import xyz.coolsa.sound_track.entity.type.NoteBlockMinecartType;
import xyz.coolsa.sound_track.item.SoundTrackMinecartItem;

public class SoundTrackConstants {
    /**
     * Name of the mod, for all identifiers used.
     */
    public static final String MOD_NAME = "sound_track";
    /**
     * Jukebox entity packet identifier.
     */
    public static final Identifier JUKEBOX_ENTITY_PLAY = new Identifier(MOD_NAME, "jukebox_entity_play");
    /**
     * The ID of the jukebox minecart item.
     */
    public static final Identifier JUKEBOX_MINECART_ITEM_ID = new Identifier(MOD_NAME, "jukebox_minecart");
    /**
     * The ID of the jukebox minecart entity.
     */
    public static final Identifier JUKEBOX_MINECART_ENTITY_ID = new Identifier(MOD_NAME, "jukebox_minecart");
    /**
     * The actual jukebox minecart item.
     */
    public static final Item JUKEBOX_MINECART_ITEM = new SoundTrackMinecartItem(new JukeboxMinecartType(),
            new Item.Settings().maxCount(1));
    /**
     * The actual jukebox minecart entity.
     */
    public static final EntityType<JukeboxMinecartEntity> JUKEBOX_MINECART_ENTITY = EntityType.Builder
            .<JukeboxMinecartEntity>create(JukeboxMinecartEntity::new, SpawnGroup.MISC).setDimensions(0.98f, 0.7f)
            .maxTrackingRange(8).build(JUKEBOX_MINECART_ENTITY_ID.toString());

//    /**
//     * The ID of the jukebox boat item.
//     */
//    public static final Identifier JUKEBOX_BOAT_ITEM_ID = new Identifier(MOD_NAME, "jukebox_boat");
//    /**
//     * The ID of the jukebox boat entity.
//     */
//    public static final Identifier JUKEBOX_BOAT_ENTITY_ID = new Identifier(MOD_NAME, "jukebox_boat");
//    /**
//     * The actual jukebox boat item.
//     */
//    public static final Item JUKEBOX_BOAT_ITEM = new Item(
//            new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION));
//    /**
//     * The actual jukebox boat entity.
//     */
//    public static final EntityType<JukeboxBoatEntity> JUKEBOX_BOAT_ENTITY = EntityType.Builder
//            .<JukeboxBoatEntity>create(JukeboxBoatEntity::new, SpawnGroup.MISC).setDimensions(1.375f, 0.5625f)
//            .maxTrackingRange(10).build(JUKEBOX_MINECART_ENTITY_ID.toString());

    /**
     * Note block minecart packet identifier.
     */
    public static final Identifier NOTE_BLOCK_ENTITY_PLAY = new Identifier(MOD_NAME, "note_block_entity_play");
    /**
     * The ID of the note block minecart item.
     */
    public static final Identifier NOTE_BLOCK_MINECART_ITEM_ID = new Identifier(MOD_NAME, "note_block_minecart");
    /**
     * The ID of the note block minecart entity.
     */
    public static final Identifier NOTE_BLOCK_MINECART_ENTITY_ID = new Identifier(MOD_NAME, "note_block_minecart");
    /**
     * The actual note block minecart item.
     */
    public static final Item NOTE_BLOCK_MINECART_ITEM = new SoundTrackMinecartItem(new NoteBlockMinecartType(),
            new Item.Settings().maxCount(1));
    /**
     * The actual noteblock minecart entity.
     */
    public static final EntityType<NoteBlockMinecartEntity> NOTE_BLOCK_MINECART_ENTITY = EntityType.Builder
            .<NoteBlockMinecartEntity>create(NoteBlockMinecartEntity::new, SpawnGroup.MISC).setDimensions(0.98f, 0.7f)
            .maxTrackingRange(8).build(NOTE_BLOCK_MINECART_ENTITY_ID.toString());
}
