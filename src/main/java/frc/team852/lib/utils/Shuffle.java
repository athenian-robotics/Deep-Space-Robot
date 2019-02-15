package frc.team852.lib.utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import java.util.HashMap;

public class Shuffle {
    private static ShuffleboardTab defaultTab = Shuffleboard. getTab("Drive");
    private static HashMap<String, Shuffle> cached = new HashMap<>();

    private NetworkTableEntry networkTableEntry;

    private Shuffle(Class c, String n, Object o, boolean helper) {
        this.networkTableEntry = defaultTab.add(getName(c, n), o).getEntry();
    }

    public Shuffle(Class c, String n, double d) {
        this(c, n, d, false);
    }

    public Shuffle(Class c, String n, boolean b) {
        this(c, n, b, false);
    }

    public Shuffle(Class c, String n, String s) {
        this(c, n, s, false);
    }

    public Shuffle set(double value) {
        networkTableEntry.setNumber(value);
        return this;
    }

    public Shuffle set(boolean value) {
        networkTableEntry.setBoolean(value);
        return this;
    }

    public Shuffle set(String value) {
        networkTableEntry.setString(value);
        return this;
    }

    public double get() {
        return getD();
    }

    public double getD() {
        return networkTableEntry.getDouble(0);
    }

    public boolean getB() {
        return networkTableEntry.getBoolean(false);
    }

    public String getS() {
        return networkTableEntry.getString("");
    }

    public static Shuffle put(Object p, String n, double d) {
        String fullName = getName(p.getClass(), n);
        
        if (!cached.containsKey(fullName)) {
            Shuffle shuffle = new Shuffle(p.getClass(), n, d);
            cached.put(fullName, shuffle);
            return shuffle;
        }
        else {
            Shuffle shuffle = cached.get(fullName);
            shuffle.set(d);
            return shuffle;
        }
    }

    public static Shuffle put(Object p, String n, boolean b) {
        String fullName = getName(p.getClass(), n);
        
        if (!cached.containsKey(fullName)) {
            Shuffle shuffle = new Shuffle(p.getClass(), n, b);
            cached.put(fullName, shuffle);
            return shuffle;
        }
        else {
            Shuffle shuffle = cached.get(fullName);
            shuffle.set(b);
            return shuffle;
        }
    }

    public static Shuffle put(Object p, String n, String s) {
        String fullName = getName(p.getClass(), n);
        
        if (!cached.containsKey(fullName)) {
            Shuffle shuffle = new Shuffle(p.getClass(), n, s);
            cached.put(fullName, shuffle);
            return shuffle;
        }
        else {
            Shuffle shuffle = cached.get(fullName);
            shuffle.set(s);
            return shuffle;
        }
    }
    
    private static String getName(Class c, String n) {
        return c.getSimpleName() + " " + n;
    }
}
