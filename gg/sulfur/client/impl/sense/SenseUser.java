package gg.sulfur.client.impl.sense;

public class SenseUser {

    String accountName;
    String clientName;
    String server;

    public SenseUser(String name, String accName, String ip) {
        accountName = accName;
        clientName = name;
        server = ip;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getServer() {
        return server;
    }
}
