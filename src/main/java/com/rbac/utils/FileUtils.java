package com.rbac.utils;


import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件工具类
 */
public class FileUtils {
    private static DecimalFormat decimalFormat=(DecimalFormat)NumberFormat.getInstance();
    static{
        decimalFormat.setMaximumFractionDigits(2);
    }
 
	/**
	 * 对路径作标准化处理，把windows下面的\格式的路径分割符处理成统一的/
	 * @param path
	 * @return
	 */
	public static String standardPath(String path){
		if(path!=null){
			return path.replaceAll("\\\\", "/");
		}else{
			return path;
		}
	}
	/**
	 * 如果文件不存在，则创建新文件
	 * @param file
	 * @throws IOException
	 */
	public static void createNewFile(File file) throws IOException {
		if(!file.exists()){
			File parentFile = file.getParentFile();
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			file.createNewFile();
		}
	}
	
	public static long getFileSize(File file){
	    FileChannel channel = null;
	    if(file.exists() && file.isFile()){
	        FileInputStream inputStream = null;
	        try {
	            inputStream = new FileInputStream(file);
                channel = inputStream.getChannel();
                return channel.size();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                throw new RuntimeException("读取文件出错");
            }finally{
                FileUtils.close(inputStream);
                FileUtils.close(channel);
            }
	    }
	    return 0L;
	}
	
	public static boolean deleteFile(File file){
		String fileFullPath;
		try {
			fileFullPath = file.getCanonicalPath();
		} catch (IOException e) {
			fileFullPath = e.getMessage();
		}
		
		boolean r = file.delete();
		return r;
	}
	/**
	 * 文件复制
	 * @param src
	 * @param dsc
	 * @param autoCreateDscDir
	 * @throws IOException
	 */
	public static void copy(File src,File dsc,boolean autoCreateDscDir) throws IOException{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcIn = null;
		FileChannel fcOut = null;
		
		try {
			if(autoCreateDscDir){
				File parentFile = dsc.getParentFile();
				if(!parentFile.exists()){
					parentFile.mkdirs();
				}
			}
			fis = new FileInputStream(src);
			fos = new FileOutputStream(dsc);
			fcIn = fis.getChannel();
			fcOut = fos.getChannel();
			
			fcIn.transferTo(0, fcIn.size(), fcOut);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			if(fis!=null)fis.close();
			if(fcIn!=null)fcIn.close();
			if(fos!=null)fos.close();
			if(fcOut!=null)fcOut.close();
		}
	}
	
