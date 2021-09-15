package xyz.coolsa.sound_track.entity.type;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;

public interface SoundTrackMinecartType {
	public abstract AbstractMinecartEntity createMinecartEntity(World world, double x, double y, double z);
}
