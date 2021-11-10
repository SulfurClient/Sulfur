package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C16PacketClientStatus implements Packet {
    private C16PacketClientStatus.EnumState status;
    // private static final String __OBFID = "CL_00001348";

    public C16PacketClientStatus() {
    }

    public C16PacketClientStatus(C16PacketClientStatus.EnumState statusIn) {
        this.status = statusIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.status = (C16PacketClientStatus.EnumState) data.readEnumValue(C16PacketClientStatus.EnumState.class);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeEnumValue(this.status);
    }

    public void func_180758_a(INetHandlerPlayServer p_180758_1_) {
        p_180758_1_.processClientStatus(this);
    }

    public C16PacketClientStatus.EnumState getStatus() {
        return this.status;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180758_a((INetHandlerPlayServer) handler);
    }

    public enum EnumState {
        PERFORM_RESPAWN(),
        REQUEST_STATS(),
        OPEN_INVENTORY_ACHIEVEMENT();

        // private static final String __OBFID = "CL_00001349";

        EnumState() {
        }
    }
}
