package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.PacketEvent
import net.minecraft.network.play.server.S02PacketChat
import gg.sulfur.client.impl.utils.math.Mafs
import gg.sulfur.client.impl.utils.networking.PacketUtil
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C01PacketChatMessage
import java.util.*
import java.util.function.Consumer

class KillSults(moduleData: ModuleData?) : Module(moduleData) {
    private val namePlayer = BooleanValue("Name Player", this, true)

    private val killMessages = Arrays.asList(
        "foi jogado no void por",
        "morreu para",
        "slain by",
        "was killed by"
    )

    //credit to dort
    val insults = Arrays.asList(
        "Sulfur on Top",
        "here's your tickets to the juice wrld concert ",
        "i bet you probably shop at Costco ",
        "do you buy your groceries at the dollar store?",
        "i guess u forgot to get sulfur... ",
        "what do your clothes have in common with your skills? they're both straight out of a dumpster ",
        "i don't cheat, you just need to click faster",
        "cry all you want, that monkey George Floyd died of a fentanyl overdose",
        "i speak english not your gibberish",
        "i understand why your parents abused you",
        "i'd tell you to uninstall, but your aim is so bad you'd miss and click on your cuck porn instead",
        "im not saying you're worthless, but i'd unplug ur life support to charge my phone ",
        "need some pvp advice? ",
        "how are you so bad? just practice your aim and hold w ",
        "you do be lookin' kinda bad at the game doh :flushed: ",
        "you look like you were drawn with my left hand",
        "you pressed the wrong button when you installed Minecraft ",
        "you should look into buying a client",
        "you're so white that you don't play on vanilla, you play on clear ",
        "your difficulty settings must be stuck on easy",
        "drown in your own salt",
        "even your mom is better than you in this game ",
        "go back to fortnite you degenerate",
        "go commit stop breathing plz",
        "go play roblox you worthless fucking degenerate ",
        "go take a long walk off a short bridge",
        "i swear on jhalt, you got shit on harder than archy ",
        "if the body is 70% water then how is your body 100% salt? ",
        "lol you probably speak dog eater",
        "mans probably got an error on his hello world program lmao",
        "no top hat, you're more trash than my garbage can ",
        "plz no repotr i no want ban plesae! ",
        "report me im really scared",
        "seriously? go back to fortnite monkey brain ",
        "Ladies and Gentleman: The runner-up to the participation award! ",
        "some kids were dropped at birth, but you got thrown at the wall ",
        "you really like taking L's",
        "damn, you're taking L's fatter than the nigger cock in your BBC cuck porn ",
        "you're the type of guy to quickdrop irl ",
        "i bet you thought gcheat was a type of STD",
        "you're the type to get 3rd place in a 1v1 ",
        "you have an IQ lower than that of a bathtub ",
        "your parents abandoned you, then the orphanage did the same ",
        "you go to the doctors and they say you shrunk ",
        "memeclient, drop kicking lil' kids and fat obese staff since 2017 ",
        "who would win; an anticheat with a $400,000 per year budget or one packet?",
        "is watchdog watching a dog or a dog watching a watch? ",
        "yo mama so fat, she sat on an iphone and it became an ipad",
        "on black friday, black people die ",
        "search up blue waffle on google, it's so cute ",
        "this anticheat is disabled as you, fucking vegetable",
        "you smell like a moldy ballsack ",
        "your grandmother has chlamydia",
        "your aim is like a toddler with parkinson's trying to aim a water gun ",
        "welcome to my rape dungeon! population: you ",
        "i'd insult you after that death but by merely existing you do all the work for me ",
        "yo whens the documentary crew coming to your house to film the next episode of my 600 pound life? ",
        "you are the type of person to think FOV increases reach ",
        "you're so gay you spent twice as much on a coloured iPhone just to join the 41% a day later ",
        "your cumulative intelligence is that of a rock",
        "you're the type of guy to buy vape v4 and cry when you get auto-banned",
        "you shouldn't be running away with all these monkeys coming after you ",
        "yes, record me, send the footage straight to child lover tenebrous",
        "your killaura was coded in scratch with help from zhn ",
        "you deserved to be bhopped on ",
        "your birth certificate was an apology letter from the condom factory",
        "always remember you're unique - just like everyone else ",
        "how do you keep an idiot amused? watch this message until it fades away ",
        "if practice makes perfect, and nobody's perfect, why practice?",
        "if i could rearrange the alphabet, i'd put U and I as far away as possible",
        "i'd smack you, but that would be animal abuse ",
        "if i wanted to kill myself, i'd climb to your ego and jump to your IQ ",
        "man's so ugly he made his happy meal cry",
        "your face makes onions cry",
        "you are like a cloud, when you disappear it's a beautiful day ",
        "you bring everyone so much joy! you know, when you leave the room. but, still ",
        "you are missing a brain ",
        "are you a primate?",
        "you're so ugly your portraits hang themselves ",
        "your brain is so smooth even a 3090 can't simulate the reflectiveness ",
        "shouldn't you have a license for being that ugly? ",
        "the village called, they want their idiot back",
        "you're like a light switch, even a little kid can turn you on ",
        "beauty is skin deep, but ugliness is to the bone",
        "sorry i can't think of an insult stupid enough for you",
        "if i could be one person for a day, it sure as hell wouldn't be you ",
        "earth is full. go home",
        "roses are red violets are blue, god made me pretty, what the hell happened to you?",
        "you're so black you scared off the mexican drug cartel",
        "i called your boyfriend gay and he hit me with his purse! ",
        "just because your head is shaped like a light bulb doesn't mean you have good ideas ",
        "you're the type of person to join a vending machine reward club ",
        "i've seen gay parades straighter than u",
        "you're like fdp client, chinese trash.",
        "you remind me of a fdp client user."
    )

    @Subscribe
    fun onChatReceive(event: PacketEvent) {
        if (event.getPacket<Packet>() is S02PacketChat) {
            val msg = (event.getPacket<Packet>() as S02PacketChat).chatComponent.unformattedText
            val split = msg.split(" ".toRegex()).toTypedArray()
            killMessages.forEach(Consumer { killsultTrigger: String? ->
                if (msg.contains(killsultTrigger!!) && msg.contains(mc.thePlayer.name) && !split[0].equals(mc.thePlayer.name, ignoreCase = true)) {
                    val insult = insults[Mafs.getRandomInRange(0, insults.size - 1)]
                    PacketUtil.sendPacketNoEvent(C01PacketChatMessage(Arrays.asList(*split)[0].toString() + ", " + insult))
                }
            })
        }
    }
}