# chapter 3.

# 컴퓨터 통신과 HTTP

## HTTP란?

**HyperText Transfer Protocol**

응용 계층에서 사용하는 **프로토콜**로 웹 브라우저 상에서 사용되고, 주고받을 데이터가 어떤 규칙에 의해서 작성되어 있는지 정의하고 있다. 즉, **서버와 클라이언트 간에 메시지를 전달하는 형식을 정의한 규약**이라고 말할 수 있다.


```java
HTTP와 REST는 같은 것인가?

REST는 좋은 API를 위한 규칙을 정해놓은 것으로 그것이 HTTP와 같다고 말할 수 없다.
일반적으로 웹 어플리케이션이 통용화되어 있어서 HTTP를 활용한 API를
REST API라고 부르기도 한다.
```

## HTTP 요청 응답의 형식

기본적으로 데이터를 일련의 바이트 흐름으로 생각하고, HTTP 프로토콜에 의해서 해석, 즉 데이터 처리가 이뤄진다고 생각한다면, **HTTP 요청 형식**은 다음과 같은 내용들을 담고 있다고 말할 수 있다.

- `Request Line` : Method, Path, Version
    - 어떤 형식의 요청인가
    - 어떤 경로로 요청했는가
    - 지금 사용하는 규약의 버전
- `Request Headers` : HTTP 요청에 대한 부수적인 데이터
    - 요청이 어떠한 형식으로 작성되어 있는지 작성되어 있다. (요청의 길이, 어디로 보내지는 요청인지 등등..)
- `Request Body` : HTTP 요청에 관한 실제 데이터(요청하면서 보내는 데이터)

**HTTP 응답 형식**은 다음과 같다.


- `Status Line` : 요청 처리에 대한 상태 표시줄
    - 규약 버전
    - Status Code : 요청 처리 상태번호
    - Statuc Code 에 대한 정보
- `Response Hearders` : HTTP 응답에 대한 부수적인 데이터
    - Expires : 언제까지 유효한 데이터인지
    - Content-Type : 어떤 형식의 데이터를 보내는 것인지
- `Response Body` : 응답 데이터

### URL (Uniform Resource Locator)

Internet 상에 자원의 위치를 나타내는 문자열.


- `scheme` : 어떤 규약을 사용하는 지 나타낸다.
- `userInfo + @` : 규약에 따라서 선택적으로 명시한다.
    - 이메일을 예로, ‘**자신의 아이디 @ gmail.com**’, `scheme`은 ‘**smtp**’가 들어간다.
- `host` : 어떠한 곳으로 보낼 것인지 나타낸다. 도메인이나 ip정보가 들어온다.
- `port` : 컴퓨터 내에 프로세스를 구별하기 위한 숫자. 규약에 따라서 어떤 포트번호로 받을 것인지 미리 정해져 있기도 하다. 따라서 선택적으로 명시하고 작성이 되어 있지 않다면 scheme(규약)인지에 따라서 기본값을 사용하게 된다.
    
    ex) http - 80, https - 443
    
- `path` : 컴퓨터 안의 자원의 위치(경로), **‘해당 컴퓨터 안에 무엇을 요청했는가’** 를 정하는 부분.
- `query` : 정보 조회를 위한 부수적인 정보
- `fragment` : 하나의 문서의 부분을 나타내는 정보

## Media Types

인터넷 상에서 주고받는 데이터의 형식. HTTP 헤더에서 Content-Type 영역을 통해서 확인할 수 있다.

<aside>
💡 Content-Type : HTTP의 응답 데이터(Body)의 미디어 타입을 알려주는 헤더

</aside>

- application/json
    - JSON (JavaScript Object Notation)
        - 영상이나 이미지와 같은 바이트 데이터가 아닌 그 이외의 정보에 대한 데이터를 주고받을 때, 흔히 사용하는 형태
        - 속성 - 값 형태의 배열을 활용한다.
- multipart/form-data

# Controller와 RestController


## Spring MVC

1. MVC (Model - View - Controller)
    
    코드 역할에 따라 분류해 놓은 설계 패턴의 일종
    
    - View : 사용자가 확인하는 데이터의 표현
    - Model : 서비스 데이터 자체
    - Controller : 사용자의 입출력을 다루는 부분
2. 작동 순서
    1. 사용자가 View를 통해서 확인
    2. 사용자의 입력이 Controller에 전달, Controller는 Model(data)를 변경하든지 삭제하든지, 조정을 실행한다.
    3. 이렇게 갱신된 Model은 View를 통해서 다시 사용자에게 보여지게 된다.


