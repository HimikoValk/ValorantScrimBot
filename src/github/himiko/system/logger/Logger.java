package github.himiko.system.logger;

public interface Logger {
    void trace(Class<?> callingClass, LogCategory logCategory,String message);
    boolean isTracable();
}
