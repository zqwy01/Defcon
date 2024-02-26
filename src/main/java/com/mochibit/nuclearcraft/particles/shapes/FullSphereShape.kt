package com.mochibit.nuclearcraft.particles.shapes

import com.mochibit.nuclearcraft.math.Vector3
import org.bukkit.Particle

class FullSphereShape(private val particle: Particle, private val radiusY: Double, private val radiusXZ: Double): ParticleShape(particle){

    override fun build(): HashSet<Vector3> {
        val result = HashSet<Vector3>();

        for (x in -radiusXZ.toInt()..radiusXZ.toInt()) {
            for (y in -radiusY.toInt()..radiusY.toInt()) {
                for (z in -radiusXZ.toInt()..radiusXZ.toInt()) {
                    if ((x * x) / (radiusXZ * radiusXZ) + (y * y) / (radiusY * radiusY) + (z * z) / (radiusXZ * radiusXZ) <= 1)
                        result.add(Vector3(x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        return result;
    }



}