package org.nh.core.exception.id;

import org.nh.core.util.id.SnowflakeUtil;

/**
 * 基于Snowflake生成errorid的实现类
 * 该类生成的errorid可以保证全局唯一
 *
 */
public class SnowflakeExceptionIdGeneratorImpl implements IExceptionIdGenerator {

	@Override
	public String getId() {
		long id = SnowflakeUtil.getId();
		return String.valueOf(id);
	}

}
