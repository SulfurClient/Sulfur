package niggerlib.mc.protocol.packet.ingame.server.world;

import niggerlib.mc.protocol.packet.MinecraftPacket;
import niggerlib.packetlib.io.NetInput;
import niggerlib.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerUnloadChunkPacket extends MinecraftPacket {
    private int x;
    private int z;

    @SuppressWarnings("unused")
    private ServerUnloadChunkPacket() {
    }

    public ServerUnloadChunkPacket(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readInt();
        this.z = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.z);
    }
}
