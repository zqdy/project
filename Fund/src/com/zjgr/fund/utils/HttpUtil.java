/**
* @JSONHelper.java
* @description:
* 网络层工具类
* @author: Jin Pang 2013/12/15
* @version: 1.0.0
*/
package com.zjgr.fund.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.text.TextUtils;
import android.util.Log;

import com.zjgr.fund.Constant;
import com.zjgr.fund.net.NetType;


/**
 * http工具类
 *
 */
public class HttpUtil {
	private static final String UNIWAP_PROXY_SERVER = "10.0.0.172"; // cmwap、uniwap和3gwap所用代理地址都10.0.0.172:80
	private static final String CTWAP_PROXY_SERVER = "10.0.0.200"; // ctwap所用代理地址为10.0.0.200：80

	private static final HttpClient httpClient;

	private static boolean isWifi = false; // 当前是否为wifi连接
	private static String apnName = ""; // 如果非wifi连接，当前所使用接入点名称

	static {
		// 设定HTTP初始化参数
		httpClient = new DefaultHttpClient();   

		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		params.setParameter(ClientPNames.CONNECTION_MANAGER_FACTORY,
				new ClientConnectionManagerFactory() {
					public ClientConnectionManager newInstance(
							HttpParams params, SchemeRegistry schemeRegistry) {
						return new ThreadSafeClientConnManager(params,
								schemeRegistry);
					}
				});

		HttpClientParams.setRedirecting(params, true);
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.2.15) Gecko/20110303 Ubuntu/10.10 (maverick) Firefox/3.6.15");
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(params, false);
		params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
	}

	/**
	 * 设置当前网络是否为WIFI网络
	 * 
	 * @param isWifi
	 */
	public static void setWifi(boolean isWf) {
		isWifi = isWf;
	}

	/**
	 * 设置当前网络的接点名称
	 * 
	 * @param apnName
	 */
	public static void setApnName(String name) {
		apnName = name;
	}

	/**
	 * 判断服务器是否支持gzip压缩
	 * 
	 * @param response
	 * @return
	 */
	private static boolean isSupportGzip(HttpResponse response) {
		Header contentEncoding = response.getFirstHeader("Content-Encoding");

		return contentEncoding != null
				&& contentEncoding.getValue().equalsIgnoreCase("gzip");
	}

	/**
	 * 添加Gzip压缩支持
	 * 
	 * @param request
	 */
	private static void supportGzip(HttpRequest request) {
		// 添加对gzip的支持
		request.addHeader("Accept-Encoding", "gzip");
	}

	

	/**
	 * 通过POST提交JSON数据 返回json
	 * 
	 * @param url
	 *            服务器目标地址
	 * @param value
	 *            上传给服务器的值，可以是LIST、MAP、或JAVABEAN
	 * @param clazz
	 *            返回的结果类
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException
	 */
	public static <T> T postJSON(String url, Object value, Class<T> clazz)
			throws ParseException, IOException, HttpResponseException,
			JSONException {
		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);

		HttpPost request = new HttpPost(url);
		// 添加Gzip支持
		supportGzip(request);

		// 检查是否需要设置代理
		checkProxy(request);

		try {
			// request.addHeader(HTTP.CONTENT_TYPE, "application/json");
			String data = JSONUtil.toJSON(value);
			Log.d(Constant.TAG, "HttpRequestHelper data:" + data);
			StringEntity entity = new StringEntity(data, HTTP.UTF_8);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			request.setEntity(entity);

			return processResponse(httpClient.execute(request), clazz);
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}
	}

	/**
	 * 通过POST提交JSON数据 返回json
	 * 
	 * @param url
	 *            服务器目标地址
	 * @param value
	 *            上传给服务器的值，可以是LIST、MAP、或JAVABEAN
	 * @param clazz
	 *            返回的结果类
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException
	 */
	public static <T> T postJSON(String url, String json, Class<T> clazz)
			throws ParseException, IOException, HttpResponseException,
			JSONException {
		HttpPost request = new HttpPost(url);
		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);
		Log.d(Constant.TAG, "HttpRequestHelper data:" + json);
		// 添加Gzip支持
		supportGzip(request);

		// 检查是否需要设置代理
		checkProxy(request);
		try {
			//强制关闭连接
			httpClient.getConnectionManager().closeExpiredConnections();
			// request.addHeader(HTTP.CONTENT_TYPE, "application/json");

			StringEntity entity = new StringEntity(json, HTTP.UTF_8);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			request.setEntity(entity);

			return processResponse(httpClient.execute(request), clazz);
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}
	}

	/**
	 * 表单提交 返回字符
	 * 
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpResponseException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T post(String url, Map<String, String> params)
			throws ParseException, ClientProtocolException, IOException,
			HttpResponseException {

		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);
		Log.d(Constant.TAG, "HttpRequestHelper data:" + params);

		HttpPost request = new HttpPost(url);

		// 添加Gzip支持
		supportGzip(request);

		// 检查是否需要设置代理
		checkProxy(request);

		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() == null)
				continue;

			parameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		try {
			// 统一编码
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);

			request.setEntity(form);
			httpClient.getConnectionManager().closeExpiredConnections();
			return (T) processResponse(httpClient.execute(request));
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}

	}
	
	/**
	 * 表单提交 返回字符
	 * 
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T postObj(String url, Object obj, Class<T> clazz)
			throws ParseException, ClientProtocolException, IOException,
			HttpResponseException, JSONException {
		
		String jsonStrParam = JSONUtil.toJSON(obj);
		HttpPost request = new HttpPost(url);

		// 添加Gzip支持
		supportGzip(request);
		
		// 检查是否需要设置代理
		checkProxy(request);


		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		parameters.add(new BasicNameValuePair(Constant.SUBMIT_PARAM_NAME, jsonStrParam));
		try {
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);

			request.setEntity(form);
			httpClient.getConnectionManager().closeExpiredConnections();
			return processResponse(httpClient.execute(request),clazz);
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}

	}
	
	/**
	 * 表单提交 返回字符
	 * 
	 * @param <T>
	 * @param url
	 * @param params
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpResponseException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T postObj(String url, Object obj)
			throws ParseException, ClientProtocolException, IOException,
			HttpResponseException {
		String jsonStrParam = JSONUtil.toJSON(obj);
		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);
		Log.d(Constant.TAG, "HttpRequestHelper data:" + Constant.SUBMIT_PARAM_NAME + "=" + jsonStrParam);
		

		HttpPost request = new HttpPost(url);

		// 添加Gzip支持
		supportGzip(request);
		
		// 检查是否需要设置代理
		checkProxy(request);

		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		parameters.add(new BasicNameValuePair(Constant.SUBMIT_PARAM_NAME, jsonStrParam));
		try {
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);

			request.setEntity(form);
			httpClient.getConnectionManager().closeExpiredConnections();
			return (T) processResponse(httpClient.execute(request));
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}

	}
	

	/**
	 * 处理响应结果
	 * 
	 * @param response
	 * @param clazz
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws JSONException
	 */
	private static <T> T processResponse(HttpResponse response, Class<T> clazz)
			throws ParseException, IOException, HttpResponseException,
			JSONException {
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			String result = null;
			if (isSupportGzip(response)) {
				InputStream is = new GZIPInputStream(entity.getContent());
				Reader reader = new InputStreamReader(is,
						EntityUtils.getContentCharSet(entity));
				CharArrayBuffer buffer = new CharArrayBuffer(
						(int) entity.getContentLength());
				try {
					char[] tmp = new char[1024];
					int l;
					while ((l = reader.read(tmp)) != -1) {
						buffer.append(tmp, 0, l);
					}
				} finally {
					reader.close();
				}
				result = buffer.toString();
				// LogUtil.debug(result);
			} else {
				result = EntityUtils.toString(entity);
			}

			entity.consumeContent(); // 释放或销毁内容

			if (TextUtils.isEmpty(result)) {
				return null;
			}
			// 将返回的文本结果进行json解析
			Log.w(Constant.TAG, "processResponse rcv :" + result);
			return JSONUtil.parseObject(result, clazz);
		} else {
			throw new HttpResponseException(statusCode);
		}
	}

	/**
	 * 处理响应结果
	 * 
	 * @param response
	 * @param clazz
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private static String processResponse(HttpResponse response)
			throws ParseException, IOException, HttpResponseException {
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			String result = null;
			if (isSupportGzip(response)) {
				InputStream is = new GZIPInputStream(entity.getContent());
				Reader reader = new InputStreamReader(is,
						EntityUtils.getContentCharSet(entity));
				CharArrayBuffer buffer = new CharArrayBuffer(
						(int) entity.getContentLength());
				try {
					char[] tmp = new char[1024];
					int l;
					while ((l = reader.read(tmp)) != -1) {
						buffer.append(tmp, 0, l);
					}
				} finally {
					reader.close();
				}
				result = buffer.toString();
			} else {
				result = EntityUtils.toString(entity);
			}

			entity.consumeContent(); // 释放或销毁内容

			if (TextUtils.isEmpty(result)) {
				return null;
			}
			// 将返回的文本结果进行json解析
			Log.d(Constant.TAG, "processResponse  json  rcv :" + result);
			return result;
		} else {
			throw new HttpResponseException(statusCode);
		}
	}

	private static byte[] readStream(InputStream is) throws Exception { // 读入输入流

		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}

		byte[] data = os.toByteArray();

		os.close();
		is.close();

		return data;
	}

	// public static String get(String path) throws Exception { // Get方法访问网络
	//
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5 * 1000);
	//
	// if (conn.getResponseCode() == 200) {
	// byte[] data = readStream(conn.getInputStream());
	// return new String(data, "UTF-8");
	// }
	// return null;
	// }

	/**
	 * 从指定的URL地址获取JSON数据
	 * 
	 * @param url
	 *            目标地址
	 * @param data
	 * 
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getString(String url, Map<String, String> data)
			throws ParseException, IOException, HttpResponseException {
		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);
		if (data != null && !data.isEmpty()) {
			String paramStr = "";
			for (Map.Entry<String, String> entry : data.entrySet()) {
				paramStr += "&" + entry.getKey() + "=" + entry.getValue();
			}

			if (url.indexOf("?") == -1) {
				url += paramStr.replaceFirst("&", "?");
			} else {
				url += paramStr;
			}
		}

		HttpGet request = new HttpGet(url);

		// 添加Gzip支持
		supportGzip(request);
		
		// 检查是否需要设置代理
		checkProxy(request);
		try {
			return (T) processResponse(httpClient.execute(request));
		} catch (NullPointerException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * 从指定的URL地址获取JSON数据
	 * 
	 * @param url
	 *            目标地址
	 * @param data
	 * 
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String url, Map<String, String> data, Class<T> clazz)
			throws ParseException, IOException, HttpResponseException, JSONException {
		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);
		if (data != null && !data.isEmpty()) {
			String paramStr = "";
			for (Map.Entry<String, String> entry : data.entrySet()) {
				paramStr += "&" + entry.getKey() + "=" + entry.getValue();
			}

			if (url.indexOf("?") == -1) {
				url += paramStr.replaceFirst("&", "?");
			} else {
				url += paramStr;
			}
		}

		HttpGet request = new HttpGet(url);

		// 添加Gzip支持
		supportGzip(request);
		
		// 检查是否需要设置代理
		checkProxy(request);

		try {
			return processResponse(httpClient.execute(request), clazz);
		} catch (NullPointerException e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * 提交表单数据到服务器，并返回JSON对象
	 * 
	 * @param <T>
	 * @param url
	 * @param data
	 * @param clazz
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException
	 */
	public static <T> Collection<T> post(String url, Map<String, String> data,
			 Class<?> collectionClazz,Class<T> clazz) throws ParseException, IOException,
			HttpResponseException, JSONException {
		
		return JSONUtil.parseCollection(post(url,data) + "", collectionClazz, clazz);

	}

	
	/**
	 * 提交表单数据到服务器，并返回对象
	 * 
	 * @param <T>
	 * @param url
	 * @param data
	 * @param clazz
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException
	 */
	public static <T> T post(String url, Map<String, String> data,
			Class<T> clazz) throws ParseException, IOException,
			HttpResponseException, JSONException {
		Log.d(Constant.TAG, "HttpRequestHelper url:" + url);
		Log.d(Constant.TAG, "HttpRequestHelper data:" + data);
		HttpPost request = new HttpPost(url);

		// 添加Gzip支持
		supportGzip(request);
		
		// 检查是否需要设置代理
		checkProxy(request);

		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (entry.getValue() == null)
				continue;

			parameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		try {
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);

			request.setEntity(form);

			return processResponse(httpClient.execute(request), clazz);
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e.getMessage());
		}
	}
	
	//使用get方法返回集合,url中可携带参数
	public static <T> Collection<T> get(String url,Map<String,String> data,
			Class<?> collectionClazz, Class<T> genericType) 
			throws ParseException, JSONException, IOException, HttpResponseException{
		return JSONUtil.parseCollection(get(url,data),collectionClazz, genericType);
	}
	
	//根据url和参数获得返回get方法访问网络的结果
	public static String get(String url,Map<String, String> data) 
			throws ParseException, IOException, HttpResponseException{
		return getString(url, data);
	}
	
	public static String get(String url) throws ParseException, IOException,
			HttpResponseException { // Get方法访问网络
		return getString(url, null);
	}

	public static void post(String path) { // Post方法访问网络

	}

	public static InputStream download(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
	
		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			return is;
		}
		return null;
	}

	/**
	 * 上传的url,f 为要上传的文件
	 * 
	 * @url 上传的目标url
	 * @f 要上传的文件
	 * @return 成功返回true,失败返回false
	 */
	public static boolean postFile(String url, File f) {
		if (url == null || f == null) {
			return false;
		}
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		try {
			FileEntity entity = new FileEntity(f, "binary/octet-stream");
			httpPost.setEntity(entity);
			response = client.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		// 判断上传的状态和打印调试信息
		if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 打印调试信息,上传的url和上传的文件大小
			Log.d(Constant.TAG, "url:" + url + ",file length:" + f.length());
			return true;
		}
		return false;
	}
	
	/**
	 * 上传的url,f 为要上传的文件
	 * 
	 * @url 上传的目标url
	 * @f 	要上传的文件
	 * @return 成功返回true,失败返回false
	 */
	public static boolean postFile(String url, String fileName) {
		String fileFullPath = null;
		if(fileName.endsWith("jpg")){
			//fileFullPath = Constant.IMG_SAVE_LOCA_PATH() + fileName;
		}else{
			if(fileName.endsWith("mp4")){
				//fileFullPath = Constant.VDO_SAVE_LOCA_PATH() + fileName;
			}
		}
		File f = new File(fileFullPath);
		if (url == null || f == null || !f.exists()) {
			return false;
		}
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		try {
			FileEntity entity = new FileEntity(f, "binary/octet-stream");
			httpPost.setEntity(entity);
			response = client.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		// 判断上传的状态和打印调试信息
		if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 打印调试信息,上传的url和上传的文件大小
			
			Log.d(Constant.TAG, "file name :" + fileName + ",url:" + url + ",file length:" + f.length());
			return true;
		}
		return false;
	}
	
	/**
	 * 上传的url,f 为要上传的文件
	 * 
	 * @url 上传的目标url
	 * @f 要上传的文件
	 * @return 成功返回true,失败返回false
	 * @throws HttpResponseException 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	public static  <T> T  postFile(String url, String fileFullPath,Class<T> clazz) throws ParseException, IOException, HttpResponseException, JSONException {
	
		
		File f = new File(fileFullPath);
		if (url == null || f == null || !f.exists()) {
			return null;
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		FileEntity entity = new FileEntity(f, "binary/octet-stream");
		httpPost.setEntity(entity);
		response = client.execute(httpPost);
		
		Log.d(Constant.TAG, "file name :" + fileFullPath + ",url:" + url + ",file length:" + f.length());
		return JSONUtil.parseObject(processResponse(response), clazz);
	}
	
	/**
	 * @param url
	 * @param f
	 * @param clazz
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpResponseException
	 * @throws JSONException
	 */
	public static  <T> T  postFile(String url,File f,Class<T> clazz) throws ParseException, IOException, HttpResponseException, JSONException {
	
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		FileEntity entity = new FileEntity(f, "binary/octet-stream");
		httpPost.setEntity(entity);
		response = client.execute(httpPost);
		
		Log.d(Constant.TAG, "file name :" + f.getAbsolutePath() + ",url:" + url + ",file length:" + f.length());
		return JSONUtil.parseObject(processResponse(response), clazz);
	}
	
	/**
	 * HTTP请求响应异常
	 * 
	 * @author <a href="mailto:Qian.Keane@">keane</a>
	 * @version 1.0
	 */
	public static class HttpResponseException extends Exception {
		private static final long serialVersionUID = 2425069222735716912L;

		private int state;

		public HttpResponseException(int state) {
			super("Wrong HTTP requested that the error status is " + state);
			this.state = state;
		}

		public HttpResponseException(int state, Throwable throwable) {
			super("Wrong HTTP requested that the error status is " + state,
					throwable);
			this.state = state;
		}

		public int getState() {
			return state;
		}
	}

	
	/*
	 * 下载包含中文名字的文件
	 */
	public static InputStream downloadChinaFile(String path) throws Exception {
		if(null == path){
			return null;
		}
		String urlPre = FileUtil.getUrlPre(path);
		String fileName = FileUtil.getFileNameFromUrl(path);
		//String urlStr = urlPre+URLEncoder.encode(fileName,"UTF-8");
		String urlStr = urlPre+new String(fileName.getBytes("GBK"),"UTF-8");
		//ASCII、ISO-8859-1、GB2312、GBK、UTF-8、UTF-16
		urlStr = urlPre+new String(fileName.getBytes("ASCII"),"UTF-8");
		urlStr = urlPre+new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
		urlStr = urlPre+new String(fileName.getBytes("GB2312"),"UTF-8");
		urlStr = urlPre+new String(fileName.getBytes("UTF-8"),"UTF-8");
		urlStr = urlPre+new String(fileName.getBytes("UTF-16"),"UTF-8");
		
		URL url = new URL(urlStr);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);

		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			return is;
		}
		return null;
	}
	
	/**
	 * 检查是否设置代理，当前非WIFI连接时，如果接入点为cmwap、uniwap、ctwap和3gwap，则需要设置代理主机地址（cmwap、
	 * uniwap和3gwap所用代理地址都10.0.0.172:80，ctwap所用代理地址为10.0.0.200：80 ）
	 * 
	 * @param request
	 */
	public static void checkProxy(HttpRequest request) {
		if (isWifi)
			return;

		if (NetType.CMWAP.equals(apnName) || NetType.UNIWAP.equals(apnName)
				|| NetType.GWAP_3.equals(apnName)) {
			HttpHost proxy = new HttpHost(UNIWAP_PROXY_SERVER, 80);
			ConnRouteParams.setDefaultProxy(request.getParams(), proxy);
		} else if (NetType.CTWAP.equals(apnName)) {
			HttpHost proxy = new HttpHost(CTWAP_PROXY_SERVER, 80);
			ConnRouteParams.setDefaultProxy(request.getParams(), proxy);
		}
	}

}
