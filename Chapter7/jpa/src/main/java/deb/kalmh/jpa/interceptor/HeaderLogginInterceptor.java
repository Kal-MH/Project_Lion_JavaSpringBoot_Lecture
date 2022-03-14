package deb.kalmh.jpa.interceptor;

import deb.kalmh.jpa.filter.TransactionLogFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;

@Component
public class HeaderLogginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);

    //컨트롤러로 넘어가기 전에 preHandle()함수가 호출
    // - 주로 세션 id확인 등, 세션관리가 이뤄짐.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        logger.info("start processing of {} ", handlerMethod.getMethod().getName());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            logger.trace("{}: {}", headerName, request.getHeader(headerName));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Collection<String> headerNames = response.getHeaderNames();
        for(String headerName : headerNames) {
            logger.trace("{}: {}", headerName, request.getHeader(headerName));
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        logger.info("start processing of {} ", handlerMethod.getMethod().getName());
        //응답이 정상적으로 돌아오기 전에 발생한 예외가 Exception ex로 주입된다.
        if (ex != null) logger.error("Exception occurred while procession", ex);

    }
}
