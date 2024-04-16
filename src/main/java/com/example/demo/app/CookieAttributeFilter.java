
package com.example.demo.app;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
 
/**
 * 저장된 쿠키에서 Secure, SameSite=None 옵션 추가하기 필터.
 * APP_NAME 은 기존에 옵션이 없는 형태로 기본 추가되므로 예외 처리.
 * SESSION 으로 ID 값이 있는데 해당 명을 사용하여 구분하지는 않았다.
 *
 * @author kjk
 */
@Slf4j 
@Component
public class CookieAttributeFilter implements Filter {
 
    @SuppressWarnings("deprecation")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("doFilter");
        log.debug("request.getRealPath(null)", request.getRealPath(null));
         
        // true error
        // false is no error
        boolean isTest = true;
        boolean isTest500 = false;
        boolean isTestSleep3000 = true;
         
        if ( isTest ) {
            if ( isTest500 ) {
                throw new RuntimeException("500 error");
            }
            try {
                if ( isTestSleep3000 ) {
                    Thread.sleep(11_000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }
 
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
// 
//    @Override
//    public void destroy() {
//    }
}
