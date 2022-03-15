# Java의 예외 처리

자바의 일반적인 예외 처리는 다음과 같다.

- try-catch-finally
- throws

```java
try {
	...
	... //예외가 일어날 수 있는 구역
} catch (Exception e) {
	... //예외가 발생할 시 실행할 부분
} finally {
	... //예외의 발생여부와 관계 없이 항상 실행하는 코드
}

---

public void func() throws NullPointException {
	...
}
```

자바에서는 Method signature의 일부로, 처리되지 않은 예외는 Compile Error를 발생시킨다.(RuntimeException 제외)

예를 들어, 위 throws 예제와 같이 Exception 처리를 했을 때, 해당 부분이 잘 처리가 되어 있지 않다면 Compile 단에서 먼저 에러가 발생하게 된다.

# Spring boot의 예외 처리

- ResponseStatusException
    
    단발적 예외 상황 즉, 특출나게 강한 예외 처리 대응을 할 필요가 없을 때 사용한다.
    
    간단한 작업을 만들어야 할 경우, 빠르게 기능 구현을 하고 오류 지점을 파악해야 하는 경우에 효과적으로 사용할 수 있는 방법이고, 무엇보다 스프링 부트 내부에서 자동적으로 예외 처리할 준비가 마련되어 있다는 것이 큰 장점이다.
    
    또한, ResponseStatusException 어느 시점에서 일어나는 지를 잘 모아둔다면 예외 처리 규칙을 잘 만들 수 있다.
    
    ResponseStatusException의 경우, 동일한 Exception 처리 코드가 여러 줄에 거쳐 중복적으로 들어간다거나, 예외처리 세부 사항을 좀 더 정밀하게 나누는 데에 한계가 있기 때문에 초기 단계에서 많이 사용한다.
    
    ```java
    @GetMapping("{id}")
    public void throwException(@PathVariable("id") int id) {
        switch(id) {
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    ```
    
- @ExceptionHandler
    
    Controller 내부에서 함수에 붙여서 사용하는 에러 처리 방법. 예외 처리 범위는 정의된 Controller 내부로 한정되며, 정의된 함수는 지정된 예외에 대해서 예외가 발생했을 때, 호출되게 된다.
    
    ```java
    @ExceptionHandler(BaseException.class)
    public void handleBaseException(
            BaseException exception,
            // 스프링 내부에 존재하는 @Exception어노테이션인만큼
            // 함수에 필요한 객체들을 자동으로 주입함으로 사용할 수 있다.
            HttpServletResponse response
    ) {
    	...
    }
    ```
    
    위 예시처럼, 해당 컨트롤러 내부에서 발생하는 모든 BaseException에 대해서 handleException()함수가 호출된다.
    
    혹은, HttpServletResponse 객체를 받지 않고, 어노테이션으로도 처리 가능하다.
    
    ```java
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBaseException (BaseException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }
    ```
    
- HandlerExceptionResolver
    
    실제 에러 처리를 구성함으로, 애플리케이션 전체에 적용되는 에러 처리 방식이 필요할 때, 호출된다.
    
    - `HandlerExceptionResolver`
    
    ```java
    @Component
    public class PostExceptionResolver extends AbstractHandlerExceptionResolver {
        //HTML 렌더링하는 경우를 염두해두기 때문에 ModelAndview를 반환하고 있다.
        @Override
        protected ModelAndView doResolveException(
                HttpServletRequest request,
                HttpServletResponse response,
                Object handler,
                Exception ex) {
            logger.debug(ex.getCause());
            if (ex instanceof BaseException) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try {
                    //Json 형식으로 반환해줘야 한다.
                    //String으로 한다고 하면 "{'name' : 'kal'}"형식으로 작성해야 함.
                    response.getOutputStream().print(
                            //Model 객체를 JSON string으로 바꿔줌.(Data serialize)
                            new ObjectMapper().writeValueAsString(
                                    new ErrorResponseDto("in resolver, message : " + ex.getMessage())
                            )
                    );
                    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    //실제 ModelAndView를 사용하는 서버가 아님으로 실제 값이 들어갈 필요는 없다.
                    return new ModelAndView();
                } catch(IOException e) {
                    logger.warn("Handling Exception caused IOException {}", e);
                }
            }
            return null; // null은 처리하지 못했음을 의미.
        }
    }
    ```
    
