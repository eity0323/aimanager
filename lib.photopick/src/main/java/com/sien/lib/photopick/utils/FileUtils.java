package com.sien.lib.photopick.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件处理类
 */
public class FileUtils {

	private static final String IMAGE_PRE_FILENAME = "mis_";
	private static final String IMAGE_FORMAT = ".jpg";
	private static final String TIME_FORMAT = "yyyyMMddHHmmssSSS";


	private static final String tag = FileUtils.class.getSimpleName();

	private static FileUtils instance;
	/** 缓存路径 **/
	private String rootPath;


	public static File createTmpFile(Context context) {
		return createTmpFile(context, "");
	}

	public static File createTmpFile(Context context, String imageCacheDir) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			if (checkDirExists(imageCacheDir)) {
				StringBuilder sBuilder = new StringBuilder(imageCacheDir);
				String timeStamp = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA).format(new Date());
				sBuilder.append(IMAGE_PRE_FILENAME).append(timeStamp).append(IMAGE_FORMAT);
				File tmpFile = new File(sBuilder.toString());
				return tmpFile;
			} else {
				File pic = Environment.getExternalStorageDirectory();
				String timeStamp = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA).format(new Date());
				String fileName = IMAGE_PRE_FILENAME + timeStamp + "";
				File tmpFile = new File(pic, fileName + IMAGE_FORMAT);
				return tmpFile;
			}
		} else {
			StringBuilder sBuilder = new StringBuilder(imageCacheDir);
			File cacheDir = context.getCacheDir();
			String timeStamp = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA).format(new Date());
			sBuilder.append(IMAGE_PRE_FILENAME).append(timeStamp).append(IMAGE_FORMAT);
			File tmpFile = new File(cacheDir, sBuilder.toString());
			return tmpFile;
		}

	}

	private static boolean checkDirExists(String storageDirPath) {
		if (TextUtils.isEmpty(storageDirPath)) {
			return false;
		}
		File storeageDir = new File(storageDirPath);
		if (!storeageDir.exists()) {
			if (!storeageDir.mkdirs()) {
				return false;
			}
		}
		return true;
	}

	public static String getFormatSize(long size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte(s)";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	/**
	 * 获取FileUtils实例，单例模式实现
	 * 该方法缓存路径为SD卡
	 * @return
	 */
	public static FileUtils getInstance() {
		return getInstance(Environment.getExternalStorageDirectory().getPath());
	}

	/**
	 * 获取FileUtils实例，单例模式实现
	 * 该方法缓存路径为/data/data/cn.xxx.xxx(当前包)/files
	 * @param context
	 * @return
	 */
	public static FileUtils getInstance(Context context) {
		return getInstance(context.getFilesDir().getPath());
	}

	/**
	 * 获取FileUtils实例，单例模式实现
	 * 该方法缓存路径为设置的rootPath
	 * @param rootPath
	 * @return
	 */
	public static FileUtils getInstance(String rootPath) {
		if (instance == null) {
			synchronized (FileUtils.class) {
				if (instance == null) {
					instance = new FileUtils(rootPath);
				}
			}
		}
		return instance;
	}


	/**
	 * @param rootPath
	 */
	public FileUtils(String rootPath) {
		this.rootPath = rootPath;
		if(TextUtils.isEmpty(rootPath)){
			throw new IllegalArgumentException("FileUtils rootPath is not null.");
		}
	}


	/**
	 * 获取保存到本地的资源路径
	 * @param fileName 文件名
	 * @return
	 */
	public String getFilePath(String fileName){
		StringBuilder path = new StringBuilder(rootPath);
		if(!TextUtils.isEmpty(fileName)){
			File file = new File(path.toString());
			if(!file.exists()){
				file.mkdirs();
			}
			path.append(File.separator);
			path.append(fileName);
		}
		Log.e(tag, "" + path);
		return path.toString();
	}


	/**
	 * 判断文件是否存在， true表示存在，false表示
	 * @param fileName 文件名
	 * @return
	 */
	public boolean isFileExits(String fileName){
		File file = new File(getFilePath(fileName));
		if(file.exists()){
			return true;
		}
		return false;
	}

	/**
	 * 保存图片到本地
	 * @param bitmap Bitmap对象
	 * @param fileName 文件名
	 * @return
	 */
	public boolean saveFile(Bitmap bitmap, String fileName) {

		if(bitmap == null) return false;
		OutputStream output = null;

		try{
			File file = new File(getFilePath(fileName));
			if(file.exists()){
				Log.d("FileUtils",fileName + " file already exists.");
				return true;
			}else{
				if(file.createNewFile()){
					output = new FileOutputStream(file);

					Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
					String tempFileName = fileName.toLowerCase(Locale.getDefault());
					if(".jpg".endsWith(tempFileName)){
						format = Bitmap.CompressFormat.JPEG;
					}else if(".png".endsWith(tempFileName)){
						format = Bitmap.CompressFormat.PNG;
					}

					bitmap.compress(format, 100, output);
					output.flush();
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (output != null)output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存字符串到本地
	 * @param content 字符串内容
	 * @param fileName 文件名
	 * @return
	 */
	public boolean saveFile(String content, String fileName) {
		if(!TextUtils.isEmpty(content)){
			return saveFile(content.getBytes(), fileName);
		}
		return false;
	}

	/**
	 * 保存字符串到本地
	 * @param bytes 字符串内容
	 * @param fileName 文件名
	 * @return
	 */
	public boolean saveFile(byte[] bytes, String fileName) {
		FileOutputStream output = null;
		try {
			if(bytes != null){
				File file = new File(rootPath, fileName);
				output = new FileOutputStream(file);
				output.write(bytes);
				output.flush();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(output != null)output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存数据流到本地
	 * @param instream 数据流
	 * @param fileName 文件名
	 * @return
	 */
	public boolean saveFile(InputStream instream, String fileName) {

		File file = new File(rootPath, fileName);
		FileOutputStream buffer = null;

		try {
			if (instream != null) {
				buffer = new FileOutputStream(file);
				byte[] tmp = new byte[1024];
				int length = 0;
				while ((length = instream.read(tmp)) != -1) {
					buffer.write(tmp, 0, length);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(instream != null)instream.close();
				if(buffer != null){
					buffer.flush();
					buffer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 读取文件
	 * @param filePath
	 * @return
	 */
	public String readFile(String filePath){
		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			br.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 从resource的raw中读取文件数据
	 * @param context
	 * @param resId
	 * @return
	 */
	public InputStream getRawStream(Context context, int resId){
		try{
			return context.getResources().openRawResource(resId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从resource的asset中读取文件数据
	 * @param context
	 * @param fileName
	 * @return
	 */
	public InputStream getAssetsStream(Context context, String fileName){
		try{
			return context.getResources().getAssets().open(fileName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将byte[]转换成InputStream
	 *
	 * @param b
	 * @return
	 */
	public InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	/**
	 * InputStream转换成byte[]
	 *
	 * @param instream
	 * @return
	 */
	public byte[] InputStream2Bytes(InputStream instream) {
		byte bytes[] = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] tmp = new byte[1024];
			int length = 0;
			while ((length = instream.read(tmp)) != -1) {
				output.write(tmp, 0, length);
			}
			bytes = output.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null) instream.close();
				if (output != null) output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;

	}

	/**
	 * 数据流转字符串
	 * @param instream
	 * @return
	 * @throws IOException
	 */
	public String inputSteamToString(InputStream instream){
		String result = null;
		try {
			byte bytes[] = InputStream2Bytes(instream);
			result = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 转换文件显示
	 * @param size
	 * @return
	 */
	public static String convertStorage(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	/**
	 * 获取bitmap图片大小
	 * @param bitmap
	 * @return
     */
	public static int getBitmapSize(Bitmap bitmap){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
			return bitmap.getAllocationByteCount();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
	}
}
