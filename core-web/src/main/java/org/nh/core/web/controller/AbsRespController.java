package org.nh.core.web.controller;

import lombok.Getter;
import lombok.Setter;
import org.nh.core.web.resp.generator.IDownloadGenerator;
import org.nh.core.web.resp.generator.IResultRespGenerator;
import org.nh.core.web.resp.generator.ISpringResponseGenerator;
import org.nh.core.web.resp.generator.IWsRsResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname AbsRespController
 * @Description TODO 注册实现响应结果生成器
 * @Date 2020/1/15 3:34 PM
 * @Created by nihui
 */
@Getter
@Setter
public abstract class AbsRespController extends AbsBaseController {

    @Autowired(required=false)
    protected IDownloadGenerator downloadGenerator;

    @Autowired(required=false)
    protected IResultRespGenerator resultRespGenerator;

    @Autowired(required=false)
    protected ISpringResponseGenerator springResponseGenerator;

    @Autowired(required=false)
    protected IWsRsResponseGenerator wsrsResponseGenerator;

}
