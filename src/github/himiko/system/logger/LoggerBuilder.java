package github.himiko.system.logger;

import com.sun.jdi.ClassNotPreparedException;

public class LoggerBuilder {

    private Object loggerClass = null;

    public static LoggerBuilder createLogger(Object loggerClass)
    {
        return new LoggerBuilder(loggerClass);
    }

    private LoggerBuilder(Object loggerClass)
    {
        this.loggerClass = loggerClass;
    }

    public void setLoggerClass(Object clas)
    {

        this.loggerClass = clas;
    }

    public Object getLoggerClass()
    {
        return loggerClass;
    }

}
