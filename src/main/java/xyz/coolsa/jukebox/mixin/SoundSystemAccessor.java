package xyz.coolsa.jukebox.mixin;

import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundExecutor;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.SoundListener;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.google.common.collect.Multimap;

/**
 * Taken from "OnDeck"
 * @author coolsa
 *
 */
@Mixin(SoundSystem.class)
public interface SoundSystemAccessor {
	@Accessor("started")
	public boolean getStarted();

	@Accessor("channel")
	public Channel getChannel();

	@Accessor("soundEngine")
	public SoundEngine getSoundEngine();

	@Accessor("taskQueue")
	public SoundExecutor getTaskQueue();

	@Accessor("listeners")
	public List<SoundInstanceListener> getListeners();

	@Accessor("soundEndTicks")
	public Map<SoundInstance, Integer> getSoundEndTicks();

	@Accessor("sources")
	public Map<SoundInstance, Channel.SourceManager> getSources();

	@Accessor("sounds")
	public Multimap<SoundCategory, SoundInstance> getSounds();

	@Accessor("tickingSounds")
	public List<TickableSoundInstance> getTickingSounds();

	@Accessor("listener")
	public SoundListener getListener();

	@Accessor("loader")
	public SoundManager getLoader();

	@Accessor("ticks")
	public int getTicks();

	@Invoker("getAdjustedVolume")
	public float invokeGetAdjustedVolume(SoundInstance soundInstance);

	@Invoker("getAdjustedPitch")
	public float invokeGetAdjustedPitch(SoundInstance soundInstance);
}
