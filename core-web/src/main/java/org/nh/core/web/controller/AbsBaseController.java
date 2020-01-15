package org.nh.core.web.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Classname AbsBaseController
 * @Description TODO
 * @Date 2020/1/15 3:27 PM
 * @Created by nihui
 */
@Getter
@Setter
public abstract class AbsBaseController {

    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    @Autowired
    protected HttpSession httpSession;
}
