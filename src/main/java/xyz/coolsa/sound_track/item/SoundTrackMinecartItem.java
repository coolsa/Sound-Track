package xyz.coolsa.sound_track.item;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import xyz.coolsa.sound_track.entity.type.SoundTrackMinecartType;

public class SoundTrackMinecartItem extends Item {
	private final SoundTrackMinecartType minecartType;
	private static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		private final ItemDispenserBehavior defaultBehavior = new ItemDispenserBehavior();

		/*
		 * WARNING - void declaration
		 */
		@Override
		public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			double yOffset = 0.0;
			Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
			World world = pointer.getWorld();
			double xPos = pointer.getX() + direction.getOffsetX() * 1.125;
			double yPos = Math.floor(pointer.getY()) + direction.getOffsetY();
			double zPos = pointer.getZ() + direction.getOffsetZ() * 1.125;
			BlockPos blockPos = pointer.getPos().offset(direction);
			BlockState blockState13 = world.getBlockState(blockPos);
			RailShape railShape14 = (blockState13.getBlock() instanceof AbstractRailBlock)
					? blockState13.<RailShape>get(((AbstractRailBlock) blockState13.getBlock()).getShapeProperty())
					: RailShape.NORTH_SOUTH;
			if (blockState13.isIn(BlockTags.RAILS)) {
				if (railShape14.isAscending()) {
					yOffset = 0.6;
				} else {
					yOffset = 0.1;
				}
			} else {
				if (!blockState13.isAir() || !world.getBlockState(blockPos.down()).isIn(BlockTags.RAILS)) {
					return this.defaultBehavior.dispense(pointer, stack);
				}
				BlockState blockState = world.getBlockState(blockPos.down());
				RailShape railShape = (blockState.getBlock() instanceof AbstractRailBlock)
						? blockState.<RailShape>get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty())
						: RailShape.NORTH_SOUTH;
				if (direction == Direction.DOWN || !railShape.isAscending()) {
					yOffset = -0.9;
				} else {
					yOffset = -0.4;
				}
			}
			AbstractMinecartEntity minecartEntity = ((SoundTrackMinecartItem)stack.getItem()).getMinecartType().createMinecartEntity(world, xPos,
					yPos + yOffset, zPos);
			if (stack.hasCustomName()) {
				minecartEntity.setCustomName(stack.getName());
			}
			world.spawnEntity(minecartEntity);
			stack.decrement(1);
			return stack;
		}

		@Override
		protected void playSound(BlockPointer pointer) {
			pointer.getWorld().syncWorldEvent(1000, pointer.getPos(), 0);
		}
	};

	public SoundTrackMinecartItem(SoundTrackMinecartType minecartType, Settings settings) {
		super(settings);
		this.minecartType = minecartType;
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState blockState = world.getBlockState(pos);
		if (!blockState.isIn(BlockTags.RAILS)) {
			return ActionResult.FAIL;
		}
		ItemStack item = context.getStack();
		if (!world.isClient) {
			RailShape rail = (blockState.getBlock() instanceof AbstractRailBlock)
					? blockState.<RailShape>get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty())
					: RailShape.NORTH_SOUTH;
			double slope = 0.0;
			if (rail.isAscending()) {
				slope = 0.5;
			}
			AbstractMinecartEntity minecart = ((SoundTrackMinecartItem)item.getItem()).getMinecartType().createMinecartEntity(world,
					pos.getX() + 0.5, pos.getY() + 0.0625 + slope, pos.getZ() + 0.5);
			if (item.hasCustomName()) {
				minecart.setCustomName(item.getName());
			}
			world.spawnEntity(minecart);
			world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos);
		}
		item.decrement(1);
		return ActionResult.success(world.isClient);
	}
	
	public SoundTrackMinecartType getMinecartType() {
		return this.minecartType;
	}
}
