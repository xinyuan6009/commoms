package com.xinyuan.utils;


import java.io.File;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**自定义日志类   
 * 调用方法：
 *   private static Logger log = LoggerTool.getInstance().getLogger(ZmqServerStart.class.getName());
 * @author swl
 *
 */
public class LoggerTool {
 	private static  String contentflag="default";//项目标志
  	private Vector<LogAppender> list  ;	//缓存 
	private static LoggerTool instance;
 
	public   void initContent(String logfiname)  {
		 
		PropertyConfigurator.configure(getRootPath(logfiname));
  	}
	public   String getRootPath(String logfiname) {
		String path= PathUtil.getRootPath()+File.separator+"log4j"+File.separator+logfiname+File.separator+"log4j.properties";
	    System.out.println("log4j-->path:"+path);
	  
 	    return path;
	}
	
 
	public static LoggerTool getInstance()  {
		synchronized (LoggerTool.class) {
			if (instance == null) {
				instance = new LoggerTool();
 			}
		}
   		return instance;
	}
 

 
 
	public   Logger getLogger(Class classobj) {
		return getLogger (classobj.getSimpleName());
	}
	/**
	 * 从root复制一个新的日志类
	 */
	public   Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
           return logger;
	}
  
 
	public static void main(String[] args) throws Exception {
		LoggerTool.getInstance().initContent("logserver");
		Logger logger = LoggerTool.getInstance().getLogger("aaa");
		for (int i = 0; i < 1; i++) {
			logger.info("this is a info "+i+" !");
			logger.error("this is a error "+i+" !");
			logger.debug("this is a debug "+i+" !");
		}

		System.out.println("Over");
	}
}
