package com.xinyuan.utils;


import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 *
 * 1. 持有Mapper的单例. 2. 返回值类型转换. 3. 批量转换Collection中的所有对象. 4.
 * 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 *
 */
public class BeanMapperUtil {
	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 基于Dozer转换对象的类型.
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	public static <T> List<T> mapList(Collection sourceList,
			Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copy(Object source, Object destinationObject) {

		dozer.map(source, destinationObject);
	}
	
	
	
	public static void main(String args[]){
		
/*//		TestUser a = new TestUser();
//		a.setUserid("a2");
//		a.setMobilemd5("a3");
//		TestUser B = new TestUser();
//		B.setUserid("B2");
//		B.setMobilemd5("B3");
		
		TestUser uM = new TestUser();
//		com.zs.sq.zz.dal.entities.User uE = new com.zs.sq.zz.dal.entities.User();
//		uM.setUserid(1);
//		uM.setCity("bj");
//		uM.setMobile("18400408641");
//		BeanMapper.copy(uM, uE);
//		try{
//			uM.getList().add(a);
//			uM.getList().add(B);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid", "1");
		map.put("mobilemd5", "185");
//		Map<String,Object> mapList = new HashMap<String,Object>();
//		mapList.put("userid", "a1");
//		mapList.put("mobilemd5", "a2");
//		map.put("list", mapList);
		uM = map(map,TestUser.class);
		System.out.println(uM.getUserid());*/
//		System.out.println(uM.getList().get(1).getUserid());
	}
}
