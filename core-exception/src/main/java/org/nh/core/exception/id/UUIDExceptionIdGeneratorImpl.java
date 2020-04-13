package org.nh.core.exception.id;


import org.nh.core.util.id.UUIDUtil;

/**
 *
 * 基于UUID生成errorId的实现类
 * UUID不能保证分布式时的唯一性
 *
 */
public class UUIDExceptionIdGeneratorImpl implements IExceptionIdGenerator {

	@Override
	public String getId() {
		String id=UUIDUtil.getId();
		return id;
	}

}
