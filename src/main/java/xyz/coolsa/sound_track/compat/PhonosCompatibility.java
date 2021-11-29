//package xyz.coolsa.sound_track.compat;
//
//import net.fabricmc.loader.api.FabricLoader;
//import net.minecraft.item.Item;
//import io.github.foundationgames.phonos.item.CustomMusicDiscItem;
//
//public class PhonosCompatibility {
//	private boolean loaded;
//	private boolean entitySound;
//	public PhonosCompatibility() {
//		loaded = FabricLoader.getInstance().isModLoaded("phonos");
//		try {
//			Class.forName("io.github.foundationgames.phonos.entity.SoundPlayEntityReceivable");
//			entitySound = true;
//		} catch (ClassNotFoundException e) {
//			entitySound = false;
//		}
//	}
//	
//	public boolean isLoaded() {
//		return this.loaded;
//	}
//	
//	public boolean supportsEntities() {
//		return this.entitySound;
//	}
//	
//	public boolean isCustomMusicDisc(Item item) {
//		return (item instanceof CustomMusicDiscItem);
//	}
//}