	public static InputStream openFile(File file){
	    InputStream inputStream = null; 
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            close(inputStream);
        }
        return inputStream;
	}
	
	public static InputStream openFile(String filePath){
	    return openFile(new File(filePath));
	}
	
	/**
	 * 写文件
	 * @param file
	 * @param inputStream
	 * @throws IOException 
	 */
	public static void writeFile(InputStream inputStream,File dscFile,boolean autoCreateDscDir) throws IOException {
		if(autoCreateDscDir)createNewFile(dscFile);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(new FileOutputStream(dscFile));
			
			int len ;
			byte[] buffer = new byte[1024*4];
			while((len=bis.read(buffer)) != -1){
				bos.write(buffer,0,len);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			FileUtils.close(bos);
			FileUtils.close(bis);
		}
	}
	
	public static void close(Closeable stream){
	       try {
	            if (stream != null) stream.close();
	        } catch (IOException e) {
	        }
	}
	
	/**
	 * 获取文件的扩展名
	 * @param file
	 * @return
	 */
	public static String getFileSuffix(String file){
		String suffix = "";
		if(StringUtils.isBlank(file)) return suffix;
		String[] ss = file.split("\\.");
		if(ss.length>1){
			suffix = ss[ss.length-1];
		}
		return suffix;
	}
	
    public  static String convertSizeText(Long size) {
        if (size == null || size == 0) return "0K";
        String text = "";
        Double size1 = size.doubleValue();
        text = decimalFormat.format(size1) + "B";
        int mulNumber = 1024;
        if (size1 > mulNumber) {
            size1 = size1 / mulNumber;
            text = decimalFormat.format(size1) + "K";
        }
        if (size1 > mulNumber) {
            size1 = size1 / mulNumber;
            text = decimalFormat.format(size1) + "M";
        }
        if (size1 > mulNumber) {
            size1 = size1 / mulNumber;
            text = decimalFormat.format(size1) + "G";
        }
        return text;
    }

	/**
	 * 删除文件，以及目录
	 * @param file
	 */
	public static void delete(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f:files){
				delete(f);
			}
		}
		file.delete();
	}
	
	public static void writeStringToFile(File file,String str,String charset) throws IOException{
	    org.apache.commons.io.FileUtils.writeStringToFile(file, str, charset);
	}
	public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception
	{
		if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath) || StringUtils.isEmpty(zipFileName))
		{
		}
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;						
		try
		{
			File srcFile = new File(srcPath);
			
			//判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			if (srcFile.isDirectory() && zipPath.indexOf(srcPath)!=-1) 
			{
			}
			
			//判断压缩文件保存的路径是否存在，如果不存在，则创建目录
			File zipDir = new File(zipPath);
			if (!zipDir.exists() || !zipDir.isDirectory())
			{
				zipDir.mkdirs();
			}
			
			//创建压缩文件保存的文件对象
			String zipFilePath = zipPath + File.separator + zipFileName;
			File zipFile = new File(zipFilePath);			
			if (zipFile.exists())
			{
				//检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(zipFilePath);
				//删除已存在的目标文件
				zipFile.delete();				
			}
			
			cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			zos = new ZipOutputStream(cos);
			
			//如果只是压缩一个文件，则需要截取该文件的父目录
			String srcRootDir = srcPath;
			if (srcFile.isFile())
			{
				int index = srcPath.lastIndexOf(File.separator);
				if (index != -1)
				{
					srcRootDir = srcPath.substring(0, index);
				}
			}
			//调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, zos);
			zos.flush();
		}
		catch (Exception e) 
		{
			throw e;
		}
		finally 
		{			
			try
			{
				if (zos != null)
				{
					zos.close();
				}				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}			
		}
	}
	private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception
	{
		if (file == null) 
		{
			return;
		}				
		
		//如果是文件，则直接压缩该文件
		if (file.isFile())
		{			
			int count, bufferLen = 1024;
			byte data[] = new byte[bufferLen];
			
			//获取文件相对于压缩文件夹根目录的子路径
			String subPath = file.getAbsolutePath();
			int index = subPath.indexOf(srcRootDir);
			if (index != -1) 
			{
				subPath = subPath.substring(srcRootDir.length() + File.separator.length());
			}
			ZipEntry entry = new ZipEntry(subPath);
			zos.putNextEntry(entry);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			while ((count = bis.read(data, 0, bufferLen)) != -1) 
			{
				zos.write(data, 0, count);
			}
			bis.close();
			zos.closeEntry();
		}
		//如果是目录，则压缩整个目录
		else 
		{
			//压缩目录中的文件或子目录
			File[] childFileList = file.listFiles();
			for (int n=0; n<childFileList.length; n++)
			{
				childFileList[n].getAbsolutePath().indexOf(file.getAbsolutePath());
				zip(srcRootDir, childFileList[n], zos);
			}
		}
	}
	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * @param sourceFilePath :待压缩的文件路径
	 * @param zipFilePath :压缩后存放路径
	 * @param fileName :压缩后文件的名称
	 * @return
	 */
	public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		if(sourceFile.exists() == false){
			System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
		}else{
			try {
				File zipFile = new File(zipFilePath + "/" + fileName +".zip");
				if(zipFile.exists()){
					System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
				}else{
					File[] sourceFiles = sourceFile.listFiles();
					if(null == sourceFiles || sourceFiles.length<1){
						System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
					}else{
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024*10];
						for(int i=0;i<sourceFiles.length;i++){
							//创建ZIP实体，并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							//读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024*10);
							int read = 0;
							while((read=bis.read(bufs, 0, 1024*10)) != -1){
								zos.write(bufs,0,read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally{
				//关闭流
				try {
					if(null != bis) bis.close();
					if(null != zos) zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}

	/**
	 * 把流输出到响应流中
	 * @param response
	 * @param inputStream
	 * @param contentType
	 * @param responseHeaders
	 */
	public static void renderStream(HttpServletResponse response, InputStream inputStream, String contentType, Map<String, String> headers) {
		response.reset();
		response.setContentType(contentType);
		//修改HTTP协议头
		if(headers!=null){
			Set<Entry<String, String>> entries = headers.entrySet();
			for(Entry<String, String> entry : entries){
				response.addHeader(entry.getKey(), entry.getValue());
			}
		}

		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			byte[] buffer = new byte[10240];
			int len = 0;
			while((len=inputStream.read(buffer)) > 0 ){
				outputStream.write(buffer, 0, len);
			}
			response.flushBuffer();
			outputStream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			FileUtils.close(inputStream);
			FileUtils.close(outputStream);
		}
	}

	public static String iso8859(String str, HttpServletRequest request){
		String charset = request.getCharacterEncoding();
		if(charset==null)charset = Charset.defaultCharset().toString();
		try {
			return new String(str.getBytes(charset),"iso8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
