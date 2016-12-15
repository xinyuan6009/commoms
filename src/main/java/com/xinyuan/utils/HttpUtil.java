package com.xinyuan.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	
    public static final String UTF8 = "utf-8";
    public static final int POST = 2;
    public static final int GET = 1;
    public static final int PUT = 3;
    public static final int DELETE = 4;

	private HttpUtil() {super();}
	//设置请求和传输超时时间
    public static final RequestConfig DefaultRequestConfig
    		= RequestConfig.custom()
    		  .setSocketTimeout(10000)
    		  .setConnectTimeout(10000)
    		//  .setConnectionRequestTimeout(1)
     		  .build();

    /**
     * 用utf-8发送 get 请求
     */
    public static String get(String url) {
    	byte[] bytes = request(GET, url);
    	return readBytes(bytes, UTF8);
    }
    /**
     * 发送 get 请求
     */
    public static String get(String url, String charset) {
    	byte[] bytes = request(GET, url, "", charset);
    	return readBytes(bytes, charset);
    }

    /**
     * 发送 post 请求, data要encode
     */
    public static String post(String url, String data, String charset) {
    	byte[] bytes = request(POST, url, data, charset);
    	return readBytes(bytes, charset);
    }

    /**
     * 发送 post 请求, data不要encode
     */
    public static String post(String url, Map<String, String> data, String charset) {
    	log.info("添加金币请求:url-->"+url+" data:"+JsonUtil.gsonToJson(data));
    	byte[] bytes = request(POST, url, data, charset);
    	return readBytes(bytes, charset);
    }

    /**
     * 发送 put 请求, data要encode
     */
    public static String put(String url, Map<String, String> data, String charset) {
    	byte[] bytes = request(PUT, url, data, charset);
    	return readBytes(bytes, charset);
    }

    public static String delete(String url) { return delete(url, UTF8); }
    /**
     * 发送 delete 请求
     */
    public static String delete(String url, String charset) {
    	byte[] bytes = request(DELETE, url, "", charset);
    	return readBytes(bytes, charset);
    }

    /**
     * 下载二进制文件
     */
    public static byte[] download(String url, String charset) {
    	return request(GET, url, "", charset);
    }

    public static byte[] request(int method, String url) {
    	return request(0, makeRequest(method, url));
    }

    public static byte[] request(int method, String url, String data, String charset) {
    	return request(0, makeRequest(method, url, data, charset));
    }

    public static byte[] request(int method, String url, Map<String, String> data, String charset) {
    	return request(0, makeRequest(method, url, data, charset));
    }

    public static byte[] request(int method, String url, HttpEntity data) {
    	return request(0, makeRequest(method, url, data));
    }

    private static byte[] request(int ssl, HttpUriRequest request) {
    	if (request == null)
    		return null;

    	CloseableHttpClient client = createClient(ssl);
    	CloseableHttpResponse response = null;
    	byte[] result = null;
    	try {
    		long l = System.currentTimeMillis();
    		log.info("request : " + request.getURI()+" time:"+l);
    		response = client.execute(request);
    		long endl = System.currentTimeMillis()-l;
    		log.info("end time相差毫秒 ：:"+endl);
    		
    		HttpEntity entity = response.getEntity();
     		result = EntityUtils.toByteArray(entity);
    		EntityUtils.consume(entity);
    		log.info("result length : " + result.length);
		}
    	catch (Exception ex) {
			ex.printStackTrace();
			log.info("http异常："+ex.getMessage()
					);
		}
    	finally {
        	try {
        		if (response != null) {response.close();}
        		request.abort();
        		client.close();
    		}
        	catch (Exception ex) {
    			ex.printStackTrace();
    			log.info("finally Exception", ex);
    		}
    	}
    	return result;
	}
    
    private static CloseableHttpClient createClient(int ssl) {
		if (ssl == 1)
    		return createSslClient();
    	return HttpClients.custom()
    			.setDefaultRequestConfig(DefaultRequestConfig)
    			.build();
	}

    private static HttpUriRequest makeRequest(int method, String url, String data, String charset) {
    	return makeRequest(method, url, makeEntity(data, charset));
	}

    private static HttpUriRequest makeRequest(int method, String url, Map<String, String> data, String charset) {
    	return makeRequest(method, url, makeEntity(data, charset));
	}

    private static HttpUriRequest makeRequest(int method, String url) {
    	return makeRequest(method, url, null);
	}

    private static HttpUriRequest makeRequest(int method, String url, HttpEntity entity) {
    	switch (method) {
			case GET    : return new HttpGet(url);
			case POST   :
				HttpPost post = new HttpPost(url);
				if (entity != null) {post.setEntity(entity);}
				return post;
			case PUT    :
				HttpPut put = new HttpPut(url);
				if (entity != null) {put.setEntity(entity);}
				return put;
			case DELETE : return new HttpDelete(url);
			default     : return null;
		}
	}

    /**
     * 用String生成HttpEntity，String应该已经是key=value&key2=value2的形式
     */
    private static HttpEntity makeEntity(final String data, String charset) {
    	if (StringUtils.isBlank(data))
    		return null;

//    	String params = data.replace("\n", "");
    	ContentType type = ContentType.create("application/x-www-form-urlencoded", charset);
    	StringEntity entity = new StringEntity(data, type);
        return entity;
    }

    /**
     * 用Map<String, String>生成HttpEntity
     */
    private static HttpEntity makeEntity(final Map<String, String> data, String charset) {
    	if (data == null || data.size() == 0)
    		return null;

    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	for (Entry<String, String> entry : data.entrySet()) {
    		params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

    	UrlEncodedFormEntity entity = null;
    	try {
    		entity = new UrlEncodedFormEntity(params, charset);
 		} 
    	catch (Exception ex) {
			ex.printStackTrace();
    		log.error("{}",ex);
		}
    	return entity;
	}

    /**
    * bytes 转换为String
    */
    private static String readBytes(byte[] bytes, String charset) {
       	if (bytes == null)
    		return "";

    	String result = null;
    	try {
    		result = new String(bytes, charset);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("{}",ex);
    	}
    	return result;
    }

    /**
     * 上传文件
     */
    public boolean upload(String url, Map<String, byte[]> data, String charset) {
    	if (data == null || data.size() == 0)
    		return false;

        CloseableHttpClient client = createClient(0);
        boolean result = false;
        try {
            HttpPost http = new HttpPost(url);
            HttpEntity reqbody = fromBinaryBody(data);
            http.setEntity(reqbody);

            log.info("upload: " + http.getURI());
            CloseableHttpResponse response = client.execute(http);
            try {
                // 获取响应实体
                HttpEntity resbody = response.getEntity();
                EntityUtils.consume(resbody);
                result = true;
            }
            finally {
                response.close();
            }
        }
        catch (Exception ex) {
        	ex.printStackTrace();
        	log.error("{}",ex);
        }
        finally {
            // 关闭连接,释放资源
            try {
            	client.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static HttpEntity fromBinaryBody(Map<String, byte[]> data) {
    	if (data == null || data.size() == 0)
    		return null;
    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    	for (Entry<String, byte[]> entry : data.entrySet()) {
    		builder.addBinaryBody(entry.getKey(), entry.getValue());
		}
    	return builder.build();
	}

    /**
     * HttpClient连接SSL使用自定义证书
     */
    public static String ssl(int method, KeyStore trustStore, String url, Map<String, String> data, String charset) {
    	HttpUriRequest request = makeRequest(method, url, data, charset);
    	if (request == null)
    		return null;

    	byte[] bytes = ssl(request, trustStore, charset);
    	return readBytes(bytes, charset);
    }

    /**
     * HttpClient连接SSL
     */
    public static byte[] ssl(HttpUriRequest request, KeyStore trustStore, String charset) {
    	if (request == null)
    		return null;
        CloseableHttpClient client = null;
    	CloseableHttpResponse response = null;
    	byte[] result = null;
    	try {
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
            	.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
            	.build();
            // Allow TLSv1 protocol only
            @SuppressWarnings("deprecation")
    		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    		log.info("request : " + request.getURI());
    		response = client.execute(request);
    		HttpEntity entity = response.getEntity();
    		log.info("result length : " + entity.getContentLength());
    		result = EntityUtils.toByteArray(entity);
    		EntityUtils.consume(entity);
		}
    	catch (Exception ex) {
			ex.printStackTrace();
			log.info("{}",ex);
		}
    	finally {
        	try {
        		if (response != null) {response.close();}
				if (request != null) {request.abort();}
				if (client != null) {client.close();}
    		}
        	catch (Exception ex) {
    			ex.printStackTrace();
    			log.info("finally Exception", ex);
    		}
    	}

    	return result;
    }

    /**
     * HttpClient连接SSL
     */
    public static String ssl(int method, String url, Map<String, String> data, String charset) {
    	HttpUriRequest request = makeRequest(method, url, data, charset);
    	if (request == null)
    		return null;

    	return ssl(request, charset);
    }

    /**
     * HttpClient连接SSL使用TrustStrategy
     */
    public static String ssl(HttpUriRequest request, String charset) {
    	if (request == null)
    		return null;

        CloseableHttpClient client = null;
    	CloseableHttpResponse response = null;
    	String result = null;
    	try {
            client = createSslClient();
    		log.info("request : " + request.getURI());
    		response = client.execute(request);
    		HttpEntity entity = response.getEntity();
    		result = EntityUtils.toString(entity);
    		EntityUtils.consume(entity);
		}
    	catch (Exception ex) {
			ex.printStackTrace();
			log.info("{}",ex);
		}
    	finally {
        	try {
        		if (response != null) {response.close();}
        		request.abort();
        		client.close();
    		}
        	catch (Exception ex) {
    			ex.printStackTrace();
    			log.info("finally Exception", ex);
    		}
    	}

    	return result;
    }

    public static CloseableHttpClient createSslClient() {
		try {
			SSLContext context = new SSLContextBuilder()
				.loadTrustMaterial(null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(context);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return HttpClients.createDefault();
    }
    
    public static KeyStore getKeyStore(String file, String password) {
        KeyStore trustStore = null;
        InputStream instream = null;
        try {
        	trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        	//instream = new FileInputStream(new File(file));
        	instream = HttpUtil.class.getClassLoader().getResourceAsStream(file);
            trustStore.load(instream, password.toCharArray());
        } 
		catch (Exception ex) {
			ex.printStackTrace();
			trustStore = null;
		}
        finally {
        	if (instream != null) {
        		try {
        			instream.close();
				}
        		catch (Exception ex) {
					ex.printStackTrace();
				}
        	}
        }
        return trustStore;
    }

	public static String WebRequestGet(String url) {return WebRequestGet(url, UTF8);}

	public static String WebRequestGet(String url, String charset) {
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		URLConnection conn = null;
		URL realUrl = null;//打开和URL之间的连接

		try {
			realUrl = new URL(url);
			conn = realUrl.openConnection();//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*"); 
			conn.setRequestProperty("connection", "Keep-Alive"); 
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"); //建立实际的连接
			conn.setConnectTimeout(300);
			conn.setReadTimeout(8000);
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine())!= null){
				sb.append(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{//使用finally块来关闭输入流
			try{
				if (in != null){
					in.close();
				}
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String Request(String url, String method, String data, String charset) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		HttpURLConnection conn = null;
		URL realUrl = null;//打开和URL之间的连接

		try {
			if (method.equalsIgnoreCase("GET") && !StringUtils.isBlank(data)) {
				if (url.indexOf("?") >= 0) {
	                url += "&" + data;
	            }
	            else {
	                url += "?" + data;
	            } 
			}
			realUrl = new URL(url);
			conn = (HttpURLConnection)realUrl.openConnection();//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*"); 
			conn.setRequestProperty("connection", "Keep-Alive"); 
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"); //建立实际的连接
			conn.setConnectTimeout(300);
			conn.setReadTimeout(8000);
			if (method.equalsIgnoreCase("POST")) {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.setInstanceFollowRedirects(true);
			}
			conn.connect();
			if (method.equalsIgnoreCase("POST")) {
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.writeBytes(data);
				out.flush();
				out.close();
			}
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = reader.readLine())!= null){
				sb.append(line);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{//使用finally块来关闭输入流
			if (reader != null){
				reader.close();
			}
			conn.disconnect();
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("aaa", "我不知道");
		map.put("bbb", "中文参数");

		KeyStore trustStore = getKeyStore("zx_server.keysstore", "12345678");
		String url = "https://test.cfund108.com:7002/kdwis";
		HttpUriRequest request = makeRequest(GET, url, map, UTF8);
        CloseableHttpClient client = createSslClient();
    	CloseableHttpResponse response = null;
    	byte[] result = null;
    	try {
    		response = client.execute(request);
    		HttpEntity entity = response.getEntity();
    		result = EntityUtils.toByteArray(entity);
    		EntityUtils.consume(entity);
		}
    	catch (Exception ex) {
			ex.printStackTrace();
			log.info("{}",ex);
		}
    	finally {
        	try {
        		if (response != null) {response.close();}
        		request.abort();
        		client.close();
    		}
        	catch (Exception ex) {
    			ex.printStackTrace();
    		}
    	}

    	String readBytes = readBytes(result, UTF8);
    	System.out.println("ssl : " + readBytes);
		
		//String result = ssl(GET, trustStore, url, map, UTF8);
		System.out.println("ssl : " + result);
	}
	
}
