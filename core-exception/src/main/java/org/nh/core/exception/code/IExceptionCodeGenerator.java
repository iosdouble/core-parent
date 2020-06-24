package org.nh.core.exception.code;

/**
 *
 * 自定义异常code处理标准接口
 *
 */
public interface IExceptionCodeGenerator {
	
	/**
	 *
	 * 设置服务的异常code
	 * @param projectExceptionCode
	 */
	public void setProjectExceptionCode(String projectExceptionCode);
	
	/**
	 * 获取服务的异常code
	 * @return
	 */
	public String getProjectExceptionCode();
}
