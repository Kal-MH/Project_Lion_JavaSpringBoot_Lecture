package deb.kalmh.jpa;

import deb.kalmh.jpa.exception.BaseException;
import deb.kalmh.jpa.exception.ErrorResponseDto;
import deb.kalmh.jpa.exception.PostNotInBoardException;
import deb.kalmh.jpa.exception.PostNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("except/")
public class ExceptTestController {
    //crtl + shift + f
    @GetMapping("{id}")
    public void throwException(@PathVariable("id") int id) {
        switch(id) {
            case 1:
                throw new PostNotExistException();
            case 2:
                throw new PostNotInBoardException();
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //해당 컨트롤러 내부에서 발생하는 모든 BaseException에 대해서 handleException()함수가
    //에러처리를 위해 호출되게 된다.
//    @ExceptionHandler(BaseException.class)
//    public void handleBaseException(
//            BaseException exception,
//            // 스프링 내부에 존재하는 @Exception어노테이션인만큼
//            // 함수에 필요한 객체들을 자동으로 주입함으로 사용할 수 있다.
//            HttpServletResponse response
//    ) {
//
//    }

//    @ExceptionHandler(BaseException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) //HttpServletResponse 객체를 받지 않고, 어노테이션으로도 처리 가능
//    public ErrorResponseDto handleBaseException (BaseException exception) {
//        return new ErrorResponseDto(exception.getMessage());
//    }
}
