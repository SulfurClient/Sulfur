package gg.sulfur.client.impl.managers;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.manager.Manager;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.api.versioning.BuildType;
import gg.sulfur.client.impl.modules.combat.*;
import gg.sulfur.client.impl.modules.exploit.*;
import gg.sulfur.client.impl.modules.misc.*;
import gg.sulfur.client.impl.modules.movement.*;
import gg.sulfur.client.impl.modules.player.*;
import gg.sulfur.client.impl.modules.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ModuleManager extends Manager<Module> {

    public ModuleManager() {
        super(new ArrayList<>());
    }
    public Module getByName(String name) {
        for (Module module : this.getObjects())
            if (module.getModuleData().getName().equals(name)) return module;
        throw new NoSuchElementException("Retard Exception: No module found");
    }
    public Module getByNameIgnoreSpaceCaseInsensitive(String name) {
        for (Module module : this.getObjects())
            if (module.getModuleData().getName().replace(" ", "").equalsIgnoreCase(name)) return module;
        throw new NoSuchElementException("Retard Exception: No module found");
    }

    /**
     * Sorts this manager by it's list's objects name string length
     *
     * @param fontRenderer - The {@code FontRenderer} used for obtaining the length
     */
    public void sort(FontRenderer fontRenderer) {
        getObjects().sort((a, b) -> {
            String dataA = a.getSuffix() == null ? "" : a.getSuffix();
            String dataB = b.getSuffix() == null ? "" : b.getSuffix();
            int first = fontRenderer.getStringWidth(a.getModuleData().getName() + dataA);
            int second = fontRenderer.getStringWidth(b.getModuleData().getName() + dataB);
            return second - first;
        });
    }

    public List<Module> getSorted(FontRenderer fontRenderer) {
        List<Module> moduleList = new ArrayList<>(getObjects());
        moduleList.sort((a, b) -> {
            String dataA = a.getSuffix() == null
                    ? ""
                    : a.getSuffix();
            String dataB = b.getSuffix() == null
                    ? ""
                    : b.getSuffix();
            String nameA = a.getModuleData().getName();
            String nameB = b.getModuleData().getName();

            int first = Minecraft.getMinecraft().fontRendererObj.getStringWidth(nameA + dataA);
            int second = Minecraft.getMinecraft().fontRendererObj.getStringWidth(nameB + dataB);
            return second - first;
        });
        return moduleList;
    }

    public List<Module> getSorted(CustomFontRenderer fontRenderer) {
        List<Module> moduleList = new ArrayList<>(getObjects());
        moduleList.sort((a, b) -> {
            String dataA = a.getSuffix() == null
                    ? ""
                    : a.getSuffix();
            String dataB = b.getSuffix() == null
                    ? ""
                    : b.getSuffix();
            String nameA = a.getModuleData().getName();
            String nameB = b.getModuleData().getName();

            int first = (int) fontRenderer.getWidth(nameA + dataA);
            int second = (int) fontRenderer.getWidth(nameB + dataB);
            return second - first;
        });
        return moduleList;
    }

    public List<Module> getModulesSortedAlternativeNoFont(FontRenderer fontRenderer) {
        List<Module> moduleList = new ArrayList<>(getObjects());
        moduleList.sort((a, b) -> {
            String dataA = a.getSuffix() == null ? "" : a.getSuffix();
            String dataB = b.getSuffix() == null ? "" : b.getSuffix();
            String nameA = a.getModuleData().hasOtherName() ? a.getModuleData().getOtherName() : a.getModuleData().getName();
            String nameB = b.getModuleData().hasOtherName() ? b.getModuleData().getOtherName() : b.getModuleData().getName();

            int first = fontRenderer.getStringWidth(nameA + dataA);
            int second = fontRenderer.getStringWidth(nameB + dataB);
            return second - first;
        });
        return moduleList;
    }

    /**
     * Sorts this manager by its list's objects name string length
     *
     * @param customFontRenderer - The {@code CustomFontRenderer} used for obtaining the length
     */
    public void sort(CustomFontRenderer customFontRenderer) {
        getObjects().sort((a, b) -> {
            String dataA = a.getSuffix() == null ? "" : a.getSuffix();
            String dataB = b.getSuffix() == null ? "" : b.getSuffix();
            String nameA = a.getModuleData().getName();
            String nameB = b.getModuleData().getName();

            int first = (int) customFontRenderer.getWidth(nameA + dataA);
            int second = (int) customFontRenderer.getWidth(nameB + dataB);
            return second - first;
        });
    }

    /**
     * Sorts this manager by its list's objects name string length with no suffix
     *
     * @param customFontRenderer - The {@code CustomFontRenderer} used for obtaining the length
     */
    public void sortNoSuffix(CustomFontRenderer customFontRenderer) {
        getObjects().sort((a, b) -> {
            String nameA = a.getModuleData().getName();
            String nameB = b.getModuleData().getName();

            int first = (int) customFontRenderer.getWidth(nameA);
            int second = (int) customFontRenderer.getWidth(nameB);
            return second - first;
        });
    }

    @Override
    public void onCreated() {
        // COMBAT
        add(new Aura(new ModuleData("Kill Aura", "KFC Nigger Slayer", "Minorities Run Over", 0, ModuleCategory.COMBAT)));
        add(new Velocity(new ModuleData("Velocity", "Weight Gain 5000", "No Hit Move", 0, ModuleCategory.COMBAT)));
        add(new TargetStrafe(new ModuleData("Target Strafe", "Nigger Spinner", "Nigger Spinner", 0, ModuleCategory.COMBAT)));
        add(new FastBow(new ModuleData("Fast Bow", "Columbine Harvester", "Speedy Bow", 0, ModuleCategory.COMBAT)));
        add(new AutoPot(new ModuleData("Auto Pot", 0, ModuleCategory.COMBAT)));
        add(new Criticals(new ModuleData("Criticals", 0, ModuleCategory.COMBAT)));
        add(new AutoClicker(new ModuleData("Auto Clicker", 0, ModuleCategory.COMBAT)));
        add(new AutoApple(new ModuleData("Auto Apple", 0, ModuleCategory.COMBAT)));

        // MOVEMENT
        add(new NoSlow(new ModuleData("No Slowdown", "Blacks Leaving Son Inator", "Weird Hands", 0, ModuleCategory.MOVEMENT)));
        //add(new Flight2(new ModuleData("Flight", "Jews When They See Money", "Drug Consumer", 0, ModuleCategory.MOVEMENT)));
        add(new Flight(new ModuleData("Flight", "Jews When They See Money", "Drug Consumer", 0, ModuleCategory.MOVEMENT)));
        add(new LongJump(new ModuleData("Long Jump", "Faggot Flinger", "Long Legs", 0, ModuleCategory.MOVEMENT)));
        add(new Speed(new ModuleData("Speed", "Auto Nigger", "Auto Nigger", 0, ModuleCategory.MOVEMENT)));
        add(new Sprint(new ModuleData("Sprint", "Auto Nigger", "Naruto Run", 0, ModuleCategory.MOVEMENT)));
        add(new InvMove(new ModuleData("InvMove", 0, ModuleCategory.MOVEMENT)));
        add(new Step(new ModuleData("Step", "Tall Guy", "Auto Jump", 0, ModuleCategory.MOVEMENT)));
        add(new Jesus(new ModuleData("Allah", "Jesus", "Piece of Plastic", 0, ModuleCategory.MOVEMENT)));
        add(new HighJump(new ModuleData("High Jump", "Border Hopper", "Auto Beaner", 0, ModuleCategory.MOVEMENT)));


        // PLAYER
        add(new NoFall(new ModuleData("No Fall", "Ass So Big No Fall Damage", "Fall Damage", 0, ModuleCategory.PLAYER)));
        add(new FastUse(new ModuleData("Fast Use", 0, ModuleCategory.PLAYER)));
        add(new InvManager(new ModuleData("Inv Manager", 0, ModuleCategory.PLAYER)));
        add(new Scaffold(new ModuleData("Scaffold", "Too fucken lazy to place blocks", "lazy as fuck", 0, ModuleCategory.PLAYER)));
        add(new ClickTP(new ModuleData("ClickTeleport", 0, ModuleCategory.PLAYER)));
        add(new SpinBot(new ModuleData("Spinbot", 0, ModuleCategory.PLAYER)));
        add(new Camera(new ModuleData("Camera", 0, ModuleCategory.PLAYER)));
        add(new Blink(new ModuleData("Blink", 0, ModuleCategory.PLAYER)));

        // MISC
        add(new Rotations(new ModuleData("Rotations", 0, ModuleCategory.MISC)));
        add(new AutoVclip(new ModuleData("Auto VClip", 0, ModuleCategory.MISC)));
        add(new Breaker(new ModuleData("Breaker", 0, ModuleCategory.MISC)));
        add(new AutoToggle(new ModuleData("Auto Toggle", 0, ModuleCategory.MISC)));
        add(new Timer(new ModuleData("Timer", 0, ModuleCategory.MISC)));
        add(new Panic(new ModuleData("Panic", 0, ModuleCategory.MISC)));
        add(new MCF(new ModuleData("MCF", 0, ModuleCategory.MISC)));
        add(new NameProtect(new ModuleData("Name Protect", 0, ModuleCategory.MISC)));
        add(new ThunderDetect(new ModuleData("Player Finder", 0, ModuleCategory.MISC)));
        add(new IceSpeed(new ModuleData("IceSpeed", 0, ModuleCategory.MISC)));
        add(new Stealer(new ModuleData("Stealer", 0, ModuleCategory.MISC)));
        add(new Freecam(new ModuleData("Freecam", 0, ModuleCategory.MISC)));
        add(new AutoRegister(new ModuleData("Auto Register", "Too lazy to /register", "Laziness", 0, ModuleCategory.MISC)));
        add(new KillSults(new ModuleData("Kill Sults", 0, ModuleCategory.MISC)));
        add(new Destruct(new ModuleData("Destruct", 0, ModuleCategory.MISC)));
        add(new Spammer(new ModuleData("Spammer", 0, ModuleCategory.MISC)));

        if (Sulfur.getInstance().getBuildType() == BuildType.DEV)
            add(new Debugger(new ModuleData("Debugger", 0, ModuleCategory.MISC)));

        add(new OofMod(new ModuleData("Oof Mod", 0, ModuleCategory.MISC)));
        add(new DeathSounds(new ModuleData("DeathSounds", 0, ModuleCategory.MISC)));
        //add(new TestModule(new ModuleData("Test Module", "Nigger Can't Code", "You need a brain to code", 0, ModuleCategory.MISC)));

        // RENDER
        add(new HurtColor(new ModuleData("HurtColor", 0, ModuleCategory.RENDER)));
        add(new Hud(new ModuleData("HUD", 0, ModuleCategory.RENDER)));
        add(new Animations(new ModuleData("Animations", 0, ModuleCategory.RENDER)));
        add(new FullBright(new ModuleData("Fullbright", "The sun is a deadly laser", "Auto Bright", 0, ModuleCategory.RENDER)));
        add(new ClickGUI(new ModuleData("Click UI", Keyboard.KEY_RSHIFT, ModuleCategory.RENDER)));
        add(new MemeESP(new ModuleData("MemeESP", 0, ModuleCategory.RENDER)));
        add(new ChestESP(new ModuleData("ChestESP", "auto nigger robber", "go rob chest now", 0, ModuleCategory.RENDER)));
        add(new Tracers(new ModuleData("Tracers", 0, ModuleCategory.RENDER)));
        add(new HitEffects(new ModuleData("Hit Effects", 0, ModuleCategory.RENDER)));
        add(new ESP(new ModuleData("ESP", 0, ModuleCategory.RENDER)));
        add(new Breadcrumbs(new ModuleData("Breadcrumbs", 0, ModuleCategory.RENDER)));
        add(new SpeedGraph(new ModuleData("Speed Graph", 0, ModuleCategory.RENDER)));
        add(new TimeChanger(new ModuleData("Time Changer", 0, ModuleCategory.RENDER)));
        add(new GameInformation(new ModuleData("Game Information", 0, ModuleCategory.RENDER)));
        add(new SessionInformation(new ModuleData("Session Information", 0, ModuleCategory.RENDER)));
        add(new ChinaHat(new ModuleData("China Hat", 0, ModuleCategory.RENDER)));
        add(new Spotify(new ModuleData("Spotify", 0, ModuleCategory.RENDER)));
        add(new Glow(new ModuleData("Glow", 0, ModuleCategory.RENDER)));
        add(new Particles(new ModuleData("Particles", 0, ModuleCategory.RENDER)));
        add(new PacketCounter(new ModuleData("Packet Counter", 0, ModuleCategory.RENDER)));

        // EXPLOIT
        add(new Phase(new ModuleData("Phase", 0, ModuleCategory.EXPLOIT)));
        add(new GodMode(new ModuleData("God Mode", 0, ModuleCategory.EXPLOIT)));
        add(new AntiFall(new ModuleData("AntiFall", 0, ModuleCategory.EXPLOIT)));
        add(new Crasher(new ModuleData("Crasher", 0, ModuleCategory.EXPLOIT)));
        add(new Disabler(new ModuleData("Disabler", 0, ModuleCategory.EXPLOIT)));

        add(new NoRotate(new ModuleData("No Rotate", 0, ModuleCategory.EXPLOIT)));

        // HIDDEN
        add(new Commands(new ModuleData("Commands", 0, ModuleCategory.HIDDEN)));
        
        //Automatically toggled
        super.get(AutoRegister.class).toggle();
        super.get(Hud.class).toggle();
        super.get(Sprint.class).toggle();
        super.get(Commands.class).toggle();
    }

    public List<Module> getAllInCategory(ModuleCategory category) {
        List<Module> list = new ArrayList<>();
        for (Module module : getObjects()) {
            if (module.getModuleData().category() == category) {
                list.add(module);
            }
        }
        return list;
    }

    public boolean isEnabled(Class<? extends Module> moduleClass) {
        return get(moduleClass).isToggled();
    }
}
