package com.xinyuan.utils;


public class MathUtil {

 	public static int getRadomNum4(){
 		Double d=Math.random()*(9999-1000+1)+1000;
		return d.intValue();
 	}
 	
 	/**
 	 * [min,max]随机
 	 * @param min
 	 * @param max
 	 * @return
 	 */
	public static Integer getRandNum(int min,int max){
		return (int) (Math.random()*(max - min + 1) + min);
	}
	
 	public static void main(String[] args) {
 		for(int i=0;i<200;i++){
 			int j=getRadomNum4();
 		 
 		}
		
	}
}
