//package xyz.coolsa.sound_track.compat.phonos;
//
//import io.github.foundationgames.phonos.block.LoudspeakerBlock;
//import io.github.foundationgames.phonos.block.PhonosBlocks;
//import io.github.foundationgames.phonos.client.ClientReceiverStorage;
//import io.github.foundationgames.phonos.entity.SoundPlayEntityReceivable;
//import io.github.foundationgames.phonos.item.ChannelTunerItem;
//import io.github.foundationgames.phonos.util.PhonosUtil;
//import io.github.foundationgames.phonos.world.RadioChannelState;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityPose;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.damage.DamageSource;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.vehicle.AbstractMinecartEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.nbt.NbtElement;
//import net.minecraft.particle.ParticleTypes;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.Hand;
//import net.minecraft.world.GameRules;
//import net.minecraft.world.World;
//import xyz.coolsa.sound_track.SoundTrack;
//import xyz.coolsa.sound_track.SoundTrackConstants;
//import xyz.coolsa.sound_track.compat.PhonosCompatibility;
//
//public class PhonosSpeakerMinecartEntity extends AbstractMinecartEntity implements SoundPlayEntityReceivable {
//
//	private int channel = 0;
//	private boolean updated = false;
//
//	public PhonosSpeakerMinecartEntity(EntityType<? extends PhonosSpeakerMinecartEntity> type, World world) {
//		super(type, world);
////		this.channelUpdate(this.channel);
//	}
//
//	public PhonosSpeakerMinecartEntity(World world, double x, double y, double z) {
//		super(SoundTrack.phonosConstants.PHONOS_SPEAKER_MINECART_ENTITY, world, x, y, z);
////		this.channelUpdate(this.channel);
//	}
//
//	@Override
//	public Type getMinecartType() {
//		// We return the chest type, because thats the weight i think it should have.
//		return Type.CHEST;
//	}
//	
//    @Override
//    public Item getItem() {
//        return SoundTrack.phonosConstants.PHONOS_SPEAKER_MINECART_ITEM;
//    }
//
//	@Override
//	public ItemStack getPickBlockStack() {
//		return new ItemStack(SoundTrack.phonosConstants.PHONOS_SPEAKER_MINECART_ITEM);
//	}
//
//	@Override
//	public void removeAllPassengers() {
//		RadioChannelState pstate = PhonosUtil.getRadioState((ServerWorld) this.world);
//		pstate.removeEntityReceiver(this.channel, this);
//		super.removeAllPassengers();
//	}
//
//	@Override
//	public BlockState getDefaultContainedBlock() {
//		return PhonosBlocks.LOUDSPEAKER.getDefaultState().with(LoudspeakerBlock.CHANNEL, channel);
//	}
//
//	@Override
//	public void readNbt(NbtCompound nbt) {
//		super.readNbt(nbt);
//		if (nbt.contains("Channel", NbtElement.INT_TYPE)) {
//			this.channel = nbt.getInt("Channel");
//		}
//	}
//
//	@Override
//	public NbtCompound writeNbt(NbtCompound nbt) {
//		super.writeNbt(nbt);
//		nbt.putInt("Channel", (this.channel));
//		return nbt;
//	}
//
//	@Override
//	public ActionResult interact(PlayerEntity player, Hand hand) {
//		ActionResult result = ActionResult.SUCCESS;
//		int oldChannel = this.channel;
//		if (!player.world.isClient) {
//			// if the player is holding a channel tuner item,
//			if ((player.getStackInHand(hand).getItem() instanceof ChannelTunerItem)) {
//				// copy that channel tuner data into the speaker minecart.
//				this.channel = player.getStackInHand(hand).getOrCreateSubNbt("TunerData").getInt("Channel");
//			} else if (player.getPose() == EntityPose.CROUCHING) {
//				this.channel--;
//				if (this.channel < 0)
//					this.channel = 19;
//				// and update the record playback.
//			} else {
//				this.channel++;
//				if (this.channel > 19)
//					this.channel = 0;
//			}
//			this.channelUpdate(oldChannel);
//		}
//		return result; // return the action result
//	}
//
//	private void channelUpdate(int oldChannel) { // update the entity's channel.
//		if (this.world instanceof ServerWorld) {
//			this.setCustomBlock(PhonosBlocks.LOUDSPEAKER.getDefaultState().with(LoudspeakerBlock.CHANNEL, channel));
//			RadioChannelState pstate = PhonosUtil.getRadioState((ServerWorld) this.world);
//			pstate.removeEntityReceiver(oldChannel, this);
//			pstate.addEntityReceiver(this.channel, this);
//		}
//	}
//
//	@Override
//	public void tick() {
//		if(!this.world.isClient && !updated) { //on the first tick, make sure the entity is proper.
//			this.channelUpdate(this.channel);
//			updated = true;
//		}
//		if(this.world.isClient && this.random.nextDouble() > 0.8d) {
//			if(ClientReceiverStorage.isChannelPlaying(this.getContainedBlock().get(LoudspeakerBlock.CHANNEL))){
//				double t = (double) world.getTime() / 80;
//				float c = (float) (t - Math.floor(t)) * 2;
//				world.addParticle(ParticleTypes.NOTE, (double) this.getX(), (double) this.getY() + 1.2,
//						(double) this.getZ(), c, 0.0D, 0.0D);
//			}
//		}
//		super.tick();
//	}
//	
//	public int getChannel() {
//		return this.channel;
//	}
//
//	@Override
//	protected void moveOffRail() {
////		this.powered = false;
//		super.moveOffRail();
//	}
//
//	@Override
//	@Environment(EnvType.CLIENT)
//	public void onRecievedSoundClient(ClientWorld world, Entity entity, int channel, float volume, float pitch) {
//		double t = (double) world.getTime() / 80;
//		float c = (float) (t - Math.floor(t)) * 2;
//		world.addParticle(ParticleTypes.NOTE, (double) this.getX(), (double) this.getY() + 1.2,
//				(double) this.getZ(), c, 0.0D, 0.0D);
//	}
//}
