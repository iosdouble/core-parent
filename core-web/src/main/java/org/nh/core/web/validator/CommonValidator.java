package org.nh.core.web.validator;

import org.apache.commons.lang3.StringUtils;
import org.nh.core.web.contants.Execute;

/**
 *
 * 公用的数据校验及补偿器
 *
 */
public class CommonValidator {
	
	/**
	 *
	 * 检验并补偿execute
	 *
	 * @param execute
	 */
	public static void validExecute(String execute){
		if(StringUtils.isBlank(execute)){
			execute=Execute.BASIC;
		}
		if(!execute.equals(Execute.BASIC) && !execute.equals(Execute.FULL)){
			execute=Execute.BASIC;
		}
	}
}
