package com.xinyuan.io;

public class CollectionsUtil {

	/**
	 * 拼接两个字符数组
	 * @param srcArr
	 * @param appendArr
	 * @return
	 */
	public static byte[] joinArr(byte[]srcArr,byte[] appendArr){
		byte[] dest = new byte[srcArr.length+appendArr.length];
		System.arraycopy(srcArr, 0, dest, 0, srcArr.length);
		System.arraycopy(appendArr, 0, dest, srcArr.length, appendArr.length);
		return dest;
		
	}
}
