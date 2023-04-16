package github.himiko.system.scrim.logger;

import github.himiko.system.logger.LogCategory;
import github.himiko.system.logger.Logger;
import github.himiko.system.logger.utils.LoggerUtil;

public class ScrimLogger implements Logger {

    @Override
    public void trace(Class<?> callingClass, LogCategory logCategory, String message) {
        LoggerUtil.createLog( " ["+ callingClass.getName()+".java Is Calling] \tMessage:"+ message,logCategory);
    }

    @Override
    public boolean isTracable() {
        return true;
    }
}