![spring_mvc](https://user-images.githubusercontent.com/59648372/158056536-1afbf09d-5b2e-42a7-82fc-0be18fec3870.png)

(출처 : The Origin: Java Spring Boot 강의 - 강사 박지호)

1. 외부 요청 발생
2. 요청 경로 확인을 위해 전달
3. `Controller`로 전달
    1. `Controller`를 통해서 `Model` 조작
4. `Model` 조작
5. 갱신된 데이터 전달
6. 응답 전달
7. 응답을 클라이언트로 전송
    1. 데이터 전송
    2. 데이터를 포함한 `View` 제작
8. 사용자에게 `View` 제공
    1. `View Resolver`가 `View`의 역할을 하게 된다.

---

- `External Client` : browser
- `Dispatcher Servlet` : 구성된 Model, Controller, View를 관리하고 직접적으로 사용자와 대화하게 됨
- `Handler Mapping` : path와 method의 연결을 관리

## Controller & RequestMapping

### 프로젝트 설정

스프링 이니셜라이져를 사용해서 spring boot 프로젝트 generate

1. Spring Web을 포함한다.

### **SampleController**

어노테이션 사용 - 클래스 객체가 Bean으로써 IoC의 관리를 받게 된다.

1. **@Controller**
    
    MVC에서 controller로 동작하는 클래스라는 것을 알려주는 어노테이션.
    
    컨트롤러는 일반적으로 View 혹은 Data를 제공하는 용도로 사용되기 때문에 넓은 범위의 어노테이션이라고 할 수 있다. 
    
    만약, Java 객체를 반환값으로 넘겨주려고 한다면, 해당 데이터가 View가 아닌 Data라는 사실을 알려줘야 하기 때문에 `@ResponseBody`와 같은 추가적인 명시가 필요하다.
    
    1. `@RequestMapping`
        
        **@Controller**, `@RequestMapping` 어노테이션 조합으로 기본적인 요청 응답동작에 대한 정의가 가능하다.
        
        ```java
        @RequestMapping(
                value ="/hello", //url 경로를 설정
                method = RequestMethod.GET // GET : 브라우저에 웹페이지를 가져오는 용도
        )
        //String으로 반환되어 View Resolver에 들어가면 어떤 뷰를 보내줘야 할 지 판단하게 된다.
        public String hello() {
            logger.info("Path hello");
            //상대경로로 반환값 설정
            return "/hello.html";
        }
        ```
        
    2. `@GetMapping`
        
        Spring에서는 `@RequestMapping` 일반화된 용도의 어노테이션 사용보다 용도가 정해진 특정 어노테이션의 사용이 더 권장된다.
        
        @GetMapping 어노테이션은 Get메소드로 요청이 들어올 때 동작할 함수를 명시하는 용도로 사용된다.
        
        ```java
        @GetMapping(
                value = "/hello" //경로상의 변수(param)으로 id가 들어가 있음
        )
        public String helloPath() {
            return "/hello.html";
        }
        ```
        
        1. `@PathVariable`
            
            url을 받아 올 때, 경로상의 변수(Nodejs에서는 param)를 지정해줄 수 있다.
            
            보통, 로그인한 사용자의 id를 명시해서 일반적인 페이지와 다른 페이지를 보여주고 싶을 때, 사용할 수 있는데, 아래와 같이 ‘/hello/{id}’ 처럼 설정이 되어 있는 경우 hello/뒤에 오는 값은 코드 내에 id라는 변수명으로 접근할 수 있다.
            
            이처럼 경로상의 변수를 사용하고 싶을 때, `@PathVariable`어노테이션을 사용한다.
            
            ```java
            @GetMapping(
                    value = "/hello/{id}" //경로상의 변수(param)으로 id가 들어가 있음
            )
            public String helloPath(@PathVariable("id") String id) {
                logger.info("Path variable is " + id);
                return "/hello.html";
            }
            ```
            
        2. `@RequestParam`
            
            Get메소드 요청에서 query값을 가져올 때 사용하는 어노테이션.
            
            중요한 것은 defaultValue를 설정할 수 있기 때문에 값이 들어오지 않는 경우에 대한 예외처리가 가능하다.
            
            ```java
            @GetMapping(
                    value ="/hello"
            )
            public String hello(
                    @RequestParam( //get method로 들어올 때, 쿼리의 내용을 가져온다.
                            name = "id",
                            required = false,
                            defaultValue = ""
                    )
                            String id) {
                logger.info("Path hello");
                logger.info("Query Param id : " + id);
                //상대경로로 반환값 설정
                return "/hello.html";
            }
            ```
            
        3. `@ResponseBody`
            
            일반적인 자바 객체 데이터를 JSON, xml형식으로 만들어서 전달하고자 할 때 사용할 수 있는 어노테이션. 응답 바디에 데이터가 있다는 것을 명시해준다.
            
            Get메소드의 결과로 돌려주는 데이터는 응답의 body에 작성된다. 예를 들어 ‘hello.html’이라는 html문서를 보내주고자 할 때, 응답의 body에 작성된다.
            
            만약 `@ResponseBody` 어노테이션이 없다면 해당 SamplePayload 객체값은 단순한 String으로 취급되어 View Resolver로 들어가게 되고 일치하는 View를 찾으려고 하기 때문에 에러가 발생한다.
            
            ```java
            @GetMapping(
                    value="/get-profile"
            )
            public @ResponseBody SamplePayload getProfile() {
                return new SamplePayload("kalmh", 10, "student");
            }
            ```
            

### **SampleRestController**

RestController는 Controller + ResponseBody라고 생각해주면 된다. Data를 주고받는 기능을 기본으로 생각하고 사용되기 때문에 `@ResponseBody`가 자동으로 붙는 것과 같은 효과를 얻을 수 있다. 

```java
//payload는 일반적으로 http요청 응답의 body를 지칭하는 말
    @GetMapping("/sample-payload")
    public SamplePayload samplePayLoad() {
        return new SamplePayload("kalmh", 10, "student");
    }
```

1. prefix annotation
    
    ```java
    @RestController
    @RequestMapping("/rest") //prefix 경로로 매핑해줌.
    public class SampleRestController {
        private static final Logger logger = LoggerFactory.getLogger(SampleRestController.class);
    		...
    }
    ```
    
2. 이미지 보내기 간단 구현
    
    ```java
    //이미지는 기본적으로 바이트 형태
    public byte[] sampleImage() throws IOException {
        //getClass() : class의 path를 받아옴
        //getResourceAsStream() : resource를 받아옴
        // - resource폴더 안에서 해당 파일명의 파일을 찾아온다.
        InputStream inputStream = getClass().getResourceAsStream("/static/url.png");
        return inputStream.readAllBytes();
    }
    ```
    

# HTML Template

## 동적 HTML

현재 시각, 사용자에 따라 달라지는 내용을 반영하기 위해서는 동적으로 데이터를 업데이트해줘야 한다.

<aside>
💡 동적이란?

</aside>

- static(정적) 컨텐츠 : 이미 작성이 완료되어 변하지 않는 파일들, HTML, CSS, JS, Image ....
- Dynamic(동적) 웹 페이지 : 서버에서 HTML문서의 내용을 데이터에 따라 다르게 작성하여 제공하는 페이지

## JSP와 Thymeleaf

### JSP

JSP와 HTML파일을 구분해서 읽은 다음, 하나로 합쳐서 다시 작성하는 과정을 거치게 된다.

1. jsp 파일 작성

```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 - JSP문서임을 나타내는 태그
```

1. [application.properties](http://application.properties) 작성

View Resolver에게 해당 설정으로 작업을 진행할 수 있도록 작성한다.

```java
spring.mvc.view.prefix: /WEB-INF/jsp/ //-> 해당 경로의 prefix를 갖는다.
spring.mvc.view.suffix: .jsp // -> 확장자가 혹은 전체 경로에서 .jsp 끝나는 파일
```

- **Error : Not found(아직 해결 안됨)**
    - Gradle sync 확인
    - jsp경로 확인

### Thymeleaf

1. template 폴더에 파일 추가
2. html 문서에 해당 내용 추가
    
    ```
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org" lang="en">
    ```
    
3. Model을 넘겨주는 2가지 방법
    1. Model 객체 활용
        
        ```java
        @GetMapping("/sample-thyme")
        public String sampleJsp(Model model) {
            logger.info("in sample thyme");
        
            List<SamplePayload> profiles = new ArrayList<>();
            profiles.add(new SamplePayload("Adam", 22, "student"));
            profiles.add(new SamplePayload("Bdam", 22, "student"));
            profiles.add(new SamplePayload("Cdam", 22, "student"));
        
            model.addAttribute("profiles", profiles);
        
            return "view-thyme";
        }
        ```
        
    2. ModeAndView 객체 활용
        
        ```java
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
        ```
        

## React, Vue, Node와 Spring Boot

# Postman

# Reference

---

 (출처 : The Origin: Java Spring Boot 강의 - 강사 박지호)
