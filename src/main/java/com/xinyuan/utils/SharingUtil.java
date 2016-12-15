package com.xinyuan.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 分表工具类
 * 本工具类用来实现分表计算
 * @author xy
 *
 */
public class SharingUtil {

	/*
	 * 默认哈希桶大小
	 */
	private static final Integer DEFAULT_HASH_BUCKET_CAPACITY = 100;
	
	/*
	 * 默认分表大小
	 */
	private static final Integer DAFAULT_TB_NUM = 10;
	
	
	/**
	 * 构建hash环
	 * 假设有10张分表
	 * 散列环容量是100
	 * 将十张表均匀分步到散列环中 每个表在散列环中对应的位置序列是0，10，20...90
	 * @throws Exception 
	 */
	private static List<Integer> buildHashCircle(Integer tbNum,Integer hashBuketCapacity) throws Exception{
		if(tbNum==null||hashBuketCapacity==null){
			throw new Exception("参数异常");
		}
		if(tbNum>hashBuketCapacity){
			throw new Exception("分表数目大于hash域空间 tbname:"+tbNum+" hashBuketCapacity:"+hashBuketCapacity);
		}
		//获取散列间隔
		int intervalNum = hashBuketCapacity/DAFAULT_TB_NUM;
		//获得散列区间数组
		List<Integer> hashPoints = new ArrayList<Integer>();
		//向hash环插入节点
		insertHashCircle(tbNum, hashPoints, intervalNum);
		//这个排序似乎在这边没有必要
		//Collections.sort(hashPoints);
		return hashPoints;
	}
	
	/**
	 * 插值生成HASH环
	 * @param tbNum
	 * @param hashPoints
	 * @param intervalNum
	 * @throws Exception 
	 */
	private static void insertHashCircle(Integer tbNum,List<Integer> hashPoints,int intervalNum ) throws Exception{
		int hashPoint = 0;
		//获取插入次数
		int insertTimes = getInsertTimes(tbNum);
		if(insertTimes==1){
			for(int i=0;i<tbNum;i++){
				hashPoints.add(hashPoint);
				hashPoint += intervalNum;
			}
		}else{
//			for(int i=0;i<insertTimes;i++){
//				hashPoint = 0;
//				intervalNum = getInsertPos(i, intervalNum);
//				if(tbNum<DAFAULT_TB_NUM){
//					for(int j=0;j<tbNum;j++){
//						hashPoints.add(hashPoint);
//						hashPoint += tintervalNum;
//						tintervalNum *= j; 
//					}
//				}else{
//					for(int j=0;j<DAFAULT_TB_NUM;j++){
//						hashPoint += tintervalNum;
//						hashPoints.add(hashPoint);
//						tintervalNum *= j; 
//					}
//				}
//				tbNum -= DAFAULT_TB_NUM;
//			}
		}
	}
	
	/**
	 * 计算每次插入的间隔
	 * @param times
	 * @return
	 * @throws Exception 
	 */
	private static Integer getInsertPos(int times,int interval) throws Exception{
		if(times>interval)
			throw new Exception("计算间隔异常");
		return (int) Math.ceil(interval/(times+1));
	}
	
	/**
	 * 计算插值次数
	 * @return
	 */
	private static Integer getInsertTimes(Integer tbNum){
		//double tfloat = tbNum/DAFAULT_TB_NUM;
		//计算插值次数
		int tnum = tbNum/DAFAULT_TB_NUM;
		//计算偏移
		int mod = tbNum/DAFAULT_TB_NUM;
		if(mod!=0){
			//插值修正
			tnum += 1;
		}
		return tnum;
	}
	
	
	/**
	 * 计算数据所在hash桶的位置
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static int getHashPoint(String str,Integer tbNum,Integer hashBuketCapacity) throws Exception{
		int code = str.hashCode();
		if(tbNum==null)
			tbNum = DAFAULT_TB_NUM;
		if(hashBuketCapacity==null)
			hashBuketCapacity = DEFAULT_HASH_BUCKET_CAPACITY;
		
		int value = Math.abs(code)%hashBuketCapacity;
		List<Integer> hashPoints = buildHashCircle(tbNum, hashBuketCapacity);
		int maxVal = Collections.max(hashPoints);
		hashPoints.add(value);
		Collections.sort(hashPoints);
		System.out.println("hash环："+hashPoints);
		int index = hashPoints.indexOf(value);
		return value<maxVal?index+1:0;
	}
	
	/**
	 * 计算数据所在hash桶的位置
	 * 
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static int getHashPoint(String str) throws Exception{
		return getHashPoint(str, 12, null);
	}
	
	public static void main(String[] args) throws Exception {
		for(int i=0;i<10;i++){
			System.out.println("生成的数据对应于表："+getHashPoint("aaaa"));
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
