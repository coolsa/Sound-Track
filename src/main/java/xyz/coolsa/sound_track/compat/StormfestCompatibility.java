package xyz.coolsa.sound_track.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StormfestCompatibility {
	private boolean loaded;
	
	public StormfestCompatibility() {
		loaded = FabricLoader.getInstance().isModLoaded("stormfest");
	}
	
	public boolean isLoaded() {
		return this.loaded;
	}
	
	public boolean isChargedMusicDisc(Item item) {
	    Identifier itemId = Registry.ITEM.getId(item);
		return (itemId.getNamespace().equals("stormfest") && itemId.getPath().equals("enchanted_music_disc_stormfest"));
	}
	
	public ItemStack handleChargedMusicDisc(ServerWorld world) {
		world.setWeather(0, 6000, true, true);
		return Registry.ITEM.get(new Identifier("stormfest","music_disc_stormfest")).getDefaultStack().copy();
	}
}
