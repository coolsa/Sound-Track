package xyz.coolsa.sound_track;

import io.github.foundationgames.phonos.item.CustomMusicDiscItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;

public class PhonosCompatibility {
	private boolean loaded;
	public PhonosCompatibility() {
		loaded = FabricLoader.getInstance().isModLoaded("phonos");
	}
	
	public boolean isLoaded() {
		return this.loaded;
	}
	
	public boolean isCustomMusicDisc(Item item) {
		return item instanceof CustomMusicDiscItem;
	}
}
