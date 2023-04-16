package github.himiko.system.logger;

public enum LogCategory {
    WARNING, INFORMATION, ERROR;

    public String toString()
    {
        switch (this)
        {
            case ERROR:
                return "[ERROR]";
            case WARNING:
                return "[WARNING]";
            case INFORMATION:
                return "[INFORMATION]";
        }

        return "";
    }
}
