package commonJam;

import java.io.IOException;

/**
 * An interface representing any class whose objects get notified when
 * the objects they are observing update themselves.
 */
public interface Observer<Subject, ClientData> {
    void init() throws Exception;
    void update(Subject subject, ClientData data) throws IOException;
}
