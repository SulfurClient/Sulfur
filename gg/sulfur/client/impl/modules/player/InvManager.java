package gg.sulfur.client.impl.modules.player;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.combat.Aura;
import gg.sulfur.client.impl.utils.Action;
import gg.sulfur.client.impl.utils.invmanager.ContainerUtil;
import gg.sulfur.client.impl.utils.invmanager.ItemUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class InvManager extends Module {
    private BooleanValue spoof = new BooleanValue("Spoof Open", this, false);
    private BooleanValue onlyInvProperty = new BooleanValue("Only In Inventory", this, true);

    private BooleanValue clean = new BooleanValue("Clean Inventory", this, true);
    private BooleanValue equip = new BooleanValue("Equip Armor", this, true);
    private BooleanValue sword = new BooleanValue("Auto Sword", this, true);

    private NumberValue dropDelayProperty = new NumberValue("Cleaning Delay", this, 5, 0, 300);

    private NumberValue equipDelayProperty = new NumberValue("Equip Delay", this, 150, 0, 350);

    private NumberValue autoSwordSlotProperty = new NumberValue("Sword Slot", this, 1, 1, 9);

    private Stopwatch dropStopwatch = new Stopwatch();
    private Stopwatch equipStopwatch = new Stopwatch();
    public boolean cleaning;
    public boolean equipping;
    public boolean swappingSword;
    private boolean guiOpenedByMod;
    private final ArrayList<Action> clickQueue;
    private boolean openedinv = false;
    ;

    public InvManager(ModuleData data) {
        super(data);
        register(spoof, onlyInvProperty, clean, equip, sword, dropDelayProperty, equipDelayProperty, autoSwordSlotProperty);
        clickQueue = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        cleaning = false;
        equipping = false;
        guiOpenedByMod = false;
        clickQueue.clear();
    }

    @Subscribe
    public void onSendPacket(PacketEvent event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow) {
            openedinv = false;
        }
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction packet = event.getPacket();
            if (packet.func_180764_b() == packet.func_180764_b().OPEN_INVENTORY) {
                openedinv = true;
            }
        }
    }

    @Subscribe
    public void onPlayerUpdate(UpdateEvent playerUpdateEvent) {
        if (Sulfur.getInstance().getModuleManager().get(Aura.class).isToggled()) {
            return;
        }
        if (playerUpdateEvent.isPre() && !mc.thePlayer.isUsingItem()) {

            if (onlyInvProperty.getValue() && !(mc.currentScreen instanceof GuiInventory) && mc.thePlayer.isMoving())
                return;

            if (!clickQueue.isEmpty()) {
                clickQueue.get(0).execute();
                clickQueue.remove(clickQueue.get(0));
            } else {
                if (!switchSwordSlot()) {
                    swappingSword = false;
                    if (!equipArmor()) {
                        equipping = false;
                        if (!clean()) {
                            cleaning = false;
                        }
                    }
                }
            }

            if (guiOpenedByMod && !cleaning && !equipping) {
                mc.displayGuiScreen(null);
                guiOpenedByMod = false;
                for (KeyBinding keyBinding : new KeyBinding[]{
                        mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                        mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                        mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint}) {
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), Keyboard.isKeyDown(keyBinding.getKeyCode()));
                }
            }
        }
    }

    private boolean switchSwordSlot() {
        if (sword.getValue()) {
            if (onlyInvProperty.getValue() && !(mc.currentScreen instanceof GuiInventory)) {
                return false;
            }
            for (int i = 9; i < 45; i++) {
                if (i == 35 + autoSwordSlotProperty.getValue().intValue())
                    continue;

                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemSword))
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (!mc.thePlayer.inventoryContainer.getSlot(35 + autoSwordSlotProperty.getValue().intValue()).getHasStack()) {
                    int finalI1 = i;
                    if (!openedinv) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                    }

                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));
                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35 + autoSwordSlotProperty.getValue().intValue(), 0, 0, mc.thePlayer));

                    if (openedinv) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));
                    }
                    return true;
                } else {
                    ItemStack stackInWantedSlot = mc.thePlayer.inventoryContainer.getSlot(35 + autoSwordSlotProperty.getValue().intValue()).getStack();
                    if (ItemUtil.compareDamage(stackInSlot, stackInWantedSlot) == stackInSlot) {
                        int finalI = i;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));

                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35 + autoSwordSlotProperty.getValue().intValue(), 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean equipArmor() {
        if (equip.getValue()) {
            if (onlyInvProperty.getValue() && !(mc.currentScreen instanceof GuiInventory)) {
                return false;
            }

            for (int i = 9; i < 45; i++) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (!(stackInSlot.getItem() instanceof ItemArmor))
                    continue;

                if (ContainerUtil.getArmorItemsEquipSlot(stackInSlot, false) == -1)
                    continue;

                if (mc.thePlayer.getEquipmentInSlot(ContainerUtil.getArmorItemsEquipSlot(stackInSlot, true)) == null) {
                    System.out.println("No stack in slot : " + stackInSlot.getUnlocalizedName());
                    if (equipStopwatch.timeElapsed(equipDelayProperty.getValue().intValue())) {
                        int finalI = i;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));


                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, ContainerUtil.getArmorItemsEquipSlot(stackInSlot, false), 0, 0, mc.thePlayer));

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                        return true;
                    }
                } else {
                    ItemStack stackInEquipmentSlot = mc.thePlayer.getEquipmentInSlot(ContainerUtil.getArmorItemsEquipSlot(stackInSlot, true));
                    if (ItemUtil.compareProtection(stackInSlot, stackInEquipmentSlot) == stackInSlot) {
                        System.out.println("Stack in slot : " + stackInSlot.getUnlocalizedName());
                        if (equipStopwatch.timeElapsed(equipDelayProperty.getValue().intValue())) {
                            int finalI1 = i;
                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));


                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, ContainerUtil.getArmorItemsEquipSlot(stackInSlot, false), 0, 0, mc.thePlayer));
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));

                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean clean() {
        if (clean.getValue()) {
            if (onlyInvProperty.getValue() && !(mc.currentScreen instanceof GuiInventory)) {
                return false;
            }

            if (mc.thePlayer == null)
                return false;

            ArrayList<Integer> uselessItem = new ArrayList<>();
            for (int i = 0; i < 45; i++) {

                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (mc.thePlayer.inventory.armorItemInSlot(0) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(1) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(2) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(3) == stackInSlot)
                    continue;

                if (isGarbo(i))
                    uselessItem.add(i);

            }

            if (uselessItem.size() > 0) {
                cleaning = true;
                if (dropStopwatch.timeElapsed(dropDelayProperty.getValue().intValue())) {
                    if (!(mc.thePlayer.inventory.currentItem == uselessItem.get(0))) {

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));

                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, uselessItem.get(0), 1, 4, mc.thePlayer);

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                    }
                    uselessItem.remove(0);
                    dropStopwatch.resetTime();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isGarbo(int slot) {
        ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
        if (stackInSlot.getItem() instanceof ItemSword) {
            for (int i = 0; i < 44; i++) {
                if (i == slot) continue;
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (stackAtIndex.getItem() instanceof ItemSword) {
                        if (ItemUtil.compareDamage(stackInSlot, stackAtIndex) == stackAtIndex) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        if (stackInSlot.getItem() instanceof ItemAxe || stackInSlot.getItem() instanceof ItemBow || stackInSlot.getItem() instanceof ItemFishingRod || stackInSlot.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackInSlot.getItem()) == 346) {
            for (int i = 44; i > 0; i--) {
                if (i == slot) continue;
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if ((stackAtIndex.getItem() instanceof ItemSword || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemBow || stackAtIndex.getItem() instanceof ItemFishingRod || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackAtIndex.getItem()) == 346)) {
                        if (Item.getIdFromItem(stackAtIndex.getItem()) == Item.getIdFromItem(stackInSlot.getItem())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        if(stackInSlot.hasDisplayName())
            return false;

        if (isBestArmorItem(stackInSlot))
            return false;

        if (stackInSlot.getItem() instanceof ItemFood)
            return false;

        if (stackInSlot.getItem() instanceof ItemBlock)
            return false;

        if(stackInSlot.getItem() instanceof ItemPotion) {
            return ItemUtil.isPotionNegative(stackInSlot);
        }

        if(stackInSlot.getItem() instanceof ItemTool) {
            return true;
        }

        if (Item.getIdFromItem(stackInSlot.getItem()) == 367)
            return true; // rotten flesh
        if (Item.getIdFromItem(stackInSlot.getItem()) == 259)
            return true; // flint & steel
        if (Item.getIdFromItem(stackInSlot.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 264)
            return true; // diamond
        if (Item.getIdFromItem(stackInSlot.getItem()) == 265)
            return true; // iron
        if (Item.getIdFromItem(stackInSlot.getItem()) == 336)
            return true; // brick
        if (Item.getIdFromItem(stackInSlot.getItem()) == 266)
            return true; // gold ingot
        if (Item.getIdFromItem(stackInSlot.getItem()) == 345)
            return true; // compass
        if (Item.getIdFromItem(stackInSlot.getItem()) == 46)
            return true; // tnt
        if (Item.getIdFromItem(stackInSlot.getItem()) == 261)
            return true; // bow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 116)
            return true; // enchanting table
        if (Item.getIdFromItem(stackInSlot.getItem()) == 54)
            return true;

        return true;
    }

    private boolean isBestTool(ItemStack itemStack) {
        return false;
    }

    private boolean isBestArmorItem(ItemStack armorStack) {
        if (armorStack.getItem() instanceof ItemArmor) {
            int equipSlot = ContainerUtil.getArmorItemsEquipSlot(armorStack, true);

            if (equipSlot == -1)
                return false;

            if (mc.thePlayer.getEquipmentInSlot(equipSlot) == null) {
                for (int slotNum = 44; slotNum > 0; slotNum--) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(slotNum).getHasStack())
                        continue;

                    ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(slotNum).getStack();

                    if (!(stackInSlot.getItem() instanceof ItemArmor))
                        continue;

                    if (ContainerUtil.getArmorItemsEquipSlot(stackInSlot, true) == equipSlot
                            && compareArmorItems(armorStack, stackInSlot) == stackInSlot)
                        return false;
                }
            } else {
                if (compareArmorItems(armorStack, mc.thePlayer.getEquipmentInSlot(equipSlot)) == mc.thePlayer.getEquipmentInSlot(equipSlot))
                    return false;
            }

            return true;
        }
        return false;
    }

    private ItemStack compareArmorItems(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null)
            return null;

        if (!(item1.getItem() instanceof ItemArmor && item2.getItem() instanceof ItemArmor))
            return null;

        if (ContainerUtil.getArmorItemsEquipSlot(item1, true) != ContainerUtil.getArmorItemsEquipSlot(item2, true))
            return null;

        double item1Protection = ItemUtil.getArmorProtection(item1);
        double item2Protection = ItemUtil.getArmorProtection(item2);

        if (item1Protection != item2Protection) {
            if (item1Protection > item2Protection)
                return item1;
            else
                return item2;
        } else {
            if (item1.getMaxDamage() > item2.getMaxDamage())
                return item2;
            else
                return item1;
        }
    }

}