package niggerlib.mc.protocol.data.game.statistic;

import niggerlib.mc.protocol.util.ObjectUtil;

import java.util.Objects;

public class BreakBlockStatistic implements Statistic {
    private String id;

    public BreakBlockStatistic(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BreakBlockStatistic)) return false;

        BreakBlockStatistic that = (BreakBlockStatistic) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
