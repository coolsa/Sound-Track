package xyz.coolsa.sound_track.compat;

import com.github.sydist.Items;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class StormfestCompatibility {
	private boolean loaded;
	
	public StormfestCompatibility() {
		loaded = FabricLoader.getInstance().isModLoaded("stormfest");
	}
	
	public boolean isLoaded() {
		return this.loaded;
	}
	
	public boolean isChargedMusicDisc(Item item) {
		return (item == Items.CHARGED_MUSIC_DISC_STORMFEST);
	}
	
	public ItemStack handleChargedMusicDisc(ServerWorld world) {
		world.setWeather(0, 6000, true, true);
		return Items.MUSIC_DISC_STORMFEST.getDefaultStack().copy();
	}
}
