package org.nh.core.web.resp.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.nh.core.util.string.CharsetContants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @class: DownloadUtil
 * @description: TODO 用于给客户端提供下载文件的工具
 * @date 2020/1/15 3:50 PM
**/
public class DownloadUtil {
	/**
	 *
	 * 用于提供下载文件的服务类使用，使其能够提供给客户端传输文件二进制数据的能力
	 * @param file 需要给客户端发送的文件对象
	 * @param fileName 需要给客户端展示的文件名称
	 * @return
	 * @throws IOException
	 */
	public static ResponseEntity<?> downloadFile(File file, String fileName) throws IOException {
		String downloadFileName = new String(fileName.getBytes(CharsetContants.UTF8), CharsetContants.ISO88591);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", downloadFileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
	}	
	
	/**
	 * 用于提供下载文件的服务类使用，使其能够提供给客户端传输文件二进制数据的能力
	 * @param byteArrayOutputStream 需要给客户端发送的文件对象的
	 * @param fileName 需要给客户端展示的文件名称
	 * @param charset 字符集名称
	 * @return
	 * @throws IOException
	 */
	public static ResponseEntity<?> downloadFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String charset) throws IOException {
		String downloadFileName = new String(fileName.getBytes(CharsetContants.UTF8), CharsetContants.ISO88591);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", downloadFileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		byte[] baosStrByte=byteArrayOutputStream.toByteArray();
		String baosStr=byteArrayOutputStream.toString(charset);
		byte[] baosStrByte=baosStr.getBytes(charset);
		
		return new ResponseEntity<byte[]>(baosStrByte, headers, HttpStatus.CREATED);
	}
	
	/**
	 *
	 * 用于提供下载文件的服务类使用，使其能够提供给客户端传输文件二进制数据的能力
	 * 默认采用GB2312
	 * @param byteArrayOutputStream 需要给客户端发送的文件对象的
	 * @param fileName 需要给客户端展示的文件名称
	 * @return
	 * @throws IOException
	 */
	public static ResponseEntity<?> downloadFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) throws IOException {
		return downloadFile(byteArrayOutputStream, fileName, CharsetContants.GB2312);
	}
	
	/**
	 * 基于Stream的下载
	 * @param inputStream
	 * @param httpServletResponse
	 * @throws IOException
	 */
	public static void downloadStream(InputStream inputStream,HttpServletResponse httpServletResponse) throws IOException {
		ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();
		IOUtils.copy(inputStream, servletOutputStream);
		inputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
	}
}
