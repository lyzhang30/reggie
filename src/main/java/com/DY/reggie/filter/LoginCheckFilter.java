package com.DY.reggie.filter;

import com.DY.reggie.common.BaseContext;
import com.DY.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 * @author 大勇
 */
@Slf4j
@WebFilter(filterName ="LoginCheckFilter",urlPatterns="/*")
public class LoginCheckFilter implements Filter {
    //路径通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest ServletRequest,ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) ServletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次登录的URL
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        //定义不需要处理的请求路径
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/common/**",
                "/user/**",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs",
                "/swagger-ui.html"
        };
        //判断当前请求是否需要处理
        boolean check = check(urls, requestURI);
        //不需要处理直接放行
        if(check){
            log.info("不需要处理的请求：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //如果已登录的话，就直接放行
        if(request.getSession().getAttribute("employee") !=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id:{}",request.getSession().getAttribute("user"));

            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return ;
        }
        log.info("用户未登录");
        //未登录的话，返回未登录，通过输出流方式向页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 判断是否匹配
     * @param patternUrl url格式
     * @param requestUrl 请求url
     * @return 是否匹配
     */
    public boolean check(String[] patternUrl, String requestUrl) {
        for(String url :patternUrl){

            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                return true;
            }
        }
        return false;
    }

}