- @ControllerAdvice
    
    Bean의 일종. ExceptionHandler의 모음이라고 말할 수 있다.
    
    ```java
    // 여러 Controller에 흩어져 있는 ExceptionHandler를 한 곳으로 모으기 위한 어노테이션
    // Bean으로 등록 가능한 Componenet의 일종
    // ExceptionHandler와 다르게 종속적이지 않음.
    // @RestControllerAdvice가 존재 -> @ResponseBody 어노테이션 생략 가능
    @RestControllerAdvice
    public class PostControllerAdvice {
        @ExceptionHandler(BaseException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponseDto handleException(BaseException exception) {
            return new ErrorResponseDto(exception.getMessage());
        }
    
    //valid에 대한 exception 처리
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ErrorResponseDto handleValidException(
                MethodArgumentNotValidException exception
        ) {
            return new ErrorResponseDto(exception.getMessage());
        }
    }
    ```
    

# Interceptors & filters

## Filter & Interceptor 기본 개념

<aside>
💡 Filter는 스프링 부트 외부에 존재, Interceptor는 내부에 존재한다.

</aside>

이전 예외처리 방식의 경우, 스프링내부에서 발생한 예외에 대한 처리였다. 즉, Interceptor의 경우 스프링 내부에 있기 때문에 Interceptor에서 발생하는 예외는 앞선 방법으로 처리가 가능했다. 하지만 Filter는 외부에 있기 때문에 예외처리가 불가능하다.

