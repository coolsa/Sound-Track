package xyz.coolsa.sound_track.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Clearable;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import xyz.coolsa.sound_track.SoundTrack;
import xyz.coolsa.sound_track.SoundTrackConstants;

public class JukeboxMinecartEntity extends AbstractMinecartEntity implements Clearable {

	private ItemStack record = ItemStack.EMPTY;
	private boolean powered = false;
	private BlockPos pos = this.getBlockPos();

	public JukeboxMinecartEntity(EntityType<? extends JukeboxMinecartEntity> type, World world) {
		super(type, world);
	}

	public JukeboxMinecartEntity(World world, double x, double y, double z) {
		super(SoundTrackConstants.JUKEBOX_MINECART_ENTITY, world, x, y, z);
	}

	@Override
	public Type getMinecartType() {
		// We return the chest type, because thats the weight i think it should have.
		return Type.CHEST;
    }
    
    @Override
    public Item getItem() {
        return SoundTrackConstants.JUKEBOX_MINECART_ITEM;
    }
    

	@Override
	public ItemStack getPickBlockStack() {
		return new ItemStack(SoundTrackConstants.JUKEBOX_MINECART_ITEM);
	}

	@Override
	public void dropItems(DamageSource damageSource) {
		super.dropItems(damageSource);
		if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
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
		if (nbt.contains("RecordItem", NbtElement.COMPOUND_TYPE)) {
			this.record = ItemStack.fromNbt(nbt.getCompound("RecordItem"));
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
		if (!this.record.isEmpty()) {
			nbt.put("RecordItem", this.record.writeNbt(new NbtCompound()));
		}
		nbt.putInt("Powered", (this.powered) ? 1 : 0);
		nbt.putLong("Position", this.pos.asLong());
		return nbt;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		ActionResult result = ActionResult.SUCCESS;
		if (!player.world.isClient) {
			// if we have a normal music disc, or it is a phonos custom music disc,
			if ((player.getStackInHand(hand).getItem() instanceof MusicDiscItem ) //TODO: get rid of bracket when phonos is updated.
//					|| (SoundTrack.phonos.isLoaded() && SoundTrack.phonos.isCustomMusicDisc(player.getStackInHand(hand).getItem())))
					&& this.record.isEmpty()) {
				this.record = player.getStackInHand(hand).copy(); // copy the players record
				this.record.setCount(1); // set the count to 1 (only play 1 record)
				if (!player.isCreative()) { // if they are not in creative
					player.getStackInHand(hand).decrement(1); // decrement the records held by 1, in line with vanilla.
				}
				// if we have stormfest, lets do some compat stuff here. THUNDER! ah ooh ah ah ooh ahh
//				if(SoundTrack.stormfest.isLoaded() && SoundTrack.stormfest.isChargedMusicDisc(this.record.getItem())) {
//					this.record = SoundTrack.stormfest.handleChargedMusicDisc((ServerWorld) player.world);
//				}
				player.incrementStat(Stats.PLAY_RECORD); // increment their stat.
				this.playRecord(); // actually play the record
				result = ActionResult.CONSUME; // its a consume action.
			} else if (!this.record.isEmpty()) { // if we already have something in the minecart,
				double randomX = this.world.random.nextFloat() * 0.7 - 0.5; // random position near the minecart
				double randomY = this.world.random.nextFloat() * 0.7 + 0.66;
				double randomZ = this.world.random.nextFloat() * 0.7 - 0.5;
				ItemEntity entity = new ItemEntity(this.world, this.getX() + randomX, this.getY() + randomY,
						this.getZ() + randomZ, this.record.copy()); // create the item entity.
				entity.setToDefaultPickupDelay(); // we go ahead and give it a default pickup delay
				this.world.spawnEntity(entity); // spawn the item
				this.record = ItemStack.EMPTY; // the jukebox minecart is now empty
				this.playRecord(); // and update the record playback.
			}
		}
//		SoundTrack.LOG.info("asdf");
		return result; // return the action result
	}

	private void playRecord() { // send the packet to play a record.
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(this.getId()); // read/write this entity's id to a packet
		buf.writeItemStack(this.record); // also write the record that it has into the packet
		buf.writeLong(0); // TODO: add seeking to the playback.
		// loop through all of the players and send them the packet for playback.
		for (ServerPlayerEntity players : PlayerLookup.around((ServerWorld) world, this.getBlockPos(), 128))
			ServerPlayNetworking.send(players, SoundTrackConstants.JUKEBOX_ENTITY_PLAY, buf);
	}

	public ItemStack getRecord() {
		return this.record;
	}

	@Override
	public void onActivatorRail(int x, int y, int z, boolean powered) {
		if (powered && !this.powered) {
			this.playRecord();
		}
		this.powered = powered;
	}

	@Override
	protected void moveOnRail(BlockPos pos, BlockState state) {
		if (!state.isOf(Blocks.ACTIVATOR_RAIL))
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
