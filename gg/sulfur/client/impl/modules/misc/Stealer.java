package gg.sulfur.client.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.inventory.ItemUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;

public class Stealer extends Module {

    private final NumberValue speed = new NumberValue("Speed", this, 100, 1, 250);
    private final BooleanValue chestsOnly = new BooleanValue("'Chests' Only", this, true);
    private BooleanValue silent = new BooleanValue("Silent", this, false);
    public Stopwatch timer = new Stopwatch();

    public Stealer(ModuleData moduleData) {
        super(moduleData);
        register(speed, chestsOnly, silent);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen instanceof GuiChest && event.isPre()) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            if (chestsOnly.getValue()) {
                if (chest.lowerChestInventory.getDisplayName().getUnformattedText().contains("Chest") || chest.lowerChestInventory.getDisplayName().getUnformattedText().equalsIgnoreCase("LOW")) {
                    if (isChestEmpty(chest) || isInventoryFull()) {
                        mc.thePlayer.closeScreen();
                        return;
                    }
                    for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                        ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                        if (stack != null && timer.timeElapsed(speed.getValue().longValue())) {
                            if (!ItemUtil.isTrash(stack)) {
                                mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                                timer.resetTime();
                                break;
                            }
                        }
                    }
                }

            } else {
                if (isChestEmpty(chest) || isInventoryFull()) {
                    mc.thePlayer.closeScreen();
                    return;
                }
                for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                    ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                    if (stack != null && timer.timeElapsed(speed.getValue().longValue())) {
                        if (!ItemUtil.isTrash(stack)) {
                            mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                            timer.resetTime();
                            break;
                        }
                    }
                }
            }

        }
    }


    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
            ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
            if (stack != null)
                if (!ItemUtil.isTrash(stack))
                    return false;
        }
        return true;
    }


    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}
