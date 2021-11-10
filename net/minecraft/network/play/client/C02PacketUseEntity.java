package net.minecraft.network.play.client;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.io.IOException;

public class C02PacketUseEntity implements Packet {
    public int entityId;
    private C02PacketUseEntity.Action action;
    private Vec3 field_179713_c;
    // private static final String __OBFID = "CL_00001357";

    public C02PacketUseEntity() {
    }

    public int getEntityId() {
        return entityId;
    }

    public C02PacketUseEntity(Entity p_i45251_1_, C02PacketUseEntity.Action p_i45251_2_) {
        this.entityId = p_i45251_1_.getEntityId();
        this.action = p_i45251_2_;
    }

    public C02PacketUseEntity(Entity p_i45944_1_, Vec3 p_i45944_2_) {
        this(p_i45944_1_, C02PacketUseEntity.Action.INTERACT_AT);
        this.field_179713_c = p_i45944_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.entityId = data.readVarIntFromBuffer();
        this.action = (C02PacketUseEntity.Action) data.readEnumValue(C02PacketUseEntity.Action.class);

        if (this.action == C02PacketUseEntity.Action.INTERACT_AT) {
            this.field_179713_c = new Vec3(data.readFloat(), data.readFloat(), data.readFloat());
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.entityId);
        data.writeEnumValue(this.action);

        if (this.action == C02PacketUseEntity.Action.INTERACT_AT) {
            data.writeFloat((float) this.field_179713_c.xCoord);
            data.writeFloat((float) this.field_179713_c.yCoord);
            data.writeFloat((float) this.field_179713_c.zCoord);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processUseEntity(this);
    }

    public Entity getEntityFromWorld(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    public C02PacketUseEntity.Action getAction() {
        return this.action;
    }

    public Vec3 func_179712_b() {
        return this.field_179713_c;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }

    public enum Action {
        INTERACT(),
        ATTACK(),
        INTERACT_AT();

//        private static final C02PacketUseEntity.Action[] $VALUES = new C02PacketUseEntity.Action[]{INTERACT, ATTACK, INTERACT_AT};
        // private static final String __OBFID = "CL_00001358";

        Action() {
        }
    }
}
