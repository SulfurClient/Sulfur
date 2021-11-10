package net.minecraft.crash;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class CrashReportCategory {
    private final String name;
    private final List children = Lists.newArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];
    // private static final String __OBFID = "CL_00001409";

    public CrashReportCategory(String name) {
        this.name = name;
    }

    public static String getCoordinateInfo(double x, double y, double z) {
        return String.format("%.2f,%.2f,%.2f - %s", x, y, z, getCoordinateInfo(new BlockPos(x, y, z)));
    }

    public static String getCoordinateInfo(BlockPos pos) {
        int var1 = pos.getX();
        int var2 = pos.getY();
        int var3 = pos.getZ();
        StringBuilder var4 = new StringBuilder();

        try {
            var4.append(String.format("World: (%d,%d,%d)", var1, var2, var3));
        } catch (Throwable var17) {
            var4.append("(Error finding world loc)");
        }

        var4.append(", ");
        int var5;
        int var6;
        int var7;
        int var8;
        int var9;
        int var10;
        int var11;
        int var12;
        int var13;

        try {
            var5 = var1 >> 4;
            var6 = var3 >> 4;
            var7 = var1 & 15;
            var8 = var2 >> 4;
            var9 = var3 & 15;
            var10 = var5 << 4;
            var11 = var6 << 4;
            var12 = (var5 + 1 << 4) - 1;
            var13 = (var6 + 1 << 4) - 1;
            var4.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", var7, var8, var9, var5, var6, var10, var11, var12, var13));
        } catch (Throwable var16) {
            var4.append("(Error finding chunk loc)");
        }

        var4.append(", ");

        try {
            var5 = var1 >> 9;
            var6 = var3 >> 9;
            var7 = var5 << 5;
            var8 = var6 << 5;
            var9 = (var5 + 1 << 5) - 1;
            var10 = (var6 + 1 << 5) - 1;
            var11 = var5 << 9;
            var12 = var6 << 9;
            var13 = (var5 + 1 << 9) - 1;
            int var14 = (var6 + 1 << 9) - 1;
            var4.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", var5, var6, var7, var8, var9, var10, var11, var12, var13, var14));
        } catch (Throwable var15) {
            var4.append("(Error finding world loc)");
        }

        return var4.toString();
    }

    /**
     * Adds a Crashreport section with the given name with the value set to the result of the given Callable;
     */
    public void addCrashSectionCallable(String sectionName, Callable callable) {
        try {
            this.addCrashSection(sectionName, callable.call());
        } catch (Throwable var4) {
            this.addCrashSectionThrowable(sectionName, var4);
        }
    }

    /**
     * Adds a Crashreport section with the given name with the given value (convered .toString())
     */
    public void addCrashSection(String sectionName, Object value) {
        this.children.add(new CrashReportCategory.Entry(sectionName, value));
    }

    /**
     * Adds a Crashreport section with the given name with the given Throwable
     */
    public void addCrashSectionThrowable(String sectionName, Throwable throwable) {
        this.addCrashSection(sectionName, throwable);
    }

    /**
     * Resets our stack trace according to the current trace, pruning the deepest 3 entries.  The parameter indicates
     * how many additional deepest entries to prune.  Returns the number of entries in the resulting pruned stack trace.
     */
    public int getPrunedStackTrace(int size) {
        StackTraceElement[] var2 = Thread.currentThread().getStackTrace();

        if (var2.length <= 0) {
            return 0;
        } else {
            this.stackTrace = new StackTraceElement[var2.length - 3 - size];
            System.arraycopy(var2, 3 + size, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    /**
     * Do the deepest two elements of our saved stack trace match the given elements, in order from the deepest?
     */
    public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2) {
        if (this.stackTrace.length != 0 && s1 != null) {
            StackTraceElement var3 = this.stackTrace[0];

            if (var3.isNativeMethod() == s1.isNativeMethod() && var3.getClassName().equals(s1.getClassName()) && Objects.equals(var3.getFileName(), s1.getFileName()) && var3.getMethodName().equals(s1.getMethodName())) {
                if (s2 == null == this.stackTrace.length > 1) {
                    return false;
                } else if (s2 != null && !this.stackTrace[1].equals(s2)) {
                    return false;
                } else {
                    this.stackTrace[0] = s1;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Removes the given number entries from the bottom of the stack trace.
     */
    public void trimStackTraceEntriesFromBottom(int amount) {
        StackTraceElement[] var2 = new StackTraceElement[this.stackTrace.length - amount];
        System.arraycopy(this.stackTrace, 0, var2, 0, var2.length);
        this.stackTrace = var2;
    }

    public void appendToStringBuilder(StringBuilder builder) {
        builder.append("-- ").append(this.name).append(" --\n");
        builder.append("Details:");

        for (Object child : this.children) {
            Entry var3 = (Entry) child;
            builder.append("\n\t");
            builder.append(var3.getKey());
            builder.append(": ");
            builder.append(var3.getValue());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0) {
            builder.append("\nStacktrace:");
            StackTraceElement[] var6 = this.stackTrace;

            for (StackTraceElement var5 : var6) {
                builder.append("\n\tat ");
                builder.append(var5.toString());
            }
        }
    }

    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

    public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData) {
        final int var4 = Block.getIdFromBlock(blockIn);
        // private static final String __OBFID = "CL_00001426";
        category.addCrashSectionCallable("Block type", () -> {
            try {
                return String.format("ID #%d (%s // %s)", var4, blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
            } catch (Throwable var2) {
                return "ID #" + var4;
            }
        });
        // private static final String __OBFID = "CL_00001441";
        category.addCrashSectionCallable("Block data value", () -> {
            if (blockData < 0) {
                return "Unknown? (Got " + blockData + ")";
            } else {
                String var1 = String.format("%4s", new Object[]{Integer.toBinaryString(blockData)}).replace(" ", "0");
                return String.format("%1$d / 0x%1$X / 0b%2$s", blockData, var1);
            }
        });
        // private static final String __OBFID = "CL_00001465";
        category.addCrashSectionCallable("Block location", () -> CrashReportCategory.getCoordinateInfo(pos));
    }

    public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final IBlockState state) {
        category.addCrashSectionCallable("Block", new Callable() {
            // private static final String __OBFID = "CL_00002617";
            public String func_175753_a() {
                return state.toString();
            }

            public Object call() {
                return this.func_175753_a();
            }
        });
        category.addCrashSectionCallable("Block location", new Callable() {
            // private static final String __OBFID = "CL_00002616";
            public String func_175751_a() {
                return CrashReportCategory.getCoordinateInfo(pos);
            }

            public Object call() {
                return this.func_175751_a();
            }
        });
    }

    static class Entry {
        private final String key;
        private final String value;
        // private static final String __OBFID = "CL_00001489";

        public Entry(String key, Object value) {
            this.key = key;

            if (value == null) {
                this.value = "~~NULL~~";
            } else if (value instanceof Throwable) {
                Throwable var3 = (Throwable) value;
                this.value = "~~ERROR~~ " + var3.getClass().getSimpleName() + ": " + var3.getMessage();
            } else {
                this.value = value.toString();
            }
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }
}
