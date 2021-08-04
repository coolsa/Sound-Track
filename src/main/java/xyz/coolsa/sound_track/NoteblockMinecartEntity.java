package xyz.coolsa.sound_track;

import java.util.Iterator;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class NoteblockMinecartEntity extends AbstractMinecartEntity {

	// initiate note to lowest note by default.
	private Integer note = Properties.NOTE.getValues().iterator().next();
	private boolean powered = false;
	private BlockPos pos = this.getBlockPos();
//	private static final int min_note;

	public NoteblockMinecartEntity(EntityType<? extends NoteblockMinecartEntity> type, World world) {
		super(type, world);
	}

	protected NoteblockMinecartEntity(World world, double x, double y, double z) {
		super(SoundTrackConstants.NOTEBLOCK_MINECART_ENTITY, world, x, y, z);
	}

	@Override
	public Type getMinecartType() {
		// We return the chest type, because thats the weight i think it should have.
		return Type.CHEST;
	}

	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(SoundTrackConstants.NOTEBLOCK_MINECART_ITEM);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		super.dropItems(damageSource);
		if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			this.dropItem(Blocks.NOTE_BLOCK);
		}
	}

	@Override
	public BlockState getDefaultContainedBlock() {
		return Blocks.NOTE_BLOCK.getDefaultState();
	}

	@Override
	public int getDefaultBlockOffset() {
		return 8;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("Note", NbtElement.INT_TYPE)) {
			this.note = nbt.getInt("Note");
		}
		if (nbt.contains("Powered", NbtElement.INT_TYPE)) {
			this.powered = nbt.getInt("Powered") == 1;
		}
		if (nbt.contains("Position", NbtElement.LONG_TYPE)) {
			this.pos = BlockPos.fromLong(nbt.getLong("BlockPos"));
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("Note", this.note);
		nbt.putInt("Powered", (this.powered) ? 1 : 0);
		nbt.putLong("Position", this.pos.asLong());
		return nbt;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		ActionResult result = ActionResult.SUCCESS;
		if (!player.world.isClient) {
			Iterator<Integer> iterator = Properties.NOTE.getValues().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().equals(this.note))
					break;
			}
			if (iterator.hasNext()) {
				this.note = iterator.next();
			} else {
				this.note = Properties.NOTE.getValues().iterator().next();
			}
			this.playNote(null);
			result = ActionResult.CONSUME;
		}
		return result;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.world.isClient || this.isRemoved())
			return true;
		if (source.getAttacker() instanceof PlayerEntity) {
			this.playNote(null);
		}
		if (source.isSourceCreativePlayer())
			amount *= 2.2;
		this.setDamageWobbleSide(-this.getDamageWobbleSide());
		this.setDamageWobbleTicks(10);
		this.scheduleVelocityUpdate();
		this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0f);
		this.emitGameEvent(GameEvent.ENTITY_DAMAGED, source.getAttacker());
		if (this.getDamageWobbleStrength() > 40.0f) {
			this.removeAllPassengers();
			if (!source.isSourceCreativePlayer() || this.hasCustomName()) {
				this.dropItems(source);
			} else {
				this.discard();
			}
		}
		return true;
	}

	private void playNote(@Nullable BlockPos pos) {
		if (pos == null) {
			pos = getBlockPos().down();
		}
		PacketByteBuf buf = PacketByteBufs.create();
		int entityId = this.getId();
		// write where the note block minecart is.
		buf.writeInt(entityId);
		// write what block is below it.
		buf.writeBlockPos(pos);
		buf.writeInt(note);
//		buf.writeItemStack(this.record);
		for (ServerPlayerEntity players : PlayerLookup.around((ServerWorld) world, this.getBlockPos(), 128))
			ServerPlayNetworking.send(players, SoundTrackConstants.NOTEBLOCK_MINECART_PLAY, buf);
	}

	@Override
	public void onActivatorRail(int x, int y, int z, boolean powered) {
		if (powered && !this.powered) {
			this.playNote(new BlockPos(x, y - 1, z));
		}
		this.powered = powered;
	}

	@Override
	protected void moveOnRail(BlockPos pos, BlockState state) {
		if (!state.isOf(Blocks.ACTIVATOR_RAIL) || !pos.equals(this.pos))
			this.powered = false;
		this.pos = pos;
		super.moveOnRail(pos, state);
	}

	@Override
	protected void moveOffRail() {
		this.powered = false;
		super.moveOffRail();
	}

}
