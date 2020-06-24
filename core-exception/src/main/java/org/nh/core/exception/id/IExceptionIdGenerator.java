package org.nh.core.exception.id;

/**
 *
 * 获取ErrorId的Generator标准
 *
 * 这里在生成异常的ID的时候，通常使用到的方法是UUID的方式，
 * 在分布式系统中，UUID会有重复的地方通过实现这个ID生成器
 * 来生成一个指定的异常码，这个异常码就可以实现Exception的
 * 精准定位
 *
 * @see SnowflakeExceptionIdGeneratorImpl 基于雪花算法的ID生成规则
 * @see UUIDExceptionIdGeneratorImpl 基于UUID的生成规则
 *
 */
public interface IExceptionIdGenerator {
	public String getId();
}
