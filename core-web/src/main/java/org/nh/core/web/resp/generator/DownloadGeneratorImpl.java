package org.nh.core.web.resp.generator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.nh.core.util.string.CharsetContants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @class: DownloadGeneratorImpl
 * @description: TODO 输出（下载）的标准接口的标准实现类
 * @date 2020/1/15 3:44 PM
**/
public class DownloadGeneratorImpl implements IDownloadGenerator {

	@Override
	public ResponseEntity<?> downloadFile(File file, String fileName) throws IOException {
		String downloadFileName = new String(fileName.getBytes(CharsetContants.UTF8), CharsetContants.ISO88591);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", downloadFileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> downloadFile(ByteArrayOutputStream byteArrayOutputStream, String fileName,
                                          String charset) throws IOException {
		String downloadFileName = new String(fileName.getBytes(CharsetContants.UTF8), CharsetContants.ISO88591);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", downloadFileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		byte[] baosStrByte=byteArrayOutputStream.toByteArray();
		String baosStr=byteArrayOutputStream.toString(charset);
		byte[] baosStrByte=baosStr.getBytes(charset);
		
		return new ResponseEntity<byte[]>(baosStrByte, headers, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> downloadFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) throws IOException {
		return downloadFile(byteArrayOutputStream, fileName, CharsetContants.GB2312);
	}

	@Override
	public void downloadStream(InputStream inputStream, HttpServletResponse httpServletResponse) {
		try(
			OutputStream outputStream=httpServletResponse.getOutputStream();
			InputStream inputStreamCopy = inputStream;
		) {
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
