package github.himiko.system.logger.utils;

import github.himiko.system.logger.LogCategory;
import github.himiko.util.ConsoleColorUtil;

public class LoggerUtil {

    public static void createLog(String message, LogCategory category)
    {
        switch (category)
        {
            case INFORMATION:
                System.out.println(ConsoleColorUtil.ANSI_GREEN + category.toString() + message);
                System.out.println(ConsoleColorUtil.ANSI_WHITE);
                break;
            case ERROR:
                System.err.println(category.toString() + message);
                System.out.println(ConsoleColorUtil.ANSI_WHITE);
                break;
            case WARNING:
                System.out.println(ConsoleColorUtil.ANSI_YELLOW + category.toString() + message);
                System.out.println(ConsoleColorUtil.ANSI_WHITE);
                break;
        }
    }


}
