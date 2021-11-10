package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.impl.events.PacketEvent
import net.minecraft.network.play.server.S02PacketChat
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.impl.notification.type.NotificationType
import net.minecraft.network.Packet

class AutoRegister(moduleData: ModuleData?) : Module(moduleData) {

    @Subscribe
    fun onPacket(event: PacketEvent) {
        if (mc.thePlayer == null) return
        if (event.getPacket<Packet>() is S02PacketChat) {
            val packet = event.getPacket<S02PacketChat>()
            val msg = packet.chatComponent.formattedText
            if (msg.contains("Please register your account using")) {
                val fixedmsg = msg.split(" ".toRegex()).toTypedArray()
                Sulfur.getInstance().notificationManager.postNotification(
                    "Registered automatically with password 'Sulfur'",
                    NotificationType.INFO
                )
                println(
                    "captcha should be " + fixedmsg[9].replace("§7§l".toRegex(), "").replace("§c'.".toRegex(), "")
                        .replace("§r".toRegex(), "")
                )
                mc.thePlayer.sendChatMessage(
                    "/register Sulfur Sulfur " + fixedmsg[9].replace("§7§l".toRegex(), "").replace("§c'.".toRegex(), "")
                        .replace("§r".toRegex(), "")
                )
            }
            if (msg.contains("/register") && !msg.contains("Please use")) {
                Sulfur.getInstance().notificationManager.postNotification(
                    "Registered automatically with password 'Sulfur123'",
                    NotificationType.INFO
                )
                mc.thePlayer.sendChatMessage("/register Sulfur123 Sulfur123")
            }
            if (msg.contains("/login") && !msg.contains("/register")) {
                Sulfur.getInstance().notificationManager.postNotification(
                    "Logged in automatically with password 'Sulfur123'",
                    NotificationType.INFO
                )
                mc.thePlayer.sendChatMessage("/login Sulfur123")
            }
            if (msg.contains("This player is already registered.")) {
                Sulfur.getInstance().notificationManager.postNotification(
                    "Logged in automatically with password 'Sulfur'",
                    NotificationType.INFO
                )
                mc.thePlayer.sendChatMessage("/login Sulfur Sulfur")
            }
            if (msg.contains("Please use")) {
                //hazel why you gotta make it so hard to bypass ur register shit :(
                val fixedmsg = msg.split(" ".toRegex()).toTypedArray()
                Sulfur.getInstance().notificationManager.postNotification(
                    "Registered automatically with password 'Sulfur'",
                    NotificationType.INFO
                )
                mc.thePlayer.sendChatMessage(
                    "/register Sulfur Sulfur " + fixedmsg[6].replace("§r".toRegex(), "").replace("§a§o".toRegex(), "")
                )
                println(
                    "/register Sulfur Sulfur " + fixedmsg[6].replace("§r".toRegex(), "").replace("§a§o".toRegex(), "")
                )
            }
            if (msg.contains("The first person to type")) {
                val fixedmsg = msg.split(" ".toRegex()).toTypedArray()
                Sulfur.getInstance().notificationManager.postNotification(
                    "Automatically did the challenge",
                    NotificationType.INFO
                )
                mc.thePlayer.sendChatMessage(fixedmsg[6])
            }
            if (msg.contains("Connected to HCF!")) {
                Sulfur.getInstance().notificationManager.postNotification(
                    "Automatically enabled pvp timer",
                    NotificationType.INFO
                )
                //Client.INSTANCE.getNotificationManager().postNotification("Coords for a vault X: 227 Z: 1990", NotificationType.INFO);
                mc.thePlayer.sendChatMessage("/pvp enable")
            }
        }
    }
}