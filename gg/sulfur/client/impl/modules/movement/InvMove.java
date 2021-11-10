package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.impl.events.PacketEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

/**
 * @author Auth
 */

public class InvMove extends Module {

    public BooleanValue lookInInv = new BooleanValue("Move Head", this, true);

    private final KeyBinding[] keyBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindJump,
            mc.gameSettings.keyBindSprint
    };

    public InvMove(ModuleData moduleData) {
        super(moduleData);
        register(lookInInv);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow && (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat)) {
            event.setCancelled(true);
        }
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat) return;

        for (KeyBinding keyBinding : keyBindings) {
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
    }
}