![filter interceptor](https://user-images.githubusercontent.com/59648372/158337156-53455222-3139-4496-8a4e-0fdb692576a4.png)

### Filter

Web application에서 관리되는 영역으로 Client로부터 오는 요청/응답에 대한 최초/최종 단계에 위치하며 Spring에 의해 데이터가 변환되기 전의 순수한 client의 요청/응답 값을 확인할 수 있다.

**Business Login과 무관한 기능**, 즉 보안, 데이터 인코딩에 사용된다.

```java
public void doFilter(ServletRequest request, ServletResponse response,
	FilterChain chain) throws IOException, ServletException;
```

- ServletRequest
    - HttpServletRequest, Response의 조상객체
    - ServletRequest, Response를 조작할 수 있게 된다.
        - ServletRequest를 HttpServletRequest로 바꾼다거나, 아예 다른 Request를 구현해서 사용 가능.
- FilterChain
    - filter의 전후를 구분한다.
    - 여러 개의 Filter가 존재하고, 그 중 구현한 doFilter()가 어디 filter에 속해있는 지 구분해주는 객체.

Filter의 경우, body의 내용을 읽는 데는 수월하나 조작하는 코드의 내용은 Interceptor보다 길다.

### Interceptor

Interceptor는 Spring context의 기능으로 일종의 Bean이라고 할 수 있다.  다른 빈을 주입하거나, 활용이 가능하기 때문에 예외처리, 데이터 베이스 모델 같은 부분들은 모두 사용할 수 있다.

또한, 3개의 함수로 분리되어 있어서 더 세밀하게 조작이 가능하다.

Interceptior는 사용자 인증, API처리 내용등과 같이 Business Login과 연관성이 높은 기능 구현에 사용된다.

- preHandle()
    - 컨트롤러로 요청이 들어오기 전에 사용하는 함수
- postHandle()
    - 컨트롤러에서 응답을 만들고 난 다음에 호출되는 함수
- afterCompletion()
    - 응답을 클라이언트에게 보내고 난 다음에 호출되는 함수

preHandler(), postHandle() 사이에서 Contoller와 Model의 조작이 이뤄진다.

postHandle(), afterCompletion() 사이에서는 요청전달, 응답전달이 이뤄진다.

## Filter 구현하기

TransactionLogFilter 클래스를 구현하자.

```java
public class TransactionLogFilter implements Filter{
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
```

## HandlerInterceptor 구현하기

```java
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
```

# Spring boot Tests

## Testing에 대하여

### Intergration Test

클래스들이 서로 상호작용을 잘 하는 지 확인하는 테스트.

### Unit Test

Controller, Service, Repository 등 클래스 하나 단위로 범위를 정하고, 각각의 함수들이 잘 동작하는 지 보는 개별 코드 단위의 테스트.

<aside>
💡 `testImplementation 'org.springframework.boot:spring-boot-starter-test'`

</aside>

spring initializer를 통해 만들어진 spring boot 파일을 보면 build.gradle에 이미 해당 의존성이 추가가 된 것을 볼 수 있다. test 라이브러리는 각각 다음의 테스팅 라이브러리를 포함하고 있다.

- JUnit : 사실상의(de-facto) Java 어플리케이션 Testing 표준 라이브러리
- Spring Test : Spring 어플리케이션 Test 지원 라이브러리
- AssertJ : 가독성 높은 Test 작성을 위한 라이브러리
- Hamcrest : Test 진행시 제약사항 설정을 위한 라이브러리
- Mockito : Test용 Mock 라이브러리
- JSONassert : JSON용 Assertion 라이브러리
- JsonPath : JSON 데이터 확인용 라이브러리

## Spring Boot 테스트 작성

### PostController Unit Test

1. JUnit 4 버전을 기준으로 라이브러리를 사용하기 위해 해당 버전의 의존성을 `build.gradle`파일에 명시한다. (현재는 5버전 기준이기 때문에 포함되어 있지 않다.)
    
    ```
    testImplementation('org.junit.vintage:junit-vintage-engine'){
    	exclude group: 'org.hamcrest', module: 'hamcrest-core'
    }
    ```
    
    - `exclude group` : 이미 포함된 라이브러리들 중 junit vintage에 겹쳐지는 라이브러리가 있기 때문에 중복성을 피하기 위해서 배제시킬 라이브러리를 지정한다.
2. JpaAuditConfig 클래스 작성
    
    ```java
    @Configuration
    @EnableJpaAuditing
    public class JpaAuditConfig {
    	...
    }
    ```
    
    - 테스트하는 과정에서 몇 개의 클래스만 선택적으로 돌리기 때문에 JPA어플리케이션 엔트리 부분에 붙으면 동작하지 않는 상황이 발생할 수 있다.
3. PostControllerTest 클래스 생성
    
    `Alt + Enter` 혹은 `Alt + Insert` 를 누르면 해당 창이 열리는데 이를 통해서 원하는 Controller 클래스의 test파일을 만들 수 있다.
    
    ![createTest](https://user-images.githubusercontent.com/59648372/158337171-933109da-5ebe-4b14-8732-5b2036dcb047.png)
    
4. PostControllerTest 클래스 작성
    
    ```java
    @RunWith(SpringRunner.class)
    @WebMvcTest(PostController.class)
    public class PostControllerTest {
        @Autowired
        //http client인 척 한다.
        private MockMvc mockMvc;
    
        @MockBean
        private PostService postService;
    
        @Test
        public void readPost() {
        }
    
        @Test
        public void readPostAll() {
        }
    }
    ```
    
    - @RunWith : 어떤 어플리케이션을 기준으로 테스트하는 지
    - @WebMvcTest
        - Controller의 함수들을 테스트하기 위해 붙여주는 어노테이션
        - 단위테스트할 때 붙여주는 어노테이션
        - MVC와 관련된 빈들을 테스트하기 때문에 Service, Repository등을 사용할 수 없다. 따라서 @Autowired 어노테이션을 바로 사용할 수 없고 @MockBean을 통해서 사용하게 된다.
    - @MockBean
        - 만들어지지 않은 bean이 만들어진 것으로 가정하고 사용하게 함.
        - 실제 구현체와는 별개로 빈 객체로서 IoC 컨테이너에 등록이 되어 사용되게 한다.
    
    테스트 코드를 작성할 때는 3가지 키워드를 기억하자.
    
    - given
        - 어떤 데이터가 준비가 되어 있다. (조건)
        - ex) readPost 기준으로, PostEntity가 존재할 때 (PostService가 PostEntity를 잘 돌려줄 때)
    - when
        - 어떤 행위(함수 호출)이 일어났을 때
        - ex) 경로에 GET 요청이 오면
    - then
        - 어떤 결과가 올 것인가
        - 조건이 주어졌고(given), 어떤 행위가 일어났을 때(when), 어떤 일이 일어날 것인지
        - ex) PostDto가 반환된다.
    
    ```java
    @Test
    public void readPost() throws Exception {
    		//given
        final int id = 10;
        PostDto testDto = new PostDto();
        testDto.setId(id);
        testDto.setTitle("Unit Title");
        testDto.setContent("Unit Content");
        testDto.setWriter("unit");
    
        //특정 행동을 하길 바를 함수를 지정
        //postService가 실제 구현체는 아니기 때문에, readPost()함수의 행동을 흉내낼 수 있도록 도와준다.
        given(postService.readPost(id)).willReturn(testDto);
       
    	  // when : 어떤 행위(함수 호출)이 일어났을 때
        final ResultActions actions = mockMvc.perform(get("/post/{id}", id))
                .andDo(print());
    
        // then : 어떤 결과가 올것인지(조건이 주어졌고(given), 어떤 행위가 일어났을 때(when) 어떤 일이 일어날 것이다.
    
        //perform() 결과로 돌려준 인터페이스를 통해 우리가 원하는 결과가 맞는 지 확인한다.
        actions.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
    						//jsonPath는 JSON 정규표현식
                jsonPath("$.title", is("Unit Title")),
                jsonPath("$.content", is("Unit Content")),
                jsonPath("$.writer", is("unit"))
        );
    }
    ```
    

