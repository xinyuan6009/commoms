package com.xinyuan.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class RegexUtil {
	
	/**{number32card},{number32card} 替换所有的{} 
	 * @param str 匹配字符串
	 * @param replacevalue  替换的目标值
	 * @return
	 */
	public static  String  replaceAllForReg(String str,String replacevalue)
	{
 
  		//也可以用Pattern.compile(rexp,Pattern.CASE_INSENSITIVE)表示整体都忽略大小写
 		Pattern p = Pattern.compile("(\\$\\{\\w+\\})");
		Matcher m = p.matcher(StringTool.nullToString(str));
		StringBuffer sb = new StringBuffer(); 
        int i=0; 
        boolean result = m.find(); 
         while(result) 
         {
        	 // 使用循环将句子里所有的   找出并替换再将内容加到 sb 里
         	 String getmatchstr=m.group();
        	 System.out.println(m.groupCount()+"第"+i+"次匹配后 sb 的内容是："+getmatchstr);
         	 m.appendReplacement(sb,replacevalue.toString()); 
             result = m.find(); 
             i++;
  		}
         m.appendTail(sb);
         return sb.toString();
	}
	
	/**替换指定KEY值的${key} 的表达式
	 * @param srcstr
	 * @param replaceKey
	 * @param replaceValue
	 * @return
	 */
	public static  String  replaceForReg(String srcstr,String replaceKey,String replaceValue)
	{
		if(StringUtils.isBlank(srcstr))
		{
			return "";
		}
  		String sb=srcstr.replaceAll("(\\$\\{"+replaceKey+"+\\})", replaceValue);
         return sb;
	}
	public static String replaceNewQuerySql(String querySQL) {
		if(StringTool.isEmpty(querySQL))
		{
			return "";
		}
		// 去掉多余的空格
 		querySQL = StringTool.replaceAll(querySQL, "  =  ", "=");
		querySQL = StringTool.replaceAll(querySQL, "  =", "=");
		querySQL = StringTool.replaceAll(querySQL, "=  ", "=");
		// System.out.println(querySQL);
		querySQL = StringTool.replaceAll(querySQL, " = ", "=");
		querySQL = StringTool.replaceAll(querySQL, " =", "=");
		querySQL = StringTool.replaceAll(querySQL, "= ", "=");
		//
		querySQL = StringTool.replaceAll(querySQL, " >=", ">=");
		querySQL = StringTool.replaceAll(querySQL, " <=", "<=");
		//
		querySQL = StringTool.replaceAll(querySQL, " >", ">");
		querySQL = StringTool.replaceAll(querySQL, " <", "<");
		//
		querySQL = StringTool.replaceAll(querySQL, " !=", "!=");
		querySQL = StringTool.replaceAll(querySQL, " <> ", "<>");
		querySQL = StringTool.replaceAll(querySQL, " <>", "<>");
		querySQL = StringTool.replaceAll(querySQL, "<> ", "<>");
		querySQL = StringTool.replaceAll(querySQL, "$ ", "$");
  		querySQL = StringTool.replaceAll(querySQL, "like${", " like ${");
		// logger.info("--->对LIKE处理后:"+querySQL);
 		querySQL = StringTool.replaceAll(querySQL, "$ {", " ${");

		querySQL = StringTool.replaceAll(querySQL, "\n", " ");
		querySQL = StringTool.replaceAll(querySQL, "\t", " ");
		querySQL = StringTool.replaceAll(querySQL, "{ ", "{");
		querySQL = StringTool.replaceAll(querySQL, " }", "} ");
		// logger.info("之前：去空格后查询语句："+querySQL);
		querySQL = StringTool.replaceAll(querySQL, " %}", "%}");
		querySQL = StringTool.replaceAll(querySQL, " % }", "%}");

		querySQL = StringTool.replaceAll(querySQL, "{% ", "{%");
		querySQL = StringTool.replaceAll(querySQL, "{ % ", "{%");

		querySQL = StringTool.replaceAll(querySQL, "% }", "%}");
		querySQL = StringTool.replaceAll(querySQL, " % }", "%}");
		//log.info("去空格后查询语句：" + querySQL);
		return querySQL;
	}
	
	
    
	    /**包含特殊字符 返回TRUE
	     * @param str
	     * @return
	     */
	    public static boolean sqlValidate(String str) {
	    	if(StringTool.isEmpty(str))
	    	{
	    		return false;
	    	}
	        str = str.toLowerCase();//统一转为小写
	        String badStr = "insert|delete|update|drop|truncate";//过滤掉的sql关键字，可以手动添加
	        String[] badStrs = badStr.split("\\|");
	        for (int i = 0; i < badStrs.length; i++) {
	            if (str.indexOf(badStrs[i]) >= 0) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    public static void main(String[] args) {
	    	//System.out.println(replaceForReg("WHERE ${ctype}  c.theday=${theday} ","ctype", "20150806"));
	    	
	    	System.out.println(replaceAllForReg("WHERE ${ctype}  c.theday=(${theday})", "20150806"));

	    }
}
