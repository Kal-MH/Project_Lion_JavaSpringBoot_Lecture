package deb.kalmh.jpa.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import deb.kalmh.jpa.exception.BaseException;
import deb.kalmh.jpa.exception.ErrorResponseDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PostExceptionResolver extends AbstractHandlerExceptionResolver {
    //HTML 렌더링하는 경우를 염두해두기 때문에 ModelAndview를 반환하고 있다.
    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
//        logger.debug(ex.getCause());
//        if (ex instanceof BaseException) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            try {
//                //Json 형식으로 반환해줘야 한다.
//                //String으로 한다고 하면 "{'name' : 'kal'}"형식으로 작성해야 함.
//                response.getOutputStream().print(
//                        //Model 객체를 JSON string으로 바꿔줌.(Data serialize)
//                        new ObjectMapper().writeValueAsString(
//                                new ErrorResponseDto("in resolver, message : " + ex.getMessage())
//                        )
//                );
//                response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//                //실제 ModelAndView를 사용하는 서버가 아님으로 실제 값이 들어갈 필요는 없다.
//                return new ModelAndView();
//            } catch(IOException e) {
//                logger.warn("Handling Exception caused IOException {}", e);
//            }
//        }
        return null; // null은 처리하지 못했음을 의미.
    }
}
