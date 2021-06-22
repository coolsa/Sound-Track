package xyz.coolsa.sound_track;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity.Type;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class NoteblockMinecartItem extends Item {
	private static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		private final ItemDispenserBehavior defaultBehavior = new ItemDispenserBehavior();

		/*
		 * WARNING - void declaration
		 */
		@Override
		public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			double double15 = 0.0;
			Direction direction4 = pointer.getBlockState().get(DispenserBlock.FACING);
			World world5 = pointer.getWorld();
			double double6 = pointer.getX() + direction4.getOffsetX() * 1.125;
			double double8 = Math.floor(pointer.getY()) + direction4.getOffsetY();
			double double10 = pointer.getZ() + direction4.getOffsetZ() * 1.125;
			BlockPos blockPos12 = pointer.getBlockPos().offset(direction4);
			BlockState blockState13 = world5.getBlockState(blockPos12);
			RailShape railShape14 = (blockState13.getBlock() instanceof AbstractRailBlock)
					? blockState13.<RailShape>get(((AbstractRailBlock) blockState13.getBlock()).getShapeProperty())
					: RailShape.NORTH_SOUTH;
			if (blockState13.isIn(BlockTags.RAILS)) {
				if (railShape14.isAscending()) {
					double15 = 0.6;
				} else {
					double15 = 0.1;
				}
			} else {
				if (!blockState13.isAir() || !world5.getBlockState(blockPos12.down()).isIn(BlockTags.RAILS)) {
					return this.defaultBehavior.dispense(pointer, stack);
				}
				BlockState blockState17 = world5.getBlockState(blockPos12.down());
				RailShape railShape18 = (blockState17.getBlock() instanceof AbstractRailBlock)
						? blockState17.<RailShape>get(((AbstractRailBlock) blockState17.getBlock()).getShapeProperty())
						: RailShape.NORTH_SOUTH;
				if (direction4 == Direction.DOWN || !railShape18.isAscending()) {
					double15 = -0.9;
				} else {
					double15 = -0.4;
				}
			}
			AbstractMinecartEntity abstractMinecartEntity17 = new NoteblockMinecartEntity(world5, double6,
					double8 + double15, double10);
			if (stack.hasCustomName()) {
				abstractMinecartEntity17.setCustomName(stack.getName());
			}
			world5.spawnEntity(abstractMinecartEntity17);
			stack.decrement(1);
			return stack;
		}

		@Override
		protected void playSound(BlockPointer pointer) {
			pointer.getWorld().syncWorldEvent(1000, pointer.getBlockPos(), 0);
		}
	};

	public NoteblockMinecartItem(Settings settings) {
		super(settings);
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
		// TODO Auto-generated constructor stub
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
			AbstractMinecartEntity minecart = new NoteblockMinecartEntity(world,
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
}
