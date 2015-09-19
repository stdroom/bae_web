/*
 * Copyright (c) 2014, kymjs 张涛 (kymjs123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yyb.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.Vector;


/**
 * 文件与流处理工具�?br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public final class FileUtils {
	
	String LUCY = "index.jsp";
	
	/**
	 * 获取文件扩展�?
	 * added by leixun 2015.01.07
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

    /**
     * 将文件保存到本地
     */
    public static void saveFileCache(byte[] fileData,
            String folderPath, String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
       
            try {
        	 if (!file.exists()) {
        		 file.createNewFile();
        	 }
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeIO(is, os);
            }
    }



    /**
     * 输入流转byte[]<br>
     * 
     * <b>注意</b> 你必须手动关闭参数inStream
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(swapStream);
        }
        return in2b;
    }


    /**
     * 复制文件
     * 
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 快�?复制文件（采用nio操作�?
     * 
     * @param is
     *            数据来源
     * @param os
     *            数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is,
            FileOutputStream os) throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    /**
     * 关闭�?
     * 
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从文件中读取文本
     * 
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return inputStream2String(is);
    }


    /**
     * 输入流转字符�?
     * 
     * @param is
     * @return �?��流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    /**
	 * 根据文件绝对路径获取文件�?
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
			f.delete();
			return true;
		}
		return false;
	}
	
	/*
	expandFileList(String[] files, boolean inclDirs)
	扩展文件列表,以便可以删除不为空的文件夹
	没有修改此方法
	*/
	private static Vector expandFileList(String[] files, boolean inclDirs) {
		Vector v = new Vector();
		if (files == null) 
			return v;
		for (int i = 0; i < files.length; i++)
		//v.add(new File(URLDecoder.decode(files[i])));
			v.add(new File(files[i]));
		for(int i = 0; i < v.size(); i++) {
			File f = (File) v.get(i);
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				for (int n = 0; n < fs.length; n++)
					v.add(fs[n]);
				if (!inclDirs) {
					v.remove(i);
					i--;
				}
			}
		}
		return v;
	}
	/*
	pj_delFile(String[] files),删除文件
	参数String[] files:要删除的文件列表
	*files字符串数组是有form提交的字符串列表,所以files[]都是位于同一目录下的文件,
	*当获得此数组后,交由expandFileList()扩展至每个文件的最地层位置,即遍历files[]中所有文件夹里面的所有文件
	返回:成功与否的提示
	异常捕获:无
	PJ 2008.2
	*/
	private String pj_delFile(String[] files){
		StringBuffer errorInfo=new StringBuffer();
		String resultInfo="";
		boolean error=false;//是否出现错误
		Vector v = expandFileList(files, true);
		int total=0;
		for(int i=v.size()-1;i>=0;i--){
			File f = (File)v.get(i);
			//if (!f.canWrite() || !f.delete()) {
			if (!f.delete()) {
				errorInfo.append("<div class='error'>无法删除:["+f.getAbsolutePath()+"]</div>");
				error=true;
				continue;//继续进行循环
			}
			total++;
		}//end for
		if (error){
			resultInfo=errorInfo.toString()+"<div class='error'>"+total+"个文件已删除,s部分文件无法删除</div>";
		}else{
			resultInfo="<div class='success'>"+total+"个文件已删除</div>";
		}
		return (resultInfo);
	}
	/*
	pj_mvFile(String[] files,String newPath),移动文件
	参数String[] files:要移动的文件列表
	参数String newPath:要移动到的新位置
	返回:成功与否的提示
	异常捕获:NullPointerException(空指向错误),SecurityException(安全管理器阻止了此操作)
	PJ 2008.2
	*/
	private String pj_mvFile(String[] files,String newPath){
		StringBuffer errorInfo=new StringBuffer();
		boolean error=false;//是否出现错误
		String sysInfo="";
		String resultInfo="";
		String fileExistsError="";
		File f_old=null;
		File f_new=null;
		try{
			for (int i=0;i<files.length;i++){
				f_old=new File(files[i]);
				if(!newPath.trim().endsWith(File.separator)) 
					newPath=newPath.trim()+File.separator;
				f_new=new File(newPath.trim()+f_old.getName());
				if(!f_old.renameTo(f_new)){
					errorInfo.append("<div class='error'>无法移动:["+files[i]+"]到["+newPath+"]</div>");
					error=true;
					continue;//继续进行for循环
				}
			}//end for
		}catch(NullPointerException mvFileNPex){
			sysInfo="<div class='error'>NullPointerException:"+mvFileNPex.getMessage()+"</div>";
			error=true;
		}catch(SecurityException mvFileSFex){
			sysInfo="<div class='error'>SecurityException:"+mvFileSFex.getMessage()+"</div>";
			error=true;
		}
		//error
		if(error){
			resultInfo=errorInfo.toString()+"<div class='error'>文件已移动到:["+newPath+"]"
			+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(点击进入)</a>部分文件无法移动</div>";
		}else{
			resultInfo="<div class='success'>所有文件已移动到:["+newPath+"]"
			+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(点击进入)</a></div>";
		}
		return (sysInfo+resultInfo);
	}//end function
	/*
	pj_cpFile(String[] files,String newPath,String path),复制文件
	参数String[] files:要移动的文件列表
	参数String newPath:要移动到的新位置
	参数String path:当前位置
	返回:成功与否的提示
	异常捕获:
	NullPointerException(空指向错误),
	SecurityException(安全管理器阻止了此操作),
	FileNotFoundException
	IOException
	PJ 2008.2
	*/
	private String pj_cpFile(String[] files,String newPath,String path){
		StringBuffer errorInfo=new StringBuffer();
		boolean error=false;//是否出现错误
		String sysInfo="";
		String resultInfo="";
		String fileExistsError="";
		boolean success=false;//是否成功
		int total=0;
		File f_old=null;
		File f_new=null;
		FileInputStream fis=null;
		FileOutputStream fos=null;
		Vector v = expandFileList(files, true);
		byte buffer[] = new byte[0xffff];
		int b;
		try{
		for(int i=0;i<v.size();i++){
		f_old=(File) v.get(i);
		if(!newPath.trim().endsWith(File.separator)) newPath=newPath.trim()+File.separator;
		f_new=new File(newPath.trim()+f_old.getAbsolutePath().substring(path.length()));
		//如果是文件夹直接创建
		if(f_old.isDirectory()){
		f_new.mkdirs();
		total++;
		}else if(f_new.exists()){
		errorInfo.append("不能复制["+f_new.getAbsolutePath()+"],文件已存在");
		error=true;
		continue;//继续进行for循环
		}else{
		fis=new FileInputStream(f_old);
		fos=new FileOutputStream(f_new);
		while ((b = fis.read(buffer)) != -1){
		fos.write(buffer,0,b);
		}
		total++;
		}//end else
		}//end for
		if(fis != null) fis.close();
		if(fos != null) fos.close();
		}catch(NullPointerException cpFileNPex){
		sysInfo="<div class='error'>NullPointerException:"+cpFileNPex.getMessage()+"</div>";
		error=true;
		}catch(FileNotFoundException cpFileFNFex){
		sysInfo="<div class='error'>FileNotFoundException:"+cpFileFNFex.getMessage()+"</div>";
		error=true;
		}catch(SecurityException cpFileSFex){
		sysInfo="<div class='error'>SecurityException:"+cpFileSFex.getMessage()+"</div>";
		error=true;
		}catch(IOException cpFileIOex){
		sysInfo="<div class='error'>IOException:"+cpFileIOex.getMessage()+"</div>";
		error=true;
		}
		//error
		if(error){
		resultInfo=errorInfo.toString()+"<div class='error'>"+total+"个文件已复制到:["+newPath+"]"
		+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(点击进入)</a>部分文件无法复制</div>";
		}else{
		resultInfo="<div class='success'>"+total+"个文件已复制到:["+newPath+"]"
		+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(点击进入)</a></div>";
		}
		return (sysInfo+resultInfo);
	}
}