package org.nh.core.web.resp.generator;

import org.nh.core.web.resp.ResultResp;

/**
 * @class: IResultRespGenerator
 * @description: TODO 自定义的Json返回结果的实现标准接口
 * @date 2020/1/15 3:45 PM
**/
public interface IResultRespGenerator {
	
	/**
	 * 根据异常和数据组装成返回自定义的json结果
	 * @param response 没有结果则填写null
	 * @return
	 */
	public <T> ResultResp<T> doResultResp(Throwable throwable, T response);

	public <T> ResultResp<T> doResultResp(Throwable throwable);
	public <T> ResultResp<T> doResultResp(T response);
	
}
