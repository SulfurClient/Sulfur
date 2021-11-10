package gg.sulfur.client.api.proxy;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.socket.oio.OioSocketChannel;
import gg.sulfur.client.Sulfur;

import java.net.Proxy;
import java.net.Socket;

public class MinecraftProxyHandler implements ChannelFactory<OioSocketChannel> {

    private final Proxy proxy;

    public MinecraftProxyHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public OioSocketChannel newChannel() {
        Socket socket = new Socket(Sulfur.getInstance().getProxy());
        return new OioSocketChannel(socket);
    }
}
