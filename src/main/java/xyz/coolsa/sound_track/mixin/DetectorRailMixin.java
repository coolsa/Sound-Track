package xyz.coolsa.sound_track.mixin;

import net.minecraft.block.DetectorRailBlock;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import xyz.coolsa.sound_track.SoundTrack;
import xyz.coolsa.sound_track.entity.JukeboxMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.MusicDiscItem;

import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(DetectorRailBlock.class)
public class DetectorRailMixin {
	@Shadow
	private <T extends AbstractMinecartEntity> List<T> getCarts(World world, BlockPos pos, Class<T> entityClass,
			Predicate<Entity> entityPredicate) {
		return null;
	}

	@Inject(at = @At("HEAD"), method = "getComparatorOutput", cancellable = true)
	private void getJukeboxMinecartComparatorValue(BlockState state, World world, BlockPos pos,
			CallbackInfoReturnable<Integer> info) {
		// if the detector rail is not powered, lets exit.
		if (!state.get(DetectorRailBlock.POWERED).booleanValue())
			return;
		// otherwise, lets get a list of the carts on top of it.
		List<JukeboxMinecartEntity> list = this.getCarts(world, pos, JukeboxMinecartEntity.class, arg -> true);
		if (!list.isEmpty() && !list.get(0).getRecord().isEmpty()) { // if there are actually jukebox minecarts
			if (list.get(0).getRecord().getItem() instanceof MusicDiscItem) { // read the power level of vanilla disc
				info.setReturnValue(((MusicDiscItem) list.get(0).getRecord().getItem()).getComparatorOutput());
			} else if (SoundTrack.phonos.isLoaded()// read power level of phonos disc.
					&& SoundTrack.phonos.isCustomMusicDisc(list.get(0).getRecord().getItem())) {
				info.setReturnValue(list.get(0).getRecord().getOrCreateSubNbt("MusicData").getInt("ComparatorSignal"));
			}
		}
	}
}
