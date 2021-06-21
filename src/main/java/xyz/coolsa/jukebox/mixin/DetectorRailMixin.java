package xyz.coolsa.jukebox.mixin;

import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import xyz.coolsa.jukebox.JukeboxMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.gui.screen.TitleScreen;
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
		List<JukeboxMinecartEntity> list = this.getCarts(world, pos, JukeboxMinecartEntity.class, arg -> true);
//		System.out.println(list);
		if (!list.isEmpty() && !list.get(0).getRecord().isEmpty()
				&& list.get(0).getRecord().getItem() instanceof MusicDiscItem) {
			info.setReturnValue(((MusicDiscItem) list.get(0).getRecord().getItem()).getComparatorOutput());
		}
	}
}
