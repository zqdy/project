package com.zjgr.fund.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import com.zjgr.fund.Constant;

public class FileUtil {
	private static final String TAG = "FileUtil";
	private static final int THUMBNAIL_WIDTH = 70;
	private static final int THUMBNAIL_HEIGHT = 70;
	private static final int BUFFER_SIZE = 16 * 1024; // 定义文件大小

	/**
	 * 获取图像文件名
	 * 
	 * @return
	 */
	public static String getNewImageFileName() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "capture" + File.separator
				+ System.currentTimeMillis() + "_img.jpg";
	}

	/**
	 * 获取视频文件名
	 * 
	 * @return
	 */
	public static String getNewVideoFileName() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "record" + File.separator
				+ System.currentTimeMillis() + "_media.mp4";
	}

	public static String generateFileName(String pref, String ext) {
		return pref + System.currentTimeMillis() + "." + ext;
	}

	/**
	 * 读取文件的内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String filename) throws Exception {
		// 获得输入流
		// FileInputStream inStream = cxt.openFileInput(filename);
		FileInputStream inStream = new FileInputStream(filename);
		// byte[] b = new byte[inputStream.available()];
		// new一个缓冲区
		byte[] buffer = new byte[1024];
		int len = 0;
		// 使用ByteArrayOutputStream类来处理输出流
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			// 写入数据
			outStream.write(buffer, 0, len);
		}
		// 得到文件的二进制数据
		byte[] data = outStream.toByteArray();
		// 关闭流
		outStream.close();
		inStream.close();
		return new String(data);
	}

	/**
	 * 以默认私有方式保存文件内容至SDCard中
	 * 
	 * @param filename
	 * @param content
	 * @throws Exception
	 */
	public static void saveToSDCard(String fileFullName, String content)
			throws Exception {
		// 通过getExternalStorageDirectory方法获取SDCard的文件路径
		File file = new File(fileFullName);
		// 获取输出流
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/*
	 * 从url中获取文件名 urlString:url地址
	 */
	public static String getFileNameFromUrl(String urlString) {
		String fileName = "";
		if (null == urlString || urlString.equals("")) {
			return fileName;
		}

		int startIndex = urlString.lastIndexOf("=");
		if (-1 != startIndex) {
			// 略去前后的空格
			fileName = urlString.substring(startIndex + 1).trim();
		}

		return fileName;
	}
	
	/*
	 * 从url中获取文件名 urlString:url地址  http://192.168.1.184:8080/upload/app/BusinessDisrict.apk
	 */
	public static String getFileNameByUrl(String urlString) {
		String fileName = "";
		if (null == urlString || urlString.equals("")) {
			return fileName;
		}

		int startIndex = urlString.lastIndexOf("/");
		if (-1 != startIndex) {
			// 略去前后的空格
			fileName = urlString.substring(startIndex + 1).trim();
		}

		return fileName;
	}

	/*
	 * 判断指定路径下的文件是否存在 String path, String fileName
	 */
	public static boolean localFileExists(String path, String fileName) {
		File file;
		if (null == path || null == fileName || path.equals("")
				|| fileName.equals("")) {
			return false;
		}

		file = new File(path + File.separator + fileName);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/*
	 * 从指定的路径中获得url中的文件是否存在 String path String urlString
	 */
	public static Drawable getLocalPic(String path, String urlString) {
		Drawable drawable = null;
		String fileName;
		if (null == urlString || null == path || path.equals("")
				|| urlString.equals("")) {
			return drawable;
		}

		// 获得url中对应的文件名
		fileName = getFileNameFromUrl(urlString);

		// 判断文件是否存在；存在则读取bitmap返回
		if (localFileExists(path, fileName)) {
			drawable = BitmapDrawable.createFromPath(path + File.separator
					+ fileName);
		}
		return drawable;
	}

	/*
	 * 从指定的路径path中，获取url中指定文件 String path String urlString
	 */
	public static File getLocalFile(String path, String urlString) {
		File file = null;
		String fileName;
		if (null == urlString || null == path || path.equals("")
				|| urlString.equals("")) {
			return file;
		}

		// 获得url中对应的文件名
		fileName = getFileNameFromUrl(urlString);

		// 判断文件是否存在；存在则读取bitmap返回
		if (localFileExists(path, fileName)) {
			file = new File(path + File.separator + fileName);
		}
		return file;
	}

	/*
	 * 保存文件 InputStream is:文件输入流 String fullFileName:保存文件的全路径(包括文件名)
	 */
	public static boolean saveFile(InputStream is, String fullFileName) {
		if (null == is || null == fullFileName || fullFileName.equals("")) {
			return false;
		}

		byte buffer[] = new byte[1024];
		try {
			// 文件不存在，创建文件
			File file = new File(fullFileName);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(fullFileName);
			int len = -1;
			while (-1 != (len = is.read(buffer))) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			is.close();
			buffer = null;
			return true;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * 根据输入流保存文件到指定的文件路径下 String srcFileName:原文件名(全路径) ,String path:文件保存路径
	 * String fileName:保存的文件名
	 */
	public static boolean saveFile(String srcFileName, String path,
			String fileName) {
		// 输入参数检查
		if (null == srcFileName || null == path || null == fileName
				|| srcFileName.equals("") || path.equals("")
				|| fileName.equals("")) {
			return false;
		}

		File folder = new File(path);
		InputStream is = null;
		try {
			is = new FileInputStream(srcFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 目录不存在
		if (!folder.exists()) {
			// 创建目录，拷贝文件
			if (folder.mkdirs()) {
				return saveFile(is, path + File.separator + fileName);
			}
			return false;
		}
		// 目录存在
		else {
			return saveFile(is, path + File.separator + fileName);
		}
	}

	/*
	 * 保存bitmap文件到指定的文件中 Bitmap bitmap, String fileFullName（包含路径）
	 */
	public boolean saveFile(Bitmap bitmap, String fileFullName) {
		if (null == bitmap || null == fileFullName || fileFullName.equals("")) {
			return false;
		}

		File file = new File(fileFullName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
				fos.flush();
				fos.close();
				return true;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 判断指定文件路径是否存在，否则创建新的文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExistOrCreateDirectory(String path) {
		boolean isExsit = true;
		File file = new File(path);
		if (!file.isDirectory()) {
			isExsit = file.mkdirs();
			if (!isExsit) {
				isExsit = file.mkdirs();
			}
		}
		return isExsit;
	}

	/**
	 * 判断文件是否已经存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 判断指定文件路径是否存在，否则创建新的文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExistOrCreateDirectory(File file) {
		boolean isExsit = true;
		if (!file.isDirectory()) {
			isExsit = file.mkdirs();
			if (!isExsit) {
				isExsit = file.mkdirs();
			}
		}
		return isExsit;
	}

	
	/**
	 * 根据url下载保存为saveFileName文件
	 * @param url 如：http://192.168.1.184:8080/upload/app/BusinessDisrict.apk
	 * @param saveFileName 完整路径，如：/sdcard/businessdistrict/businessdistrict.apk  
	 */
	public static void downloadFileByUrl(String url, String saveFileName){
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				isExistOrCreateDirectory(saveFileName.substring(0, saveFileName.lastIndexOf("/")));
				File file = new File(saveFileName);
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				int count = 0;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					if (length > 0) {
					}
				}
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 拷贝文件
	 * 
	 * @param src
	 * @param dest
	 */
	public static void copy(File src, File dest) {
		FileChannel in = null;
		FileChannel out = null;
		try {
			in = new FileInputStream(src).getChannel();
			out = new FileOutputStream(dest).getChannel();
			out.transferFrom(in, 0, in.size());
			Log.i(TAG, "copy file success,src:" + src.getAbsolutePath()
					+ ",dest:" + dest.getAbsolutePath());
		} catch (Exception e) {
			Log.e(TAG, "copy file  error.", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void copy(String srcPath, String destPath) throws IOException {
		File src = new File(srcPath);
		if (!src.exists() || !src.isFile()) {
			return;
		}

		File dest = new File(destPath);
		if (!dest.exists()) {
			if (isExistOrCreateDirectory(dest.getParentFile())) {
				dest.createNewFile();
			}
		} else {
			// dest.delete();
			// dest.createNewFile();
		}
		if (dest.exists() && dest.isFile()) {
			copy(src, dest);
		}

	}

	public static void copy2(String srcPath, String destPath)
			throws IOException {
		File src = new File(srcPath);
		if (!src.exists() || !src.isFile()) {
			return;
		}

		File dest = new File(destPath);
		if (!dest.exists()) {
			if (isExistOrCreateDirectory(dest.getParentFile())) {
				dest.createNewFile();
			}
		} else {
			// dest.delete();
			// dest.createNewFile();
		}
		if (dest.exists() && dest.isFile()) {
			try {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new BufferedInputStream(new FileInputStream(src),
							BUFFER_SIZE);
					out = new BufferedOutputStream(new FileOutputStream(dest),
							BUFFER_SIZE);
					byte[] buffer = new byte[BUFFER_SIZE];
					while (in.read(buffer) > 0) {
						out.write(buffer);
					}
				} finally {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * 获得url的前半部分http://172.16.0.254:4040/smarthome-fileserver/upload/
	 * fileAction_downloadWebUploadFile.action?fileObj.fileName=
	 * http://172.16.0.254
	 * :4040/smarthome-fileserver/upload/fileAction_downloadWebUploadFile
	 * .action?fileObj.fileName=最小.jpg
	 */
	public static String getUrlPre(String urlString) {
		String pre = "";
		if (null == urlString || urlString.equals("")) {
			return pre;
		}

		int startIndex = urlString.lastIndexOf("=");
		if (-1 != startIndex) {
			pre = urlString.substring(0, startIndex + 1);
		}

		return pre;
	}

	/**
	 * 获取SDCard路径
	 * 
	 * @return
	 */
	public static String getSDCardDir() {
		File sdCardDir = Environment.getExternalStorageDirectory();
		return sdCardDir.getAbsolutePath();
	}

	/**
	 * 获取项目路径
	 * 
	 * @return
	 */
	public static String getProjectDir() {
		String sdCardDir = getSDCardDir();
		String projectPath = sdCardDir + File.separator + Constant.PROJECT_NAME;
		File projectFile = new File(projectPath);
		if (!projectFile.exists()) {
			projectFile.mkdir();
		}
		return projectPath;
	}

	/**
	 * 获取项目图片路径
	 * 
	 * @return
	 */
	public static String getProjectImageDir() {
		String projectPath = getProjectDir();
		String imagePath = projectPath + File.separator
				+ Constant.PROJECT_IMAGE_NAME;
		File imageFile = new File(imagePath);
		if (!imageFile.exists()) {
			imageFile.mkdir();
		}
		return imagePath;
	}

	/**
	 * 生成一个还后缀的文件名
	 * 
	 * @param suffix
	 *            如：.jpg .gif .png etc
	 * @return
	 */
	public static String generateFileName(String suffix) {
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase()
				+ suffix;
	}

	/**
	 * 生成一个还后缀的文件名
	 * 
	 * @param suffix
	 *            如：.jpg .gif .png etc
	 * @return
	 */
	public static String generateDefualtImageName() {
		return generateFileName(".jpg");
	}

	public static String getFileNameByPath(String path) {
		if (path == null || "".equals(path.trim())) {
			return path;
		}
		String subPath = path.substring(path.lastIndexOf(File.separator) + 1);
		return subPath;
	}

	public static String getFileSuffixByPath(String filePath) {
		if (filePath == null || "".equals(filePath.trim())) {
			return filePath;
		}
		String suffix = filePath.substring(filePath.lastIndexOf("."));
		return suffix;
	}

	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}

	public static Bitmap getBitmapByPath(String filePath, int imageWidth,
			int imageHeight) {
		try {
			File file = new File(filePath);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			Bitmap bmap = BitmapFactory.decodeStream(bis, null, opt);
			Bitmap thbm = ThumbnailUtils.extractThumbnail(bmap, imageWidth,
					imageHeight);
			bis.close();
			return thbm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
