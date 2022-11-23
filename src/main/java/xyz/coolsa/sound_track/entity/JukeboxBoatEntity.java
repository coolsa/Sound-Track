//package xyz.coolsa.sound_track.entity;
//
//import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
//import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.ItemEntity;
//import net.minecraft.entity.RideableInventory;
//import net.minecraft.entity.damage.DamageSource;
//import net.minecraft.entity.mob.PiglinBrain;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.vehicle.BoatEntity;
//import net.minecraft.inventory.Inventory;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.item.MusicDiscItem;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.nbt.NbtElement;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.stat.Stats;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.Clearable;
//import net.minecraft.util.Hand;
//import net.minecraft.util.ItemScatterer;
//import net.minecraft.world.GameRules;
//import net.minecraft.world.World;
//import net.minecraft.world.event.GameEvent;
//import xyz.coolsa.sound_track.SoundTrack;
//import xyz.coolsa.sound_track.SoundTrackConstants;
//
//public class JukeboxBoatEntity extends BoatEntity implements Clearable {
//    
//    private ItemStack record = ItemStack.EMPTY;
//
//    public JukeboxBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
//        super(entityType, world);
//    }
//    
//    public JukeboxBoatEntity(World world, double d, double e, double f) {
//        this((EntityType<? extends BoatEntity>)SoundTrackConstants.JUKEBOX_BOAT_ENTITY, world);
//        this.setPosition(d, e, f);
//        this.prevX = d;
//        this.prevY = e;
//        this.prevZ = f;
//    }
//
//    @Override
//    protected float getPassengerHorizontalOffset() {
//        return 0.15f;
//    }
//
//    @Override
//    protected int getMaxPassengers() {
//        return 1;
//    }
//
//    @Override
//    protected void writeCustomDataToNbt(NbtCompound nbt) {
//        super.writeCustomDataToNbt(nbt);
//        if (!this.record.isEmpty()) {
//            nbt.put("RecordItem", this.record.writeNbt(new NbtCompound()));
//        }
//    }
//    @Override
//    protected void readCustomDataFromNbt(NbtCompound nbt) {
//        super.readCustomDataFromNbt(nbt);
//        if (nbt.contains("RecordItem", NbtElement.COMPOUND_TYPE)) {
//            this.record = ItemStack.fromNbt(nbt.getCompound("RecordItem"));
//        }
//    }
//    
//    @Override
//    public void dropItems(DamageSource source) {
//        super.dropItems(source);
//        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
//            this.dropStack(this.record);
//        }
//    }
//    
//    @Override
//    public void remove(Entity.RemovalReason reason) {
//        if (!this.world.isClient && reason.shouldDestroy()) {
//            ItemScatterer.spawn(this.world, this.getX(), this.getY(), this.getZ(), this.record);
//        }
//        super.remove(reason);
//    }
//    
//    @Override
//    public ActionResult interact(PlayerEntity player, Hand hand) {
//        if (!this.canAddPassenger(player) || player.shouldCancelInteraction()) {
//            ActionResult result = ActionResult.SUCCESS;
//            if (!player.world.isClient) {
//                // if we have a normal music disc, or it is a phonos custom music disc,
//                if ((player.getStackInHand(hand).getItem() instanceof MusicDiscItem ) //TODO: get rid of bracket when phonos is updated.
////                      || (SoundTrack.phonos.isLoaded() && SoundTrack.phonos.isCustomMusicDisc(player.getStackInHand(hand).getItem())))
//                        && this.record.isEmpty()) {
//                    this.record = player.getStackInHand(hand).copy(); // copy the players record
//                    this.record.setCount(1); // set the count to 1 (only play 1 record)
//                    if (!player.isCreative()) { // if they are not in creative
//                        player.getStackInHand(hand).decrement(1); // decrement the records held by 1, in line with vanilla.
//                    }
//                    // if we have stormfest, lets do some compat stuff here. THUNDER! ah ooh ah ah ooh ahh
//                    if(SoundTrack.stormfest.isLoaded() && SoundTrack.stormfest.isChargedMusicDisc(this.record.getItem())) {
//                        this.record = SoundTrack.stormfest.handleChargedMusicDisc((ServerWorld) player.world);
//                    }
//                    player.incrementStat(Stats.PLAY_RECORD); // increment their stat.
//                    this.playRecord(); // actually play the record
//                    result = ActionResult.CONSUME; // its a consume action.
//                } else if (!this.record.isEmpty()) { // if we already have something in the boat,
//                    double randomX = this.world.random.nextFloat() * 0.7 - 0.5; // random position near the boat
//                    double randomY = this.world.random.nextFloat() * 0.7 + 0.66;
//                    double randomZ = this.world.random.nextFloat() * 0.7 - 0.5;
//                    ItemEntity entity = new ItemEntity(this.world, this.getX() + randomX, this.getY() + randomY,
//                            this.getZ() + randomZ, this.record.copy()); // create the item entity.
//                    entity.setToDefaultPickupDelay(); // we go ahead and give it a default pickup delay
//                    this.world.spawnEntity(entity); // spawn the item
//                    this.record = ItemStack.EMPTY; // the jukebox boat is now empty
//                    this.playRecord(); // and update the record playback.
//                }
//            }
////          SoundTrack.LOG.info("asdf");
//            return result; // return the action result
//        }
//        return super.interact(player, hand);
//    }
//
//    private void playRecord() { // send the packet to play a record.
//        PacketByteBuf buf = PacketByteBufs.create();
//        buf.writeInt(this.getId()); // read/write this entity's id to a packet
//        buf.writeItemStack(this.record); // also write the record that it has into the packet
//        buf.writeLong(0); // TODO: add seeking to the playback.
//        // loop through all of the players and send them the packet for playback.
//        for (ServerPlayerEntity players : PlayerLookup.around((ServerWorld) world, this.getBlockPos(), 128))
//            ServerPlayNetworking.send(players, SoundTrackConstants.JUKEBOX_ENTITY_PLAY, buf);
//    }
//    
//    public ItemStack getRecord() {
//        return this.record;
//    }
//
//    @Override
//    public Item asItem() {
//        return switch (this.getBoatType()) {
//            case SPRUCE -> Items.SPRUCE_CHEST_BOAT;
//            case BIRCH -> Items.BIRCH_CHEST_BOAT;
//            case JUNGLE -> Items.JUNGLE_CHEST_BOAT;
//            case ACACIA -> Items.ACACIA_CHEST_BOAT;
//            case DARK_OAK -> Items.DARK_OAK_CHEST_BOAT;
//            case MANGROVE -> Items.MANGROVE_CHEST_BOAT;
//            default -> Items.OAK_CHEST_BOAT;
//        };
//    }
//
//    @Override
//    public void clear() {
//        this.record = ItemStack.EMPTY;
//    }
//
//}
