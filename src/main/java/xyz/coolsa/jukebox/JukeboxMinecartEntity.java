package xyz.coolsa.jukebox;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class JukeboxMinecartEntity extends StorageMinecartEntity {

	public JukeboxMinecartEntity(EntityType<? extends JukeboxMinecartEntity> type, World world) {
		super(type, world);
	}

	protected JukeboxMinecartEntity(World world, double x, double y, double z) {
		super(ExampleMod.JUKEBOX_MINECART_ENTITY, x, y, z, world);
	}

	@Override
	public int size() {
		return 27;
	}

	@Override
	protected ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
		// TODO Auto-generated method stub
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	@Override
	public Type getMinecartType() {
		// We return the chest type, because thats the weight i think it should have.
		return Type.CHEST;
	}

	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(ExampleMod.JUKEBOX_MINECART_ITEM);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		super.dropItems(damageSource);
		if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			this.dropItem(Blocks.JUKEBOX);
		}
	}

	@Override
	public BlockState getDefaultContainedBlock() {
		return Blocks.JUKEBOX.getDefaultState();
	}

	@Override
	public int getDefaultBlockOffset() {
		return 8;
	}
}
