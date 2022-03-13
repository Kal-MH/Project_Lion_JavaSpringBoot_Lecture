package dev.kalmh.controller.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("view")
//MVC에서 Controller로 동작하는 클래스라는 것을 알려주는 annotation
public class SampleController {
    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @GetMapping("/sample-jsp")
    public String sampleJsp(Model model) {
        logger.info("in sample jsp");

        //ModelAndView modelAndView = new ModelAndView();
        //modelAndView.setViewName();

        List<SamplePayload> profiles = new ArrayList<>();
        profiles.add(new SamplePayload("Adam", 22, "student"));
        profiles.add(new SamplePayload("Bdam", 22, "student"));
        profiles.add(new SamplePayload("Cdam", 22, "student"));

        model.addAttribute("profiles", profiles);
        //View Resolver로 1차 해당 파일이 있는 지 확인하고 없으면
        //경로로 요청하게 된다.
        return "view-jsp";
    }

    @GetMapping("/sample-thyme")
    public ModelAndView samplePayload() {
        ModelAndView modelAndView = new ModelAndView();

        logger.info("in sample thyme");

        List<SamplePayload> profiles = new ArrayList<>();
        profiles.add(new SamplePayload("Adam", 22, "student"));
        profiles.add(new SamplePayload("Bdam", 22, "student"));
        profiles.add(new SamplePayload("Cdam", 22, "student"));

        modelAndView.addObject("profiles", profiles);
        modelAndView.setViewName("view-thyme");
        return modelAndView;
    }

//    // 경로 설정, 어떤 경로에 어떤 함수가 매핑될 지,
//    // - Controller, RequestMapping 조합으로 기본적인 요청을 받는 응답함수(컨트롤러)에 대해서 정의 가능하다.
//    @RequestMapping(
//            value ="/hello", //url 경로를 설정
//            method = RequestMethod.GET // GET : 브라우저에 웹페이지를 가져오는 용도
//    )
//    //String으로 반환되어 View Resolver에 들어가면 어떤 거를 보내줘야 할 지 판단하게 된다.
//    public String hello() {
//        logger.info("Path hello");
//        //상대경로로 반환값 설정
//        return "/hello.html";
//    }
//
//    //Spring에서는 RequestMapping처럼 일반화된 용도의 어노테이션 사용보다
//    //특정한 어노테이션 사용을 권장한다.
//    @GetMapping(
//            value = "/hello/{id}" //경로상의 변수(param)으로 id가 들어가 있음
//    )
//    public String helloPath(@PathVariable("id") String id) {
//        logger.info("Path variable is " + id);
//        return "/hello.html";
//    }
//
//    @GetMapping(
//            value ="/hello"
//    )
//    public String hello(
//            @RequestParam( //get method로 들어올 때, 쿼리의 내용을 가져온다.
//                    name = "id",
//                    required = false,
//                    defaultValue = ""
//            )
//                    String id) {
//        logger.info("Path hello");
//        logger.info("Query Param id : " + id);
//        //상대경로로 반환값 설정
//        return "/hello.html";
//    }
//
//    @GetMapping(
//            value="/get-profile"
//    )
//    //getmethod의 결과로 돌려주는 데이터는 응답의 body에 작성된다.
//    // - @ResponseBody라는 어노테이션을 붙여주자.
//    // - 일반적인 자바 객체를 JSON 또는 xml형식으로 만들어서 전달해줄 수 있다.
//    // - 만약 @ResponseBody라는 어노테이션이 없다면 String값으로 View Resolver에 들어가게 되고 오류가 발생
//    public @ResponseBody SamplePayload getProfile() {
//        return new SamplePayload("kalmh", 10, "student");
//    }
}
