package niggerlib.mc.protocol.packet.ingame.client.window;

import niggerlib.mc.protocol.packet.MinecraftPacket;
import niggerlib.packetlib.io.NetInput;
import niggerlib.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientConfirmTransactionPacket extends MinecraftPacket {
    private int windowId;
    private int actionId;
    private boolean accepted;

    @SuppressWarnings("unused")
    private ClientConfirmTransactionPacket() {
    }

    public ClientConfirmTransactionPacket(int windowId, int actionId, boolean accepted) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.accepted = accepted;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getActionId() {
        return this.actionId;
    }

    public boolean getAccepted() {
        return this.accepted;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.actionId = in.readShort();
        this.accepted = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.actionId);
        out.writeBoolean(this.accepted);
    }
}
