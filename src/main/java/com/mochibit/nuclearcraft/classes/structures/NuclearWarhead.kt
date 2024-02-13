package com.mochibit.nuclearcraft.classes.structures

import com.mochibit.nuclearcraft.NuclearCraft
import com.mochibit.nuclearcraft.explosives.NuclearComponent
import com.mochibit.nuclearcraft.explosives.Shockwave
import com.mochibit.nuclearcraft.explosives.ShockwaveColumn
import com.mochibit.nuclearcraft.interfaces.ExplodingStructure
import com.mochibit.nuclearcraft.threading.jobs.SimpleCompositionJob
import com.mochibit.nuclearcraft.threading.tasks.ScheduledRunnable
import com.mochibit.nuclearcraft.utils.Geometry
import it.unimi.dsi.fastutil.objects.ObjectSets
import it.unimi.dsi.fastutil.objects.ObjectSets.SynchronizedSet
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashSet
import kotlin.math.ceil


class NuclearWarhead : AbstractStructureDefinition(), ExplodingStructure {
    override fun explode(center: Location, nuclearComponent: NuclearComponent) {
        /* The explosion is subdivided into some steps
        *
        *  The first one, assuming the bomb is a nuclear bomb, create a sphere of air blocks ( calculated with the explosive component ),
        *  since nuclear bombs obliterate everything in their radius
        *
        *  The second step is to give fire damage to all entities in the radius of the thermal radiation
        *
        *  The third step is to create an expanding shockwave, which will expand and move over structures, exploding them, and changing the terrain
        *  with some blocks that look like burnt or destroyed blocks.
        *
        *  The fourth step is to modify the biomes of the area, since the explosion will change the terrain and the environment
        *
        *  When all of this is happening, there will be a sound effect, and a particle effect, to simulate the explosion
        */

        // Create a sphere of air blocks
        val obliterationRadius = nuclearComponent.blastPower * 30;
        for (x in -obliterationRadius.toInt()..obliterationRadius.toInt()) {
            for (y in -obliterationRadius.toInt()..obliterationRadius.toInt()) {
                for (z in -obliterationRadius.toInt()..obliterationRadius.toInt()) {
                    val distance = (x * x + y * y + z * z);
                    if (distance <= obliterationRadius * obliterationRadius) {
                        val block = center.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block;
                        block.type = Material.AIR;
                    }
                }
            }
        }

        // Give fire damage to all entities in the radius of the thermal radiation (unless they are protected)
        // We will use ray-casting to check if the entity is in the radius of the thermal radiation
        val thermalRadius = nuclearComponent.thermalPower * 5;
        // TODO: Implement thermal radiation


        val shockwaveRadius = nuclearComponent.blastPower * 30 * 5;
        val shockwaveHeight = nuclearComponent.blastPower * 100 * 2;

        NuclearCraft.Companion.Logger.info("Shockwave radius: $shockwaveRadius, Shockwave height: $shockwaveHeight");


        NuclearCraft.instance.scheduledRunnable.addWorkload(SimpleCompositionJob(shockwaveRadius) {
            Shockwave(center, shockwaveRadius.toDouble(), shockwaveHeight.toDouble()).explode();
        });


    }

}
