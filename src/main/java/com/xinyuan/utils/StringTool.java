package com.xinyuan.utils;


import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {
	public static String jsonName = "date";
 
	/**
	 * java实现不区分大小写替换
	 * 
	 * @param source
	 * @param oldstring
	 * @param newstring
	 * @return
	 */
	public static String IgnoreCaseReplace(String source, String oldstring,
			String newstring) {
		//System.out.println("------------>java实现不区分大小写替换");
		try {
			String newSource = java.net.URLEncoder.encode(source, "UTF-8");
			String newOldstring = java.net.URLEncoder.encode(oldstring, "UTF-8");
			String newNewstring = java.net.URLEncoder.encode(newstring, "UTF-8");

			Pattern p = Pattern.compile(newOldstring, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(newSource);
			String ret = m.replaceAll(newNewstring);
			return java.net.URLDecoder.decode(ret, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return source;
		}
	}

	public static String replaceAll(String strSource, String strFrom,
			String strTo) {
		if (strSource == null) {
			return null;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}
 
    
    /** String 数组转换为 逗号隔开的字符串连接
    * @Title: arraytosplitstr 
    * @Description: TODO() 
    * @param items
    * @return     
    * @return String  
    * @throws 
    */ 
    
    public static String arraytosplitstr(String[] items)
    {
    	if(items==null)
    		return "";
    	StringBuffer sb1 = new StringBuffer();
    	if(items.length>1)
    	{
        	for(int i=0;i<items.length;i++)
        	{

        		if(i==(items.length-1))
        		{
            		sb1.append(items[i]);
         		}else
        		{
            		sb1.append(items[i]);
            		sb1.append(",");
        		}
        	}
    	}else
    	{
    		sb1.append(items[0]);
    	}
    	return sb1.toString();
    }
    
    /** String  逗号隔开的字符串连接转换为数组
    * @Title: splitstrtoarray 
    * @Description: TODO() 
    * @param splitstr
    * @return     
    * @return String [] 
    * @throws 
    */ 
    
    public static String[] splitstrtoarray(String splitstr)
    {
    	if(isEmpty(splitstr))
    		return new String[]{};
    	return	splitstr.split(",",-1);
     }
    
    /**取得指定长度 逗号分隔的问号字符串 
     * 例如：?,?,?
     * @return
     */
    public static String splitToSqlHelperStr(int length)
    {
    	StringBuffer buf=new StringBuffer();
    	for(int i=0;i<length;i++)
    	{
    		buf.append("?");
    		if(i<(length-1))
    		{
    			buf.append(",");
    		}
    	}
     	//String str = StringUtils.join(strarray, ",");
     	return	 buf.toString();
     }
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //System.out.println(getNextCardCode("0000888888880099"));
/*		String[] strArg = StringTool.split("$1$WX0102$0000000000000001$25036671", '$');
		System.out.println(strArg.length);
		for(int i=0;i<strArg.length;i++){
			System.out.println(strArg[i]);
		}
*/	/*	String[] strArg = "1$WX0102$0000000000000001$25036671".split("\\$");
		System.out.println(strArg.length);
		for(int i=0;i<strArg.length;i++){
			System.out.println(strArg[i]);
		}*/
	//	System.out.println(cutUrl("http://mypay.ebatong.com"));
		System.out.println(splitToSqlHelperStr(2));
	}
 
	
	/**把所有标准的 http://www.cxxcom/sss/sxx/x/x/或者htpps://www.cxx.com/sdfsdfx/x/x/x/截取到第3个/ 符号为止
	 * 结果形式：http://www.cxxcom/ htpps://www.cxx.com/
 	 * @param urlstr 需要截取的URL
	 * @return
	 */
	public static String cutUrl(String urlstr)
	{
		String http="http://";
		String https="https://";
		if(isNotEmpty(urlstr))
		{
			if(urlstr.startsWith(http))
			{
				urlstr=urlstr.substring(http.length(),urlstr.length());
				if(urlstr.contains("/")) 
				{
					urlstr=urlstr.substring(0, urlstr.indexOf("/")+1);
				}
				urlstr=http+urlstr;
			}
			if(urlstr.startsWith(https))
			{
				urlstr=urlstr.substring(https.length(),urlstr.length());
				if(urlstr.contains("/")) 
				{
					urlstr=urlstr.substring(0, urlstr.indexOf("/")+1);
				}
				urlstr=https+urlstr;
			}

 
		}else
		{
			urlstr="";
		}
 		return urlstr;
	}
	public static String likeStr(String str) {
		return "%" + str.trim() + "%";
	}
	/**
	 * 本类用到的方法--null转变成空
	 * 
	 * @return
	 */
	public static String nullToString(Object obj)
	{
		String resource="";
		//if (obj == null||obj.equals("null")||(null != obj && obj.toString().equalsIgnoreCase("null"))) 
		if (obj == null||obj.equals("null")) 
		{
			 return resource;
		} else
		{
			resource=obj.toString().trim();
		}
			return resource;
	 }
	
	/**
	 * 本类用到的方法--Integer转变成Double
	 * 
	 * @param obj
	 * @return
	 */
	public static Double intToDouble(Integer obj)
	{
		Double d=0d;
		//if (obj == null||obj.equals("null")||(null != obj && obj.toString().equalsIgnoreCase("null"))) 
		if (obj == null) 
		{
			 return d;
		} else
		{
			d=Double.valueOf(obj);
		}
			return d;
	 }
	//0100888888880099 => 0100888888880100
	public static String getNextCardCode(String oldCode){
		String newCode = ""+(Long.parseLong(oldCode)+1);
		while(newCode.length()<16){
			newCode = "0"+newCode;
		}
		return newCode;
	}
	
	/**
	 * 去掉所有空格
	 * @param str
	 * @return
	 */
	public static String trimAll(Object str){
		String newStr=nullToString(str);
		return newStr.replace(" ", nullToString(null));
	}
	
	/**
	 * 分隔字符  , 注意：如果前后有分隔符，String.split会多出来一个。该方法自动去掉前后分隔符再调用 String.split
	 *            注意：特殊字符 $ % 等，需要使用 转义   $, 改为 \\$ 
	 * aibo zeng 2009-06-09
	 * @param str
	 * @param ch
	 * @return
	 */
	public static String[] split(String str , char ch){
		if(str==null){
			return null;
		}
		if(str.charAt(0)==ch){
			str = str.substring(1);
		}
		if(str.charAt(str.length()-1)==ch){
			str = str.substring(0,str.length()-1);
		}
		return str.split(ch+"");
	}
	
	/**
	 * 判断 NULL和0的 LONG类型都返回flase 其他返回TRUE
	 */
	public static boolean isNotNullAndZeroLong(Long num){
		if(num!=null)
		{
			if(num>0)
			{
				return true;
			}else
			{	
				return  false;
			}
			
		}else
		{
			return false;
		}
	
	}
	
	/**
	 * Long类型 如果为NULL 返回0l
	 */
	public static Long NullLongReturnZero(Long num){
		if(num==null)
		{ 
			return 0l;
		}else
		{
			return num;
		}
	
	}
	/**
	 * Double类型 如果为NULL 返回0l
	 */
	public static Double NullDoubleReturnZero(Double num){
		if(num==null)
		{ 
			return 0d;
		}else
		{
			return num;
		}
	
	}
	/**
	 * 判断字符串是否为0
	 */
	public static String isZeroRturnNullStr(String str){
		if(StringTool.isNotEmpty(str))
		{
			if(str.equals("0"))
			{
				return "";
			}else
			{	
				return  str;
			}
			
		}else
		{
			return "";
		}
	
	}
	/**
	 * 去掉DOUBLE  类型小数点后面的数字 变整数
	 */
	public static String DoubleToInteger(Double doublestr)
	{
		if(doublestr==null)
		{
			return "";
		}
		String[] strlast=String.valueOf(doublestr).split("\\.", -1);
		if(isNotEmpty(strlast[0]))
		{
			return strlast[0];
		}else
		{
			return "";
		}
 	}
	
	/**
	 * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
	 */
	public static boolean isEmpty(String str){
		return str==null || str.length()==0 || str.trim().length()==0;
	}
	
	/**
	 * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
	 */
	public static boolean isNotEmpty(String str){
		return (str!=null && !"".equals(str.trim()));
	}
	/**
	 * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
	 */
	public static boolean isEmpty(Long o){
		return (o==null);
	}
	public static boolean isEmpty(Integer o){
		return (o==null);
	}
	public static boolean isNotEmpty(Long o){
		return (o!=null);
	}
 
	public static boolean isNotEmpty(Integer o){
		return (o!=null );
	}	
	public static boolean isNotEmpty(Date o){
		return (o!=null );
	}
	
	public static boolean isNotEmpty(BigDecimal o){
		return (o!=null );
	}
	
	public static boolean isNotEmpty(Object o){
		return (o!=null );
	}
	
	public static String htmlEscape(String strData)
	{
	    if (strData == null)
	    {
	        return "";
	    }
	    strData = replaceString(strData, "&", "&amp;");
	    strData = replaceString(strData, "<", "&lt;");
	    strData = replaceString(strData, ">", "&gt;");
	    strData = replaceString(strData, "'", "&apos;");
	    strData = replaceString(strData, "\"", "&quot;");
	    strData = replaceString(strData, "`", "&acute;");
 	    return strData;
	}
	public static String replaceString(String strData, String regex, String replacement)
	{
	    if (strData == null)
	    {
	        return null;
	    }
	    int index;
	    index = strData.indexOf(regex);
	    String strNew = "";
	    if (index >= 0)
	    {
	        while (index >= 0)
	        {
	            strNew += strData.substring(0, index) + replacement;
	            strData = strData.substring(index + regex.length());
	            index = strData.indexOf(regex);
	        }
	        strNew += strData;
	        return strNew;
	    }
	    return strData;
	}
}
