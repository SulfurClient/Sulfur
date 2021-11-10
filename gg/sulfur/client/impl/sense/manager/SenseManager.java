package gg.sulfur.client.impl.sense.manager;

import gg.sulfur.client.impl.sense.SenseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SenseManager {

    ArrayList<SenseUser> senseUsers = new ArrayList<>();
    HashMap<String, String> senseUserMap = new HashMap<>();

    public ArrayList<SenseUser> getSenseUsers() {
        return senseUsers;
    }

    public void addUser(SenseUser user) {
        senseUsers.add(user);
    }

    public HashMap<String, String> getSenseUserMap() {
        return senseUserMap;
    }

    public boolean isSulfurUser(String name) {
        for (SenseUser senseUser : senseUsers) {
            if (senseUser.getAccountName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public void updateUser(SenseUser user) {
        if (this.getUser(user.getClientName()) != null) {
            senseUsers.remove(getUser(user.getClientName()));
            senseUsers.add(user);
            senseUserMap.put(user.getClientName(), user.getAccountName());
            return;
        }
        senseUsers.add(user);
        senseUserMap.put(user.getClientName(), user.getAccountName());
    }

    public SenseUser getUser(String user) {
        for (SenseUser senseUser : senseUsers) {
            if (Objects.equals(senseUser.getClientName(), user)) {
                return senseUser;
            }
        }
        return null;
    }

}
