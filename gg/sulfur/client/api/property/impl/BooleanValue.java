package gg.sulfur.client.api.property.impl;

import gg.sulfur.client.api.property.Value;

public final class BooleanValue extends Value<Boolean> {

    public BooleanValue(String name, Object owner, Boolean value) {
        super(name, owner, value);
    }

    public BooleanValue(String name, Boolean value, Object owner) {
        super(name, owner, value);
    }

    public BooleanValue(String name, Object owner, Boolean value, Value parent) {
        super(name, owner, value);
        this.parent = parent;
    }

    @Override
    public Boolean getValue() {
        return super.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue() == obj || super.equals(obj);
    }
}
