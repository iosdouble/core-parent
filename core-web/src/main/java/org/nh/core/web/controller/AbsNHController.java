package org.nh.core.web.controller;

import org.nh.core.web.req.util.AjaxUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Classname AbsNHController
 * @Description TODO
 * @Date 2020/1/15 3:28 PM
 * @Created by nihui
 */
public abstract class AbsNHController extends AbsRespController {
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public Object doException(Exception exception,WebRequest webRequest){

        if(AjaxUtil.isAjaxRequest(webRequest)) {
            return this.resultRespGenerator.doResultResp(exception);
        }else {
            exception.printStackTrace();
            ModelAndView modelAndView=new ModelAndView("error/500");
            modelAndView.addObject("errorMessage", exception);
            return modelAndView;
        }

    }
}
