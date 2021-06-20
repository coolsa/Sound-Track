package xyz.coolsa.jukebox;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.Buffer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Clearable;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class JukeboxMinecartEntity extends AbstractMinecartEntity implements Clearable {

	private ItemStack record = ItemStack.EMPTY;

	public JukeboxMinecartEntity(EntityType<? extends JukeboxMinecartEntity> type, World world) {
		super(type, world);
	}

	protected JukeboxMinecartEntity(World world, double x, double y, double z) {
		super(JukeboxConstants.JUKEBOX_MINECART_ENTITY, world, x, y, z);
	}

//	@Override
//	public int size() {
//		return 27;
//	}

//	@Override
//	protected ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
//		// TODO Auto-generated method stub
//		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
//	}

	@Override
	public Type getMinecartType() {
		// We return the chest type, because thats the weight i think it should have.
		return Type.CHEST;
	}

	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(JukeboxConstants.JUKEBOX_MINECART_ITEM);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		super.dropItems(damageSource);
		if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			this.dropItem(Blocks.JUKEBOX);
			this.dropStack(this.record);
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

	@Override
	public void clear() {
		this.record = ItemStack.EMPTY;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("RecordItem", 10)) {
			this.record = ItemStack.fromNbt(nbt.getCompound("RecordItem"));
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.record.isEmpty()) {
			nbt.put("RecordItem", this.record.writeNbt(new NbtCompound()));
		}
		return nbt;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		boolean isMusicDisc = player.getStackInHand(hand).getItem() instanceof MusicDiscItem;
		if (!player.world.isClient) {
			if (isMusicDisc && this.record.isEmpty()) {
				this.record = player.getStackInHand(hand).copy();
				if (!player.isCreative())
					player.setStackInHand(hand, ItemStack.EMPTY);
				PacketByteBuf buf = PacketByteBufs.create();
				int entityId = this.getId();
				buf.writeInt(entityId);
				buf.writeItemStack(this.record);
				System.out.println(buf);
				for (ServerPlayerEntity players : PlayerLookup.around((ServerWorld) world, this.getBlockPos(), 500))
					ServerPlayNetworking.send(players, JukeboxConstants.JUKEBOX_MINECART_PLAY, buf);
			} else if (!this.record.isEmpty()) {
				double randomX = this.world.random.nextFloat() * 0.7 - 0.5;
				double randomY = this.world.random.nextFloat() * 0.7 + 0.66;
				double randomZ = this.world.random.nextFloat() * 0.7 - 0.5;
				ItemEntity entity = new ItemEntity(this.world, this.getX() + randomX, this.getY() + randomY,
						this.getZ() + randomZ, this.record.copy());
				entity.setToDefaultPickupDelay();
				this.world.spawnEntity(entity);
				this.record = ItemStack.EMPTY;
				PacketByteBuf buf = PacketByteBufs.create();
				int entityId = this.getId();
				buf.writeInt(entityId);
				buf.writeItemStack(this.record);
				for (ServerPlayerEntity players : PlayerLookup.around((ServerWorld) world, this.getBlockPos(), 500))
					ServerPlayNetworking.send(players, JukeboxConstants.JUKEBOX_MINECART_PLAY, buf);
			}
		}
		return ActionResult.SUCCESS;
	}

	public ItemStack getRecord() {
		return this.record;
	}

}
