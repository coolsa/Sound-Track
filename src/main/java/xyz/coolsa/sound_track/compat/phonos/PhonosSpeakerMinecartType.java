package xyz.coolsa.sound_track.compat.phonos;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import xyz.coolsa.sound_track.entity.type.SoundTrackMinecartType;

public class PhonosSpeakerMinecartType implements SoundTrackMinecartType {

	@Override
	public AbstractMinecartEntity createMinecartEntity(World world, double x, double y, double z) {
		return new PhonosSpeakerMinecartEntity(world, x, y, z);
	}

}