### PostController Intergration Test

1. PostControllerIntergrationTest Class 작성
    
    ```java
    @RunWith(SpringRunner.class)
    //전체 어플리케이션을 테스트한다는 의미
    @SpringBootTest(
            //test진행시, 실제 환경이 어디일지를 가정(어디에 생성할 지)
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
            classes = JpaApplication.class
    )
    @AutoConfigureMockMvc
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    //h2 database를 사용함을 의미
    @AutoConfigureTestDatabase
    public class PostControllerIntegrationTest {
        @Autowired
        private MockMvc mockMvc;
    
        @Autowired
        private PostRepository postRepository;
    
        //test 실행전에 호출되는 함수
        @Before
        public void setEntitties() {
            createTestPost("fist post", "fist post content", "test-writer");
            createTestPost("second post", "second post content", "test-writer");
        }
    
        //test 함수 명명 규칙에는 정해진 게 없으나
        //몇 단계인지 구분을 해서 짓는 것이 일반적
        @Test
        void givenValidId_whenReadPost_then200() throws Exception {
            //given
            Long id = createTestPost("Read Post", "Created on readPost()", "read-tester");
            
            //when
            final ResultActions actions = mockMvc.perform(get("/post/{id}", id))
                    .andDo(print());
            
            //then
            actions.andExpectAll(
                    status().isOk(),
                    content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                    jsonPath("$.title", is("Read Post")),
                    jsonPath("$.content", is("Created on readPost()"))
            );
        }
    
        private Long createTestPost(String title, String content, String writer) {
            PostEntity postEntity = new PostEntity();
            postEntity.setTitle(title);
            postEntity.setContent(content);
            postEntity.setWriter(writer);
            return postRepository.save(postEntity).getId();
        }
    
    }
    ```
    

## Test Driven Development

테스트 주도 개발.

