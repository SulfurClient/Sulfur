package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.player.Scaffold;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.time.TimerUtil;
import org.lwjgl.input.Mouse;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;

public class AutoApple extends Module {
    public boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff = false;
    private final TimerUtil timer = new TimerUtil();
    private final BooleanValue eatHeads = new BooleanValue("Eat heads",this, true);
    private final BooleanValue eatApples = new BooleanValue("Eat apples",this, true);
    private final NumberValue health = new NumberValue("Health", this, 10, 20, 1, true);
    private final NumberValue delay = new NumberValue("Delay", this, 750, 25, 2000, true);

    public AutoApple(ModuleData moduleData) {
        super(moduleData);
        register(eatHeads, eatApples, health, delay);
    }

    @Override
    public void onEnable() {
        eatingApple = doingStuff = false;
        switched = -1;
        timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        doingStuff = false;
        if (eatingApple) {
            repairItemPress();
            repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        if (mc.gameSettings != null) {
            final KeyBinding keyBindUseItem = mc.gameSettings.keyBindUseItem;
            if (keyBindUseItem != null) keyBindUseItem.pressed = false;
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (Sulfur.getInstance().getModuleManager().get(Scaffold.class).isToggled()) return;
        if (Sulfur.getInstance().getModuleManager().get(Aura.class).isToggled()) return;
        if (mc.thePlayer == null) return;
        final InventoryPlayer inventory = mc.thePlayer.inventory;
        if (inventory == null) return;
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            final KeyBinding useItem = mc.gameSettings.keyBindUseItem;

            if (!timer.reach(delay.getCastedValue().longValue())) {
                eatingApple = false;
                repairItemPress();
                repairItemSwitch();
                return;
            }

            if (mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.isPotionActive(Potion.regeneration) ||mc.thePlayer.getHealth() >= health.getValue()) {
                timer.reset();
                if (eatingApple) {
                    eatingApple = false;
                    repairItemPress();
                    repairItemSwitch();
                }
                return;
            }

            for (int i = 0; i < 2; i++) {
                final boolean doEatHeads = i != 0;

                if (doEatHeads) {
                    if (!eatHeads.getValue()) continue;
                } else {
                    if (!eatApples.getValue()) {
                        eatingApple = false;
                        repairItemPress();
                        repairItemSwitch();
                        continue;
                    }
                }

                int slot;

                if (doEatHeads) {
                    slot = getItemFromHotbar(397);
                } else {
                    slot = getItemFromHotbar(322);
                }

                if (slot == -1) continue;

                final int tempSlot = inventory.currentItem;

                doingStuff = true;
                if (doEatHeads) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    timer.reset();
                } else {
                    inventory.currentItem = slot;
                    useItem.pressed = true;
                    if (eatingApple) continue; // no message spam
                    eatingApple = true;
                    switched = tempSlot;
                }
                ChatUtil.displayMessage(String.format("Automatically ate a %s", doEatHeads ? "player head" : "golden apple"));
            }
        }
    }

    private void repairItemSwitch() {
        final EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;
        final InventoryPlayer inventory = p.inventory;
        if (inventory == null) return;
        int switched = this.switched;
        if (switched == -1) return;
        inventory.currentItem = switched;
        switched = -1;
        this.switched = switched;
    }

    private int getItemFromHotbar(final int id) {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == id) {
                    return i;
                }
            }
        }
        return -1;
    }

}
