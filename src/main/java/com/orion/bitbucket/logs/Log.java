package com.orion.bitbucket.logs;


import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import java.net.URL;
public class Log {
    private final static Logger logger = Logger.getLogger(Log.class);

    public Log() {
        URL url = getClass().getClassLoader().getResource(LogConstant.LOG4J_XML_URL);
        DOMConfigurator.configure(url);
    }
    public static final class LogConstant {
        public final static String LOG4J_XML_URL = "log4j.xml";
        public final static boolean IS_DEBUG = true;
        public final static String TAG_INFO = "info";
        public final static String TAG_WARN = "warn";
        public final static String TAG_ERROR = "error";

    }
    public static void logger(String tag, String message) {
        if (LogConstant.IS_DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

            String logMessage = (className + "." + methodName + "():" + lineNumber + " : " + message);

            if (LogConstant.TAG_INFO.equals(tag)){
                info(logMessage);
            }
            if (LogConstant.TAG_WARN.equals(tag)){
                warn(logMessage);
            }
            if (LogConstant.TAG_ERROR.equals(tag)){
                error(logMessage);
            }
        }
    }
    private final static void info(String message) {
        logger.info(message);
    }
    private final static void warn(String message){
        logger.warn(message);
    }
    private final static void error(String message){
        logger.error(message);
    }
}