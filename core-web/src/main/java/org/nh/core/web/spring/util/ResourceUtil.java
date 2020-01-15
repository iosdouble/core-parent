package org.nh.core.web.spring.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @class: ResourceUtil
 * @description: TODO 用于处理org.springframework.core.io.Resource的工具
 * @date 2020/1/15 3:56 PM
**/
public class ResourceUtil {
	/**
	 *
	 * 从指定的文件描述路径符获取Resource[]
	 * @param locations 指定的文件描述路径符
	 * 例如：classpath*:com/baidu/fsg/uid/worker/xml/*.xml（读取jar中的文件需要使用classpath*）
	 * @return Resource[]
	 */
	public static Resource[] resolveLocationsToResource(String[] locations) {
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = new ArrayList<Resource>();
		
		if (locations != null) {
			for (String location : locations) {
				try {
					Resource[] mappers = resourceResolver.getResources(location);
					resources.addAll(Arrays.asList(mappers));
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return resources.toArray(new Resource[resources.size()]);
	}
}
