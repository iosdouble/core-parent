package org.nh.core.web.resp;

import java.io.Serializable;

/**
 *
 * Response对象的基础接口，继承了序列化接口
 *
 */
public interface IResp extends Serializable {
	/**
	 *
	 * 将本对象json化
	 * @return
	 */
	public String toJson();
}
