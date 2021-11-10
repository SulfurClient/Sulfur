package gg.sulfur.client.impl.managers;

import gg.sulfur.client.api.manager.Manager;
import gg.sulfur.client.api.property.Value;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ValueManager extends Manager<Value> {

    public ValueManager() {
        super(new ArrayList<>());
    }

    public <T extends Value> T getValueFromOwner(Object object, String name) {
        for (Value<?> value : getObjects()) {
            if (value.getOwner() == object && value.getName().equalsIgnoreCase(name)) {
                return (T) value;
            }
        }
        return null;
    }

    public List<Value> getValuesFromOwner(Object owner) {
        List<Value> list = new ArrayList<>();
        for (Value value : getObjects()) {
            if (value.getOwner() == owner) {
                list.add(value);
            }
        }
        return list;
    }

    public boolean hasValues(Object owner) {
        for (Value value : getObjects())
            if (value.getOwner() == owner)
                return true;
        return false;
    }
}
