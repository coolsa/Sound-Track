package xyz.coolsa.sound_track.entity.type;

import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.coolsa.sound_track.SoundTrack;
import xyz.coolsa.sound_track.entity.JukeboxMinecartEntity;

public class JukeboxMinecartType implements SoundTrackMinecartType, MinecartComparatorLogic<JukeboxMinecartEntity>  {

	@Override
	public AbstractMinecartEntity createMinecartEntity(World world, double x, double y, double z) {
		return new JukeboxMinecartEntity(world, x, y, z);
	}

    @Override
    public int getComparatorValue(JukeboxMinecartEntity minecart, BlockState state, BlockPos pos) {
        if(minecart.getRecord().getItem() instanceof MusicDiscItem)
            return ((MusicDiscItem)minecart.getRecord().getItem()).getComparatorOutput();
        else if(SoundTrack.phonos.isLoaded()
                && SoundTrack.phonos.isCustomMusicDisc(minecart.getRecord().getItem())) {
            return minecart.getRecord().getOrCreateSubNbt("MusicData").getInt("ComparatorSignal");
        }
        else
            return 0;
    }
}
