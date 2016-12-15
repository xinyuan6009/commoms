package com.xinyuan.utils;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XmlUtil {
	/**
	 * 从本地路径读取xml文档
	 */
	public static Document getDocument(String localpath) throws DocumentException{
		SAXReader reader = new SAXReader();
		return reader.read(localpath);
	}

	public static Document loadFromStream(InputStream in) throws DocumentException{
		SAXReader reader = new SAXReader();
		return reader.read(in);
	}

	/**
	 * 从httl url读取xml文档
	 */
	public static Document loadFromUrl(String url) throws DocumentException{
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			byte[] bytes = HttpUtil.download(url, "utf-8");
			doc = reader.read(new ByteArrayInputStream(bytes));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return doc;
	}
	
	public static Document LoadFromText(String xml) throws DocumentException{
		if (StringUtils.isBlank(xml))
			return null;
		return DocumentHelper.parseText(xml);
	}

	public static Document getXmlTextDocument(String xmlData) throws Exception {
		return DocumentHelper.parseText(xmlData);
	}
}
