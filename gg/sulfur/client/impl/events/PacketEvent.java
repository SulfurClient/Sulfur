package gg.sulfur.client.impl.events;

import gg.sulfur.client.impl.events.enums.PacketDirection;
import net.minecraft.network.Packet;
import gg.sulfur.client.api.event.Event;

public class PacketEvent extends Event {

    private final PacketDirection packetDirection;
    private Packet packet;

    public PacketEvent(PacketDirection packetDirection, Packet packet) {
        this.packetDirection = packetDirection;
        this.packet = packet;
    }

    public PacketDirection getPacketDirection() {
        return packetDirection;
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> T getPacket() {
        return (T) packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public Class getPacketClass() {
        return packet.getClass();
    }
}
