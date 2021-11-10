package niggerlib.packetlib.packet;

import niggerlib.packetlib.io.NetInput;
import niggerlib.packetlib.io.NetOutput;

import java.io.IOException;

/**
 * The default packet header, using a varint packet length and id.
 */
public class DefaultPacketHeader implements PacketHeader {
    @Override
    public boolean isLengthVariable() {
        return true;
    }

    @Override
    public int getLengthSize() {
        return 5;
    }

    @Override
    public int getLengthSize(int length) {
        if ((length & -128) == 0) {
            return 1;
        } else if ((length & -16384) == 0) {
            return 2;
        } else if ((length & -2097152) == 0) {
            return 3;
        } else if ((length & -268435456) == 0) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public int readLength(NetInput in, int available) throws IOException {
        return in.readVarInt();
    }

    @Override
    public void writeLength(NetOutput out, int length) throws IOException {
        out.writeVarInt(length);
    }

    @Override
    public int readPacketId(NetInput in) throws IOException {
        return in.readVarInt();
    }

    @Override
    public void writePacketId(NetOutput out, int packetId) throws IOException {
        out.writeVarInt(packetId);
    }
}
