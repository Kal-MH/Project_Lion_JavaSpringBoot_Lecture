package dev.kalmh.controller.demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

//@RestController 어노테이션으로 인해 @ResponseBody 어노테이션을 붙이지 않아도 된다.
// 1. @Controller같은 경우에, View 혹은 Data를 제공하는 용도로 사용된다. (좀 더 넓은 범위의 어노테이션)
// 2. @RestController는 기본적으로 Data를 주고받는 것을 나타냄으로 자동으로 @ResponseBody를 붙은 것과 같은 효과
//    - Controller + ResponseBody
@RestController
@RequestMapping("/rest") //prefix 경로로 매핑해줌.
public class SampleRestController {
    private static final Logger logger = LoggerFactory.getLogger(SampleRestController.class);


    //payload는 일반적으로 http요청 응답의 body를 지칭하는 말
    @GetMapping("/sample-payload")
    public SamplePayload samplePayLoad() {
        return new SamplePayload("kalmh", 10, "student");
    }

    @GetMapping(
            value = "/sample-image",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    //이미지는 기본적으로 바이트 형태
    public byte[] sampleImage() throws IOException {
        //getClass() : class의 path를 받아옴
        //getResourceAsStream() : resource를 받아옴
        // - resource폴더 안에서 해당 파일명의 파일을 찾아온다.
        InputStream inputStream = getClass().getResourceAsStream("/static/url.png");
        return inputStream.readAllBytes();
    }

}
