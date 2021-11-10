package gg.sulfur.client.impl.modules.player;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.StringValue;
import gg.sulfur.client.impl.events.KeyboardEvent;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.render.Hud;
import gg.sulfur.client.impl.modules.render.Rotations;
import gg.sulfur.client.impl.utils.combat.RotationsUtil;
import gg.sulfur.client.impl.utils.math.MathUtil;
import gg.sulfur.client.impl.utils.math.Vec2f;
import gg.sulfur.client.impl.utils.pathfinding.Vec3d;
import gg.sulfur.client.impl.utils.pathfinding.Vec3f;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.awt.*;

public class Scaffold extends Module {

    private BooleanValue swing = new BooleanValue("Swing", this, true);
    private BooleanValue sprint = new BooleanValue("Sprint", this, false);
    private BooleanValue tower = new BooleanValue("Tower", this, true);
    private BooleanValue amount = new BooleanValue("Show Amount", this, true);
    public BooleanValue safewalk = new BooleanValue("Safewalk", this, true);
    public BooleanValue keepY = new BooleanValue("Keep Y", this, false);

    private StringValue mode = new StringValue("Mode", this, "NCP", "Expand", "Verus");

    private NumberValue delay = new NumberValue("Delay", this, 0, 0, 9000, true);
    private NumberValue expansion = new NumberValue("Expansion", this, 4, 1, 6, true);

    private int animation = 0;

    private final Stopwatch delayTimer = new Stopwatch();
    private final Stopwatch towerTimer = new Stopwatch();

    public Scaffold(ModuleData data) {
        super(data);
        register(mode, swing, sprint, tower, amount, safewalk, delay, expansion, keepY);
    }

    private BlockEntry blockEntry;
    private Scaffold.BlockEntry lastBlockEntry;

    private int startSlot, lastSlot;
    private boolean didPlaceBlock;

    @Override
    public void onEnable() {
        this.delayTimer.resetTime();
        ScaledResolution scaledResolution = RenderUtils.getScaledResolution();

        animation = 0;

        switch (mode.getValue()) {
            case "Verus": {
                blockEntry = null;
                this.didPlaceBlock = false;

                this.startSlot = mc.thePlayer.inventory.currentItem;

                if (getSlotWithBlock() > -1) {
                    mc.thePlayer.inventory.currentItem = getSlotWithBlock();
                }

                lastSlot = getSlotWithBlock();
                break;
            }
            case "NCP": {
                this.startSlot = mc.thePlayer.inventory.currentItem;

                if (getSlotWithBlock() > -1) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getSlotWithBlock()));
                }

