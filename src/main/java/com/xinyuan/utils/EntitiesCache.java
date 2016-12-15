package com.xinyuan.utils;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntitiesCache {

	private EntitiesCache() {}
	
	private static EntitiesCache instance;
	public static synchronized EntitiesCache getInstance() throws Exception {
		if (instance == null) {
			instance = new EntitiesCache();
		}
		return instance;
	}

	/**
     * 获取某实体的所有属性
     */
    public Map<String, Field> getFields(Class<?> clazz) {
    	String key = getKey(clazz);
    	this.register(clazz);

        return this.properties.get(key);
    }

	/**
     * 获取某实体的name对应的属性
     */
    public Field getField(Class<?> clazz, String name) {
		Map<String, Field> map = getFields(clazz);
		if (map.containsKey(name.toLowerCase()))
        	return map.get(name);
		return null;
    }

	/**
	 *  实体BEAN转换成MAP 存放 key:字段KEY value：值
	 */
	public static Map<String, String> beanToMap(Object object) throws Exception {
		Map<String, Field> fields = EntitiesCache.getInstance().getFields(object.getClass());
		//map的字段名都小写
		Map<String, String> map = new HashMap<String, String>();
		//获取自己的所有属性
		for (Map.Entry<String, Field> entry : fields.entrySet()) {
			Object value = null;
			try {
				Field field = entry.getValue();
				Reflections.makeAccessible(field);
				value = field.get(object);
			}
			catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
			if (value == null)
				continue;

			String result = (value instanceof Date)
						  ? DateUtil.DateToString((Date)value, "yyyy-MM-dd HH:mm:ss")
						  : value.toString();
						  
			map.put(entry.getKey(), result);
		}
	
		return map;
	}

	/**
	 * String map转换为Object map
	 */
	public static Map<String, Object> mapStringToObject(final Map<String, String> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setObject(final Object obj, final Map<String, String> map) throws Exception {
		Map<String, Field> fields = EntitiesCache.getInstance().getFields(obj.getClass());
		Object data = null;
		try {
			for (String name : map.keySet()) {
				data = map.get(name);
				if (fields.containsKey(name.toLowerCase()) == false)
					continue;
				Field field = fields.get(name.toLowerCase());
				Reflections.makeAccessible(field);
 				Object value = getValue(field, map.get(name)==null?"":map.get(name).toString());
   				field.set(obj, value);
			}
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	private static Object getValue(Field field, String str) {
		Object value = null;
		if (field.getType() == Date.class)
			value = (str==null||str.equals(""))?null:DateUtil.StringToDate(str, "yyyy-MM-dd HH:mm:ss");
		else if (field.getType() == String.class)
			value = (str==null||str.equals(""))? "":str;
		else if (field.getType() == Byte.class)
			value = Byte.parseByte(str);
		else if (field.getType() == Integer.class)
			value = (str==null||str.equals(""))?0:Integer.parseInt(str);
		else if (field.getType() == Long.class)
			value = (str==null||str.equals(""))?0:Long.parseLong(str);
		else if (field.getType() == Double.class)
			value = (str==null||str.equals(""))?0.0:Double.parseDouble(str);
		else if (field.getType() == Float.class)
			value = (str==null||str.equals(""))?0.0:Float.parseFloat(str);
		else if (field.getType() == Map.class)
			value = (str==null||str.equals(""))?null:strToMap(str);
		else 
			value = str;
		return value;
	}

	private static Map<Integer,String> strToMap(String str){
		String dataStr = str.substring(1, str.length()-1);
		if(dataStr==null||dataStr.trim().equals("")) return new HashMap<>();
		String[] arr =  dataStr.split(",");
		Map<Integer,String> roles = new HashMap<>();
		for(int i=0;i<arr.length;i++){
			Integer temp = Integer.parseInt(arr[i].replace("=", "").trim());
			roles.put(temp, "");
		}
		return roles;
	}
	
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setField(final Object obj, final String name, final Object value) throws Exception {
		Field field = EntitiesCache.getInstance().getField(obj.getClass(), name);
		if (field == null)
			return;

		try {
			Reflections.makeAccessible(field);
			field.set(obj, value);
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	/**
     * 在缓存中添加一个实体
     */
    public void register(Class<?> clazz) {
        String key = getKey(clazz);
        if (this.exists(key))
            return;

        //map的字段名都小写
        Map<String, Field> map = new HashMap<String, Field>();
        //获取自己的所有属性
        Field[] fields = clazz.getDeclaredFields();
        
     //   Field[] fields = clazz.getFields();
        for (Field f : fields) {
        	if (f.getName().indexOf("$")>=0)
        		continue;
        	map.put(f.getName().toLowerCase(), f);
		}
        this.properties.put(key, map);
    }
	
	//缓存字典
	private final Map<String, Map<String, Field>> properties = new HashMap<String, Map<String, Field>>();

	private static String getKey(Class<?> clazz) {
		String type = clazz.toString();
		//System.out.println("--"+type);
		return type.substring(6, type.length());
	}
    
    private boolean exists(String key) {
		return this.properties.containsKey(key);
	}


}
