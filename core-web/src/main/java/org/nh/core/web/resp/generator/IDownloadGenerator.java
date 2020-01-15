package org.nh.core.web.resp.generator;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * @class: IDownloadGenerator
 * @description: TODO 输出（下载）的标准接口
 * @date 2020/1/15 3:45 PM
**/
public interface IDownloadGenerator {

	/**
	 *
	 * 用于提供下载文件的服务类使用，使其能够提供给客户端传输文件二进制数据的能力
	 * @param file 需要给客户端发送的文件对象
	 * @param fileName 需要给客户端展示的文件名称
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<?> downloadFile(File file, String fileName) throws IOException;
	
	/**
	 *
	 * 用于提供下载文件的服务类使用，使其能够提供给客户端传输文件二进制数据的能力
	 * @param byteArrayOutputStream 需要给客户端发送的文件对象
	 * @param fileName 需要给客户端展示的文件名称
	 * @param charset 字符集名称{@code CharsetContants}
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<?> downloadFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String charset) throws IOException;

	/**
	 *
	 * 用于提供下载文件的服务类使用，使其能够提供给客户端传输文件二进制数据的能力
	 * @param byteArrayOutputStream 需要给客户端发送的文件对象
	 * @param fileName 需要给客户端展示的文件名称
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<?> downloadFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) throws IOException;

	/**
	 *
	 * 用于提供下载二进制流的能力（将输入流直接作为输出流）
	 * @param inputStream
	 * @param httpServletResponse
	 * @throws IOException
	 */
	public void downloadStream(InputStream inputStream, HttpServletResponse httpServletResponse) throws IOException;
	
}
