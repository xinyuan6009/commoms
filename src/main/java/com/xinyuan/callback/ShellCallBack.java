package com.xinyuan.callback;


import java.io.InputStream;

/**
 * 回调接口
 * @author Administrator
 *
 */
public interface ShellCallBack {

	/**
	 * 回调方法
	 * @param msg
	 */
	public void call(String msg);
	
	/**
	 * 回调方法
	 * @param msg
	 */
	public void call(String msg, Object obj);
	
	/**
	 * 回调方法
	 * @param in
	 */
	public void call(InputStream in);
}
