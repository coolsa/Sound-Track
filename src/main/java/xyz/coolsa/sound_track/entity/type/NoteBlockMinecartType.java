package xyz.coolsa.sound_track.entity.type;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import xyz.coolsa.sound_track.entity.NoteBlockMinecartEntity;

public class NoteBlockMinecartType implements SoundTrackMinecartType {

	@Override
	public AbstractMinecartEntity createMinecartEntity(World world, double x, double y, double z) {
		return new NoteBlockMinecartEntity(world, x, y, z);
	}

}
