package deb.kalmh.jpa.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/*
 * 어떤 요청읻 들어오고, statuscode는 어떻게 되는 지.
 */
public class TransactionLogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TransactionLogFilter.class);
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String requestUUID = UUID.randomUUID().toString().split("-")[0];
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        logger.debug("[{}] start request : {} {}",
                    requestUUID,
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI()
        );
        //chain의 doFilter를 요청하기 전까지가 filter에서 spring으로 넘어가기 전이다.
        logger.info("* response status code: {} ",((HttpServletResponse) response).getStatus());
        chain.doFilter(request, response);

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        logger.info(" * response status code: {}", ((HttpServletResponse)response).getStatus());
        logger.debug("[{}] status response: {}",
                requestUUID, httpServletResponse.getStatus());
    }
}