                this.lastSlot = getSlotWithBlock();
                break;
            }
            case "Expand": {
                this.startSlot = mc.thePlayer.inventory.currentItem;

                if (getSlotWithBlock() > -1) {
                    mc.thePlayer.inventory.currentItem = getSlotWithBlock();
                }

                lastSlot = getSlotWithBlock();
                break;
            }
        }
    }

    @Override
    public void onDisable() {
        switch (mode.getValue()) {
            case "Verus": {
                break;
            }
            case "NCP": {
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.startSlot));
                lastBlockEntry = null;
                break;
            }
            case "Extend": {
                if (mc.thePlayer.inventory.currentItem != this.startSlot) {
                    mc.thePlayer.inventory.currentItem = this.startSlot;
                }
                break;
            }
        }
    }

    @Subscribe
    public void onRender(RenderHUDEvent event) {
        if (this.amount.getValue()) {
            Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
            ScaledResolution scaledResolution = RenderUtils.getScaledResolution();

            RenderUtils.drawRect(scaledResolution.getScaledWidth() / 2 - 30, scaledResolution.getScaledHeight() / 2 + 50 + animation, 20 + mc.fontRendererObj.getStringWidth(getBlockCount() + "") + 10, 30, new Color(0, 0, 0, 105).getRGB());
            RenderUtils.drawRect(scaledResolution.getScaledWidth() / 2 - 30, scaledResolution.getScaledHeight() / 2 + 50 + animation, 20 + mc.fontRendererObj.getStringWidth(getBlockCount() + "") + 10, 1, hud.color.getValue().getRGB());

            mc.fontRendererObj.drawStringWithShadow(getBlockCount() + "", scaledResolution.getScaledWidth() / 2 - 5, scaledResolution.getScaledHeight() / 2 + 61 + animation, - 1);

            mc.getRenderItem().func_175042_a(mc.thePlayer.inventory.getStackInSlot(getSlotWithBlock()), scaledResolution.getScaledWidth() / 2 - 28, scaledResolution.getScaledHeight() / 2 + 57 + animation);
        }
    }


    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case "Verus": {
                Vec2f vec2f = null;

                if (mc.thePlayer.isMoving()) {
                    mc.thePlayer.setSprinting(sprint.getValue());
                }

                if (this.blockEntry != null) {
                    vec2f = RotationsUtil.getRotations(
                            getPositionByFace(blockEntry.getPosition(),
                                    blockEntry.getFacing()));

                    vec2f.setVecY(90);
                }

                Scaffold.BlockEntry blockEntry = (find(new Vec3(0, 0, 0)));

                if (blockEntry == null) return;

                this.blockEntry = blockEntry;

                if (vec2f != null) {
                    event.setRotationYaw(vec2f.x);
                    event.setRotationPitch(vec2f.y);

                    if (Sulfur.getInstance().getModuleManager().isEnabled(Rotations.class)) {
                        mc.thePlayer.renderPitchHead = vec2f.y;
                        mc.thePlayer.renderYawOffset = vec2f.x;
                        mc.thePlayer.renderYawHead = vec2f.x;
                    }
                }

                int slot = getSlotWithBlock();

                if (getBlockCount() < 1 && this.didPlaceBlock) {
                    mc.thePlayer.motionY -= 20;
                    this.didPlaceBlock = false;
                    return;
                }

                if (this.blockEntry != null && vec2f != null && slot > -1 && event.isPre()) {
                    if (lastSlot != slot) {
                        mc.thePlayer.inventory.currentItem = slot;
                        lastSlot = slot;
                    }

                    if (keepY.getValue()) {
                        if (mc.thePlayer.isMoving() && (mc.gameSettings.keyBindJump.pressed || mc.thePlayer.motionY > 0)) {
                            placeBlockVerus(this.blockEntry.getPosition().add(0, -1, 0), this.blockEntry.getFacing(), slot, swing.getValue());
                        } else {
                            placeBlockVerus(this.blockEntry.getPosition().add(0, 0, 0), this.blockEntry.getFacing(), slot, swing.getValue());
                        }
                    } else {
                        placeBlockVerus(this.blockEntry.getPosition().add(0, 0, 0), this.blockEntry.getFacing(), slot, swing.getValue());
                    }

                    if (tower.getValue() && !mc.thePlayer.isPotionActive(Potion.jump) && !mc.thePlayer.isMoving()
                            && mc.gameSettings.keyBindJump.pressed) {
                        mc.thePlayer.motionY = .52f;
                        mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;

                        if (this.towerTimer.timeElapsed(1500L)) {
                            this.towerTimer.resetTime();
                            //mc.thePlayer.motionY = -1.28f;
                        }
                    }
                }
                break;
            }
            case "Extend": {
                Vec2f vec2f = null;

                if (mc.thePlayer.isMoving()) {
                    mc.thePlayer.setSprinting(sprint.getValue());
                }

                int slot = getSlotWithBlock();
                if (slot > -1 && event.isPre()) {

                    if (lastSlot != slot) {
                        mc.thePlayer.inventory.currentItem = slot;
                        lastSlot = slot;
                    }

                    int expand = expansion.getValue().intValue() * 5;

                    for (int i = 0; i < expand; i++) {
                        Scaffold.BlockEntry blockEntry = findExpand(new Vec3(mc.thePlayer.motionX * i,
                                0, mc.thePlayer.motionZ * i), i);

                        if (blockEntry != null) {

                        }
                    }
                }
                break;
            }
            case "NCP": {
                if (this.lastBlockEntry != null) {
                    Vec2f rotation = RotationsUtil.getRotations(getPositionByFace(this.lastBlockEntry.getPosition(),
                            this.lastBlockEntry.getFacing()));

                    if (mc.thePlayer.isMoving()) {
                        mc.thePlayer.setSprinting(sprint.getValue());
                    }

                    event.setRotationPitch(rotation.y);
                    event.setRotationYaw(rotation.x);

                    if (Sulfur.getInstance().getModuleManager().isEnabled(Rotations.class)) {
                        mc.thePlayer.renderPitchHead = rotation.y;
                        mc.thePlayer.renderYawOffset = rotation.x;
                        mc.thePlayer.renderYawHead = rotation.x;
                    }
                }

                Scaffold.BlockEntry blockEntry = find(new Vec3(0, 0, 0));
                this.lastBlockEntry = blockEntry;

                if (!event.isPre() && blockEntry != null) {
                    int slot = getSlotWithBlock();

                    if (slot > -1) {

                        if (this.lastSlot != slot) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                            this.lastSlot = slot;
                        }

                        if (placeBlock(blockEntry.getPosition().add(0, 0, 0), blockEntry.getFacing(),
                                slot, swing.getValue())) {

                            if (tower.getValue() && !mc.thePlayer.isPotionActive(Potion.jump) && !mc.thePlayer.isMoving()
                                    && mc.gameSettings.keyBindJump.pressed) {
                                mc.thePlayer.motionY = .42f;
                                mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;

                                if (this.towerTimer.timeElapsed(1500L)) {
                                    this.towerTimer.resetTime();
                                    mc.thePlayer.motionY = -.28f;
                                }
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    @Subscribe
    public void onKeyboard(KeyboardEvent event) {
        if (!sprint.getValue() && event.getKey() == 29) {
            event.setCancelled(true);
        }
    }


    public BlockEntry findExpand(Vec3 offset3, int expand) {
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH,
                EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(mc.thePlayer.getPositionVector().add(offset3))
                .offset(EnumFacing.DOWN);
        if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir))
            return null;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir ||
                    !rayTrace(mc.thePlayer.getLook(0.0f),
                            this.getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockEntry(offset, invert[facing.ordinal()]);
        }
        for (int i = 0; i < expand; i++) {
            BlockPos[] offsets = new BlockPos[]{new BlockPos(-i, 0, 0), new BlockPos(i, 0, 0),
                    new BlockPos(0, 0, -i), new BlockPos(0, 0, i)};
            for (BlockPos offset : offsets) {
                BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
                if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
                for (EnumFacing facing : EnumFacing.values()) {
                    BlockPos offset2 = offsetPos.offset(facing);
                    if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir ||
                            !rayTrace(mc.thePlayer.getLook(0.0f),
                                    this.getPositionByFace(offset, invert[facing.ordinal()])))
                        continue;
                    return new BlockEntry(offset2, invert[facing.ordinal()]);
                }
            }
        }
        return null;
    }

    public boolean placeBlock(BlockPos blockPos, EnumFacing facing, int slot, boolean swing) {
        if (this.delayTimer.timeElapsed(this.delay.getValue().intValue())) {
            this.delayTimer.resetTime();

            BlockPos offset = blockPos.offset(facing);
            EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH,
                    EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            if (rayTrace(mc.thePlayer.getLook(0.0f), this.getPositionByFace(offset,
                    invert[facing.ordinal()]))) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
                Vec3f hitVec = (new Vec3f(blockPos)).add(0.5f, 0.5f, 0.5f)
                        .add((new Vec3f(facing.getDirectionVec())).scale(0.5f));
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack,
                        blockPos, facing, new Vec3d(hitVec.x, hitVec.y, hitVec.z))) {
                    if (swing) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPlaceable(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            Block block = (((ItemBlock) itemStack.getItem()).getBlock());
            return !(block instanceof BlockNote) && !(block instanceof BlockFurnace)
                    && !block.getLocalizedName().equalsIgnoreCase("Crafting Table")
                    && !(block instanceof BlockWeb) && !(block instanceof BlockFence)
                    && !(block instanceof BlockFenceGate)
                    && !(block instanceof BlockSlab) && !(block instanceof BlockStairs);
        }
        return true;
    }

    public int getSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (!isPlaceable(itemStack)) continue;
            if (itemStack != null && (itemStack.getItem() instanceof ItemAnvilBlock
                    || (itemStack.getItem() instanceof ItemBlock
                    && ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockSand))) {
                continue;
            }
            if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock)
                    || (((ItemBlock) itemStack.getItem()).getBlock().maxY -
                    ((ItemBlock) itemStack.getItem()).getBlock().minY != 1) && !(itemStack.getItem() instanceof ItemAnvilBlock)) {
                continue;
            }
            return i;
        }

        return -1;
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 8; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i) != null) {
                if (Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i) == null) {
                    continue;
                }
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);

                if (is.getItem() instanceof ItemBlock) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    public boolean placeBlockVerus(BlockPos blockPos, EnumFacing facing, int slot, boolean swing) {
        if (this.delayTimer.timeElapsed(this.delay.getValue().intValue())) {
            this.delayTimer.resetTime();

            BlockPos offset = blockPos.offset(facing);
            EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH,
                    EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            if (rayTrace(mc.thePlayer.getLook(0.0f), this.getPositionByFace(offset,
                    invert[facing.ordinal()]))) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);

                float f = MathUtil.getRandomInRange(.3f, .5f);

                Vec3f hitVec = (new Vec3f(blockPos)).add(f, f, f)
                        .add((new Vec3f(facing.getDirectionVec())).scale(f));
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                        stack, blockPos, facing, new Vec3d(hitVec.x, hitVec.y, hitVec.z))) {
                    if (swing) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }

    public Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double)
                facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.75,
                (double) position.getZ() + 0.5);
        return point.add(offset);
    }

    public BlockEntry find(Vec3 offset3) {
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
                EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(mc.thePlayer.getPositionVector().add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir
                    || !rayTrace(mc.thePlayer.getLook(0.0f),
                    getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockEntry(offset, invert[facing.ordinal()]);
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0),
                new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
        for (BlockPos offset : offsets) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir
                        || !rayTrace(mc.thePlayer.getLook(0.0f),
                        getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new BlockEntry(offset2, invert[facing.ordinal()]);
            }
        }
        return null;
    }

    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset(blockPosition).isVecInside(point)) continue;
            return false;
        }
        return true;
    }

    public class BlockEntry {

        private final BlockPos position;
        private final EnumFacing facing;

        BlockEntry(BlockPos position, EnumFacing facing) {
            this.position = position;
            this.facing = facing;
        }

        public BlockPos getPosition() {
            return this.position;
        }

        public EnumFacing getFacing() {
            return this.facing;
        }
    }
}