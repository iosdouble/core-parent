package org.nh.core.exception.code;

/**
 *
 * 自定义异常code处理标准接口的实现类
 *
 * TODO 在这个类中主要就是申明一些对应服务的异常码
 *
 */
public class ExceptionCodeGeneratorImpl implements IExceptionCodeGenerator {

	private String projectExceptionCode = "00000";
	
	@Override
	public void setProjectExceptionCode(String projectExceptionCode) {
		this.projectExceptionCode = projectExceptionCode;
	}
	
	@Override
	public String getProjectExceptionCode() {
		return projectExceptionCode;
	}

}
