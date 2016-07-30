
package com.example.duanxudong.myapplication.hanzibihua.utils;

import android.content.Context;
import android.os.Environment;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {

	private static FileUtils instance;

	public static FileUtils getInstance() {
		if (instance == null) {
			instance = new FileUtils();
		}
		return instance;
	}

	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils() {
		// 得到当前外部存储设备的目/SDCARD )
		File sdCard = Environment.getExternalStorageDirectory();
		SDPATH = sdCard.getAbsolutePath() + "/";
		//Log.d("FileUtils", "FileUtils.SDPATH=" + SDPATH);
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		// 断点续传要放�?��处，否则创建的都是新文件
		// if(!file.exists()){
		file.createNewFile();
		// }
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {

		File dir = null;

		if (dirName == null) {
			dirName = "";
		}
		
		dir = new File(SDPATH + dirName);
		dir.mkdir();

		return dir;
	}
	
	public String getPath(String dirName){
		File dir=null;
		if(!isExist(dirName)){
			dir=createSDDir(dirName);
			return dir.getPath();
		}else{
			return SDPATH +"/"+ dirName;
		}
		
	}

	/**
	 * 判断SD卡上的文件是否存�?
	 * 
	 * @param fileName
	 * @return
	 */
	public File isFileExist(String fileName) {

		File file = new File(SDPATH + fileName);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}

	}

	/**
	 * 判断SD卡上的文件是否存�?
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isExist(String fileName) {

		File file = new File(SDPATH + fileName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 *            创建目录
	 * @param fileName
	 *            创建的文件名
	 * @param input
	 *            输入�?
	 * @return
	 */
	public File writeFile2SDFromInput(String path, String fileName,
									  InputStream input) {
		File file = null;
		FileOutputStream output = null;
		try {
			File dir = createSDDir(path);
			file = createSDFile(dir.getPath() + "/" + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			do {
				int numread = input.read(buffer);
				if (numread <= 0) {
					break;
				}
				output.write(buffer, 0, numread);
			} while (true);
			input.close();
		} catch (Exception e) {
			file = null;
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将字节流转INT
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte[] b) {

		int s = 0;

		int s0 = b[0] & 0xff;// �?���?

		int s1 = b[1] & 0xff;

		int s2 = b[2] & 0xff;

		int s3 = b[3] & 0xff;

		s3 <<= 24;

		s2 <<= 16;

		s1 <<= 8;

		s = s0 | s1 | s2 | s3;

		return s;
	}

	// byte数组转成long
	public static long byteToLong(byte[] bb) {

		return ((((long) bb[0] & 0xff) << 56) | (((long) bb[1] & 0xff) << 48)
				| (((long) bb[2] & 0xff) << 40) | (((long) bb[3] & 0xff) << 32)
				| (((long) bb[4] & 0xff) << 24) | (((long) bb[5] & 0xff) << 16)
				| (((long) bb[6] & 0xff) << 8) | (((long) bb[7] & 0xff) << 0));
	}

	/**
	 * 将long转字节流
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] longToByte(long number) {

		long temp = number;

		byte[] b = new byte[8];

		for (int i = 0; i < b.length; i++) {

			b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位

			temp = temp >> 8; // 向右�?�?

		}

		return b;

	}

	/**
	 * byte(字节)根据长度转成kb(千字�?和mb(兆字�?
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		if (returnValue > 1) {
			String mb = "" + returnValue;
			String dot = mb.substring(mb.indexOf(".") + 1, mb.length());
			if (dot.length() > 3) {
				mb = mb.substring(0, mb.indexOf(".") + 3);
			}
			return (mb + "MB");
		}
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		String kb = "" + returnValue;
		String dot = kb.substring(kb.indexOf(".") + 1, kb.length());
		if (dot.length() > 3) {
			kb = kb.substring(0, kb.indexOf(".") + 3);
		}

		return (kb + "KB");
	}

	// /**
	// * 读取SD卡某个文件内容，仅限TXT
	// * @param path SD卡上创建的目�?
	// * @param fileName 文件�?
	// * @return
	// */
	// public String readFromSD(String path,String fileName){
	//
	// StringBuffer sb = new StringBuffer();
	//
	// File f = new File(SDPATH + path + fileName);//这是对应文件路径
	//
	// InputStream in = null;
	//
	// try {
	// in = new BufferedInputStream(new FileInputStream(f));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// return "";
	// }
	//
	// BufferedReader br = null;
	//
	// try {
	// br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// return "";
	// }
	//
	// String tmp;
	//
	// try {
	// while((tmp=br.readLine())!=null){
	// sb.append(tmp);
	// }
	// br.close();
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return "";
	// }
	// return sb.toString();
	// }

	/**
	 * 删除当前下载临时文件
	 */
	public void delFile(String localPath) {
		File myFile = new File(localPath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * 删除文件夹所有内�?
	 * 
	 */
	public void deleteDirectoryandFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文�?
				file.delete(); // delete()方法 你应该知�?是删除的意�?;
			} else if (file.isDirectory()) { // 否则如果它是�?��目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteDirectoryandFile(files[i]); // 把每个文�?用这个方法进行迭�?
				}
			}
			file.delete();
		}
	}

	// /**
	// * 获取文件类型
	// * @param f
	// * @return
	// */
	// public static String getMIMEType(File f){
	// String type = "";
	// String fName = f.getName();
	// String end = fName.substring(fName.lastIndexOf(".") + 1,
	// fName.length()).toLowerCase();
	// if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
	// || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
	// type = "audio";
	// } else if (end.equals("3gp") || end.equals("mp4")) {
	// type = "video";
	// } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
	// end.equals("jpeg") || end.equals("bmp")) {
	// type = "image";
	// } else if (end.equals("apk")) {
	// type = "application/vnd.android.package-archive";
	// } else {
	// type = "*";
	// }
	// if (end.equals("apk")){
	//
	// } else {
	// type += "/*";
	// }
	// return type;
	// }

	/**
	 * 根据URL得到输入�?
	 * 
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			inputStream = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * 根据URL下载文件,前提是这个文件当中的内容是文�?函数的返回�?就是文本当中的内�?1.创建�?��URL对象
	 * 2.通过URL对象,创建�?��HttpURLConnection对象 3.得到InputStream 4.从InputStream当中读取数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public String downloadTxT(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			//Log.e("FileUtils", "FileUtils.downloadTxT|Exception", e);
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				//Log.e("FileUtils", "FileUtils.downloadTxT|IOException", e);
			}
		}
		return sb.toString();
	}

	// 复制文件
	public void copyFile(File sourceFile, File targetFile) {
		try {
			FileInputStream input = new FileInputStream(sourceFile); // 新建文件输入流并对它进行缓冲

			BufferedInputStream inBuff = new BufferedInputStream(input);

			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);

			// 缓冲数组
			byte[] b = new byte[1024];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出�?
			outBuff.flush();

			// 关闭�?
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取本地文件大小
	public long readFileSize(String filename) {

		long size = 0;

		File file = new File(SDPATH + filename);

		if (file.exists()) {
			size = file.length();
		}
		return size;

	}

	/**
	 * 判断SDCard是否存在
	 * 
	 * @return
	 */
	public boolean hasSDCard() {
		boolean b = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			b = true;
		}
		return b;
	}

	/**
	 * 获得sdcard路径
	 * 
	 * @return
	 */
	public String getExtPath() {
		String path = "";
		if (hasSDCard()) {
			path = Environment.getExternalStorageDirectory().getPath();
		}
		return path;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public String getPackagePath(Context context) {
		return context.getFilesDir().toString();
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFileName
	 *            源文件完整路�?
	 * @param destDirName
	 *            目的目录完整路径
	 * @return 文件移动成功返回true，否则返回false
	 */
	public boolean moveFile(String srcFileName, String destDirName) {

		File srcFile = new File(srcFileName);
		if (!srcFile.exists() || !srcFile.isFile())
			return false;

		File destDir = new File(destDirName);
		if (!destDir.exists())
			destDir.mkdirs();

		return srcFile.renameTo(new File(destDirName + File.separator
				+ srcFile.getName()));
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路�?如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路�?如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在�?
				InputStream inStream = new FileInputStream(oldPath); // 读入原文�?
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节�?文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 根据路径获取图片名称
	 * 
	 * @param url
	 * @return
	 */
	public String getFileName(String url) {
		String imageName = "";
		if (url != null) {
			imageName = url.substring(url.lastIndexOf("/") + 1);
		}
		return imageName;
	}

	/***
	 * 读取asets文件内容
	 * @param context
	 * @param url
	 * @return
	 */
	public String getAssets(Context context, String url) {
		String line = "";
		String Result = "";
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {

			inputStream = context.getResources().getAssets().open(url);
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((line = bufferedReader.readLine()) != null) {
				Result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e2) {
			}
		}
		return Result;
	}

	// 从assets 文件夹中获取文件并读取数�?
	public String getFromAssets(Context context, String fileName) {
		String result = "";
		InputStream inputStream = null;
		try {
//			inputStream = context.getResources().getAssets().open(fileName);
			inputStream = new FileInputStream( fileName);


			// 获取文件的字节数
			int lenght = inputStream.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组�?
			inputStream.read(buffer);
//			result = EncodingUtils.getString(buffer, "UTF-8");
			result = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}