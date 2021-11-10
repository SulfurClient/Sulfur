package gg.sulfur.client.impl.commands

import gg.sulfur.client.api.command.Command
import gg.sulfur.client.api.command.CommandData
import gg.sulfur.client.impl.utils.player.ChatUtil

class SpamMessageCommand : Command(CommandData("spammer", "spam", "spammessage", "spamr", "spamm")) {

    override fun run(command: String?, vararg args: String?) {
        var message: String? = ""
        for (smg in args) {
            if (smg === args[0]) {
                continue
            }
            message += smg
        }
        ChatUtil.displayMessage("$message ")
    }

}