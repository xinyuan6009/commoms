package com.xinyuan.utils;


import org.apache.log4j.*;

import java.util.Enumeration;

public class LogBuilder {
	private static final Logger rooter = Logger.getRootLogger();

	/**
	 * 从root复制一个新的日志类
	 */
	public static Logger clone(String name) {
		Logger logger = Logger.getLogger(name);
		if (logger.getAppender(name) != null)
			return logger;

        logger.removeAllAppenders();
        logger.setLevel(rooter.getLevel());
        logger.setAdditivity(false);
        FileAppender appender = newAppender(name);
        logger.addAppender(appender);
        return logger;
	}

	/**
	 * 从root复制一个新的日志类
	 */
	public static Logger clone1(String name) {
		String info = name.toUpperCase()+"_INFO";
		Logger logger = Logger.getLogger(info);
		if (logger.getAppender(info) != null)
			return logger;

        logger.removeAllAppenders();
        logger.setLevel(rooter.getLevel());
        logger.setAdditivity(false);
        FileAppender appender = newAppender(name,"INFO");
        logger.addAppender(appender);
        

        logger.setLevel(rooter.getLevel());
        logger.setAdditivity(false);
        appender = newAppender(name,"DEBUG");
        logger.addAppender(appender);
        
        logger.setLevel(rooter.getLevel());
        logger.setAdditivity(false);
        appender = newAppender(name,"_ERROR");
        logger.addAppender(appender);
        return logger;
	}
	
	/**
	 * 从root复制一个新的 FileAppender
	 */
	private static DailyRollingFileAppender newAppender(String name) {
		DailyRollingFileAppender source = getFileAppender();
		///usr/local/zzpay-java/log/info.log
		String file = source.getFile();
		int slash = file.lastIndexOf("/");
		file = file.substring(0, slash + 1) + name + ".log";

		DailyRollingFileAppender target = new  DailyRollingFileAppender();
		target.setName(name);
		target.setFile(file);
		target.setAppend(true);
		target.setEncoding("UTF-8");
		target.activateOptions();
		target.setDatePattern(source.getDatePattern());
		target.setLayout(source.getLayout());
		return target;
	}

	/**
	 * 从root复制一个新的 FileAppender
	 */
	private static DailyRollingFileAppender newAppender(String name,String level) {
		DailyRollingFileAppender source = getFileAppender();
		String file = source.getFile();
		int slash = file.lastIndexOf("/");
		file = file.substring(0, slash + 1) + name + "_"+level+".log";
		DailyRollingFileAppender target = new  DailyRollingFileAppender();
		target.setName(name);
		target.setFile(file);
		target.setAppend(true);
		target.setEncoding("UTF-8");
		target.activateOptions();
		target.setDatePattern(source.getDatePattern());
		target.setLayout(source.getLayout());
		return target;
	}
	
	/**
	 * 从root复制一个新的 DailyRollingFileAppender
	 */
	private static DailyRollingFileAppender getFileAppender() {
		@SuppressWarnings("rawtypes")
		Enumeration all = rooter.getAllAppenders();
		while (all.hasMoreElements()) {
			Appender appender = (Appender) all.nextElement();

			if (appender instanceof DailyRollingFileAppender)
				return (DailyRollingFileAppender) appender;
		}

		DailyRollingFileAppender appender = new DailyRollingFileAppender();
		appender.setAppend(true);
		appender.setFile("info.log");
		appender.setDatePattern("'_'yyyy-MM-dd_HH'.log'");
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%n%-d{yyyy-MM-dd HH:mm:ss.SSS} %p (%F:%L) [%c.%t] - %r%n%m%n---------------------------------------------------------------------------%n");
		appender.setLayout(layout);
		return appender;
	}

	public static void main(String[] args) {
		Logger logger = clone("test");
		for (int i = 0; i < 2; i++) {
			logger.info("this is a info "+i+" !");
			logger.error("this is a error "+i+" !");
			rooter.info("this is a info "+i+" !");
			rooter.error("this is a error "+i+" !");
		}

		System.out.println("Over");
	}
}
