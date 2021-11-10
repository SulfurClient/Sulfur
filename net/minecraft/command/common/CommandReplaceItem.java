package net.minecraft.command.common;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class CommandReplaceItem extends CommandBase {
    private static final Map field_175785_a = Maps.newHashMap();
    // private static final String __OBFID = "CL_00002340";

    public String getCommandName() {
        return "replaceitem";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.replaceitem.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage");
        } else {
            boolean var3;

            if (args[0].equals("entity")) {
                var3 = false;
            } else {
                if (!args[0].equals("block")) {
                    throw new WrongUsageException("commands.replaceitem.usage");
                }

                var3 = true;
            }

            byte var4;

            if (var3) {
                if (args.length < 6) {
                    throw new WrongUsageException("commands.replaceitem.block.usage");
                }

                var4 = 4;
            } else {
                if (args.length < 4) {
                    throw new WrongUsageException("commands.replaceitem.entity.usage");
                }

                var4 = 2;
            }

            int var16 = var4 + 1;
            int var5 = this.func_175783_e(args[var4]);
            Item var6;

            try {
                var6 = getItemByText(args[var16]);
            } catch (NumberInvalidException var15) {
                if (Block.getBlockFromName(args[var16]) != Blocks.air) {
                    throw var15;
                }

                var6 = null;
            }

            ++var16;
            int var7 = args.length > var16 ? parseInt(args[var16++], 1, 64) : 1;
            int var8 = args.length > var16 ? parseInt(args[var16++]) : 0;
            ItemStack var9 = new ItemStack(var6, var7, var8);

            if (args.length > var16) {
                String var10 = getChatComponentFromNthArg(sender, args, var16).getUnformattedText();

                try {
                    var9.setTagCompound(JsonToNBT.func_180713_a(var10));
                } catch (NBTException var14) {
                    throw new CommandException("commands.replaceitem.tagError", var14.getMessage());
                }
            }

            if (var9.getItem() == null) {
                var9 = null;
            }

            if (var3) {
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                BlockPos var17 = func_175757_a(sender, args, 1, false);
                World var11 = sender.getEntityWorld();
                TileEntity var12 = var11.getTileEntity(var17);

                if (!(var12 instanceof IInventory)) {
                    throw new CommandException("commands.replaceitem.noContainer", var17.getX(), var17.getY(), var17.getZ());
                }

                IInventory var13 = (IInventory) var12;

                if (var5 >= 0 && var5 < var13.getSizeInventory()) {
                    var13.setInventorySlotContents(var5, var9);
                }
            } else {
                Entity var18 = func_175768_b(sender, args[1]);
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);

                if (var18 instanceof EntityPlayer) {
                    ((EntityPlayer) var18).inventoryContainer.detectAndSendChanges();
                }

                if (!var18.func_174820_d(var5, var9)) {
                    throw new CommandException("commands.replaceitem.failed", var5, var7, var9 == null ? "Air" : var9.getChatComponent());
                }

                if (var18 instanceof EntityPlayer) {
                    ((EntityPlayer) var18).inventoryContainer.detectAndSendChanges();
                }
            }

            sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var7);
            notifyOperators(sender, this, "commands.replaceitem.success", var5, var7, var9 == null ? "Air" : var9.getChatComponent());
        }
    }

    private int func_175783_e(String p_175783_1_) throws CommandException {
        if (!field_175785_a.containsKey(p_175783_1_)) {
            throw new CommandException("commands.generic.parameter.invalid", p_175783_1_);
        } else {
            return (Integer) field_175785_a.get(p_175783_1_);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "entity", "block") : (args.length == 2 && args[0].equals("entity") ? getListOfStringsMatchingLastWord(args, this.func_175784_d()) : ((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block")) ? ((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block")) ? null : func_175762_a(args, Item.itemRegistry.getKeys())) : func_175762_a(args, field_175785_a.keySet())));
    }

    protected String[] func_175784_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }

    static {
        int var0;

        for (var0 = 0; var0 < 54; ++var0) {
            field_175785_a.put("slot.container." + var0, var0);
        }

        for (var0 = 0; var0 < 9; ++var0) {
            field_175785_a.put("slot.hotbar." + var0, var0);
        }

        for (var0 = 0; var0 < 27; ++var0) {
            field_175785_a.put("slot.inventory." + var0, 9 + var0);
        }

        for (var0 = 0; var0 < 27; ++var0) {
            field_175785_a.put("slot.enderchest." + var0, 200 + var0);
        }

        for (var0 = 0; var0 < 8; ++var0) {
            field_175785_a.put("slot.villager." + var0, 300 + var0);
        }

        for (var0 = 0; var0 < 15; ++var0) {
            field_175785_a.put("slot.horse." + var0, 500 + var0);
        }

        field_175785_a.put("slot.weapon", 99);
        field_175785_a.put("slot.armor.head", 103);
        field_175785_a.put("slot.armor.chest", 102);
        field_175785_a.put("slot.armor.legs", 101);
        field_175785_a.put("slot.armor.feet", 100);
        field_175785_a.put("slot.horse.saddle", 400);
        field_175785_a.put("slot.horse.armor", 401);
        field_175785_a.put("slot.horse.chest", 499);
    }
}
