package com.xinyuan.utils;


import java.io.File;
import java.io.UnsupportedEncodingException;


public class PathUtil {
	/** 取得CLASS目录的根目录   E:/swl/workspace/xxx

	* @Title: getRootPath 
	* @Description: TODO() 
	* @return
	* @throws UnsupportedEncodingException
	* @return String  
	* @throws 
	*/ 
	
	public static String getRootPath() {
		java.net.URL url = PathUtil.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filePath=filePath.replaceAll("/", File.separator+File.separator);
		 filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
		if (filePath.endsWith(".jar"))
		{
			filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
		}
		if (filePath.contains(File.separator+"bin"))
		{
			filePath = filePath.substring(0, filePath.lastIndexOf(File.separator+"bin") + 1);
		}
		if (filePath.contains(File.separator+"lib"))
		{
			filePath = filePath.substring(0, filePath.lastIndexOf(File.separator+"lib") + 1);
		}
  		
		File file = new File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}
	
	public static void main(String[] args) {
		System.out.println(getRootPath());
	}
}
