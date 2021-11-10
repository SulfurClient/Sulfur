package gg.sulfur.client.impl.events

import gg.sulfur.client.api.event.Event
import net.minecraft.client.model.ModelPlayer
import net.minecraft.entity.player.EntityPlayer

/**
 * @author Kansio
 * @created 4:22 AM
 * @project Client
 */

class UpdateModelEvent(val player: EntityPlayer, val model: ModelPlayer) : Event()