실제 작동하는 코드 이전에 통과 해야 할 테스트를 우선 만드는 개발 방식을 말한다.

1. 실패할 테스트 작성 (Red)
    1. 기본적으로 아직 작성된 코드가 없으므로 테스트 실행 및 실패가 된다.
2. 테스트를 통과하는 코드 작성 (Green)
    1. 테스트 통과 확인
3. 작성된 코드 수정 (Refactor)

기본적으로 Unit Test부터 시작함으로 객체 지향적인 원칙들이 지켜질 수 있을 것이라고 기대할 수 있다. 

또한, 모든 테스트를 통과해야 코드 작성이 이뤄지는 방식으로 이뤄짐으로 디버깅과 재설계에 대한 비용이 절감되어 생산성의 향상을 기대할 수 있다.

```java
@RunWith(SpringRunner.class)
@WebMvcTest(BoardController.class) //-> error
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createBoard() throws Exception {
        //given
        BoardDto boardDto = new BoardDto(); //->error
        boardDto.setName("notice"); //->error

        //when
        final ResultActions actions = mockMvc.perform(post("/board") //->error
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(boardDto)))
                .andDo(print());

        //then
        actions.andExpectAll(
                status().is2xxSuccessful(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON), //->error
                jsonPath("$.name", is("notice"))
        );
    }

    private byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
```

# Spring Boot Actuator

## 상용 서비스 준비

상용화를 위해서는 어플리케이션 기능 외에 고려해야 할 사항들이 존재할 수 있다. (서비스가 안정적으로 돌아가는데 필요한 기능)

- 컴퓨터 메모리 사용량
- 컴퓨터 디스크 공간

## Actuator 기본 설정

기본 설정을 `build.gradle`에 추가해준다.

```java
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

어플리케이션을 build하고 나면 다음과 같은 로그를 볼 수 있다.

```java
: Exposing 1 endpoint(s) beneath base path '/actuator'
```

즉, 자동으로 `http:localhost:8080/actuator` 경로가 하나 생겼다는 것을 알 수 있다.

### Actuator yaml 설정

```java
management:
  endpoints:
    # enabled : actuator가 가지고 있는 빈을 실제로 사용할지 말지를 결정
    # true로 설정하면 endpoint 각각의 enabled를 설정할 필요가 없다.
    enabled-by-default: false
    web:
      # exposure에 포함되지 않으면 endpoint가 존재하지 않는다.
      # 어플리케이션 내부에는 존재하지만 정보를 받아올 수는 없는 상태가 된다.
      exposure:
        include: health, info, loggers
# management.endpoint.<function>.enabled=true
# management.endpoint.<function>.show-details=always
  endpoint:
    # endpoint health의 설정
    health:
      enabled: true
      show-details: always
      show-components: always
    info:
      enabled: true
    loggers:
      enabled: true
```

- /actuator
- /actuator/health
    - 일반적으로 health check라 함은 서버가 사용자의 요청을 받을 수 있는 상태인지 체크하는 것을 말한다.
- /actuator/health/{*path}
- /actuator/loggers/dev.kalmh.jpa.interceptor
    
    ```java
    GET
    {
    	"configuredLevel" : null,
    	"effectiveLevel" : "INFO"
    }
    
    POST -> 204 No Content
    {
    	"configuredLevel" : "TRACE" //null, DEBUG, ...
    }
    --> 다시 GET으로 보내보면 loggerLevel이 모두 TRACE로 바뀐 것을 확인할 수 있다.
    ```
    
- /actuator/shutdown

## Actuator prometheus

세계적으로 많이 사용하는 모니터링 도구

- 다양한 계측 정보를 HTTP요청을 통해 받아온다.
    - Actuator에서 제공한 endpoint를 통해서 여러 정보를 받아온다.
- 계측 정보를 GUI로 표시하고 위험 상황에 대한 알림을 보여준다.

```java
//prometheus를 사용하기 위해 추가
runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
```

(출처 - ProjectLion - The Origin SpringBoot 강사 박지호)
