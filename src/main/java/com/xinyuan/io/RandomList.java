package com.xinyuan.io;

import java.util.ArrayList;
import java.util.Random;


/**
 * 可以随机获取容器元素的容器
 * @author xinyuan
 *
 * @param <T>
 */
public class RandomList<T> {

	private ArrayList<T> storage = new ArrayList<T>();
	private Random rand = new Random(47);
	
	public void add(T item){
		storage.add(item);
	}
	
	public T getRandom(){
		return storage.get(rand.nextInt(storage.size()));
	}
	
}
