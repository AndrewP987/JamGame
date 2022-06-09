package commonJam.solver;

import java.util.Collection;

public interface Configuration {
    boolean isSolution();
    Collection<Configuration> getNeighbors();
    boolean equals(Object other);
    int hashCode();
    String toString();
}
