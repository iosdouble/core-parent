package org.nh.core.web.resp;


import org.nh.core.util.json.JsonUtil;

/**
 * @class: AbsJsonResp
 * @description: TODO Response抽象基础类，继承了序列化接口，同时显示了toJson方法
 * @date 2020/1/15 3:55 PM
**/
public abstract class AbsJsonResp implements IResp {

	private static final long serialVersionUID = 3451833393183790558L;

	public String toJson() {
		return JsonUtil.toJsonAndLongToString(this);
	}

	/**
	 *
	 * 重写toString方法，返回json
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return this.toJson();
	}
	
	
}
