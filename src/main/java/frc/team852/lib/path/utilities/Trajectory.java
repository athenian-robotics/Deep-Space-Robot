/**
 * Data type that contains a list of Pose2D objects and methods to interact with them
 */
package frc.team852.lib.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Trajectory<P extends Pose2D> implements Iterable<P>, CSVWritable {
    protected List<P> trajectory;

    public Trajectory() {
        this.trajectory = new ArrayList<>();
    }

    public Trajectory(List<P> trajectory) {
        this.trajectory = trajectory;
    }

    public boolean add(P p) {
        return trajectory.add(p);
    }

    public void add(int index, P element) {
        trajectory.add(index, element);
    }

    public boolean addAll(Collection<? extends P> c) {
        return trajectory.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends P> c) {
        return trajectory.addAll(index, c);
    }

    public P get(int index) {
        return trajectory.get(index);
    }

    public int size() {
        return trajectory.size();
    }

    public boolean isEmpty() {
        return trajectory.isEmpty();
    }

    public Object[] toArray() {
        return trajectory.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return trajectory.toArray(a);
    }

    public Iterator<P> iterator() {
        return null;
    }

    public void forEach(Consumer<? super P> action) {
        trajectory.forEach(action);
    }


    @Override
    public String toCSV() {
        StringBuilder res = new StringBuilder();
        for (P p : trajectory) {
            res.append(p.toCSV()).append("\n");
        }
        return res.toString();
    }
}
