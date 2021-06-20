package xyz.coolsa.jukebox.mixin;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Taken from "OnDeck"
 * @author coolsa
 *
 */
@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
	@Accessor("soundSystem")
	public SoundSystem getSoundSystem();
}
