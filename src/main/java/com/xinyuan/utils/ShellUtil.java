package com.xinyuan.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.xinyuan.callback.ShellCallBack;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class ShellUtil {

	private  String hostname ;
	private  String username;
	private  String password ;
	private  Connection conn = null;
	private  Session ssh = null;
	private  BufferedReader brs = null;

	
	
	public ShellUtil(String hostname,String username,String password){
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}
	
	public void call(String command,ShellCallBack ShellCallBack){
		if(StringUtils.isBlank(hostname)||StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			return;
		}
		try {
			if(login()){
				exeComm(command,ShellCallBack);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			shutDown();
		}
	}
	
	public void call(String command,ShellCallBack ShellCallBack,Object obj){
		if(StringUtils.isBlank(hostname)||StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			return;
		}
		try {
			if(login()){
				exeComm(command,ShellCallBack,obj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			shutDown();
		}
	}
	
	/**
	 * 批量执行命令
	 * @param command
	 * @param ShellCallBack
	 */
	public void callBatch(String command,ShellCallBack ShellCallBack){
		if(StringUtils.isBlank(hostname)||StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			return;
		}
		try {
			if(login()){
				exeComm(command,ShellCallBack);
			}
		} catch (IOException e) {
			e.printStackTrace();
			shutDown();
		}
	}
	
	/**
	 * 远程登录
	 * 
	 * @return
	 * @throws IOException 
	 */
	private  boolean login() throws IOException {
		// 指明连接主机的IP地址
		conn = new Connection(hostname);
		// 连接到主机
		conn.connect();
		// 使用用户名和密码校验
		boolean isconn = conn.authenticateWithPassword(username, password);
		if (isconn) {
			return true;
		}
		return false;
	}

	/**
	 * 执行命令
	 * 
	 * @param comm
	 * @throws IOException 
	 */
	private void exeComm(String comm,ShellCallBack ShellCallBack) throws IOException {
		// 获取一个session
		ssh = conn.openSession();
		ssh.execCommand(comm);
		InputStream is = new StreamGobbler(ssh.getStdout());
		brs = new BufferedReader(new InputStreamReader(is));
		while (true) {
			String line = brs.readLine();
			if (line == null) {
				break;
			}
			if(ShellCallBack!=null){
				ShellCallBack.call(line);
			}
		}
	}

	/**
	 * 执行命令
	 * 
	 * @param comm
	 * @throws IOException 
	 */
	private void exeComm(String comm,ShellCallBack ShellCallBack,Object obj) throws IOException {
		// 获取一个session
		ssh = conn.openSession();
		ssh.execCommand(comm);
		InputStream is = new StreamGobbler(ssh.getStdout());
		brs = new BufferedReader(new InputStreamReader(is));
		while (true) {
			String line = brs.readLine();
			if (line == null) {
				break;
			}
			if(ShellCallBack!=null){
				ShellCallBack.call(line,obj);
			}
		}
	}
	
	/**
	 * 关闭连接
	 */
	public void shutDown() {
		try {
			if (brs != null) {
				brs.close();
			}
			if (ssh != null) {
				ssh.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
