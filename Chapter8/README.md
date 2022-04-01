# Auth & HTTPS

Auth란, 로그인 이후의 일들까지 함축하고 있다. 

## Authorization & Authentication

로그인의 의미는 무엇인가.

특정 사이트에서 요구하는 정보를 제공함으로써 자신이 누구인 지 밝히는 것이라고 말할 수 있겠다. 즉, 한 개인이 자기 자신을 증명함으로써 특정 컴퓨터 시스템에 접근하는 행위라고 할 수 있다. (ex) mysql 로그인, linux OS user login, ....)

그렇다면 Auth란?

Auth란 기본적으로 아래 두 가지를 모두 포함하는 개념이다.

- Authentication
    - 사용자가 자신이 누구인지를 증명하는 과정
    - REST에서 사용자가 자신이 누구인지를 매번 요청마다 증명해야 한다.
    - ex) 로그인, 소셜 로그인, API 요청(API KEY발급과 요청할 때 같이 제공)
- Authorization
    - 사용자의 기능 사용 권한을 검증하는 과정
    - 블로그, SNS 등 여러 상황에서 어떤 서비스를 사용할 수 있는 지 검증한다.
    - ex) 권한 관리, 작성, 차단 등...

### REST 사용자 인증

1. 클라이언트에서 로그인 요청을 함.
2. 서버에서는 데이터베이스에 저장된 유저 정보를 통해 해당 사용자가 맞는지 확인하고, 맞다면 `sessionId`를 보낸다.
3. 클라이언트(브라우저)에서는 `sessionId`를 쿠키에 저장한다.
    1. 쿠키란,
        
        브라우저에서 저장하고 있는 사용자 정보를 쿠키라고 한다. HTTP 요청은 상태를 기억하지 않는 요청으로, 매 요청마다 서버에게 자신이 누구인지 클라이언트는 밝혀야 한다. 서버에서 보내주는 일련의 문자열(key-value형태)을 `sessionId` 브라우저에 쿠키로 저장하고 이를 매 요청마다 사용한다.
        
        또한 쿠키는 도메인 기반으로 작동한다. 하나의 도메인 내에서 다른 도메인에 있는 쿠키를 확인할 수도, 사용할 수도 없다.
        
        - 기술적 쿠키 : 검색하는 주체가 사람인지, 어플리케이션인지
        - 분석 쿠키 : 무엇을 많이 검색하는지, 어떤 종류의 검색인지, 시간, 언어 대상
        - 광고 쿠키 : 국가, 언어, 검색 내용에 따라 다르게 저장.
4. 클라이언트는 매 요청마다 `sessionId`를 동봉해서 보낸다.
5. 서버는 `sessionId`를 통해 key-value 매핑의 관계에 있는 유저 정보를 조회하여 사용자를 검증하고, 서비스를 제공한다.

즉, `sessionId`에 해당하는 서버에서 일시적으로 저장한 유저 정보를 통해서 로그인 여부를 판단하게 된다. 

<aside>
💡 페이지 소스 검사를 통해서 쿠키를 확인할 수 있다.

</aside>

## HTTPS

TLS 란, 컴퓨터 네트워크 상의 정보를 안전하게 공유하기 위한 암호화 규칙으로, 흔히 말하는 HTTPS는 HTTP에 TLS가 적용된 형태이다.

```yaml
암호화

특정 규칙(알고리즘)을 가지고 평범한 데이터를 제 3자가 확인할 수 없도록 정보를 숨기는 과정
- 평범한 데이터 : 평문
- 정보가 숨겨진 데이터 : 암호문
```

- 대칭키 암호화
    - 같은 키를 사용하여 암호화
    - 빠르고 자원 소모가 적음
    - 양측이 동일한 키를 가지고 있어야 함
- 비대칭키 암호화(공개키 암호화)
    - 서로 다른 키를 사용하여 암호화
    - 개인키를 공개하지 않아 보안이 뛰어남
        - 개인키는 서버 내부에서 사용하는 암호화를 위한 키로, 외부에 공개되지 않는다.
        - HTTP요청이 있을 때, 공개키를 통해 암호화했다면, 복호화는 개인키를 통해 진행된다.
    - 하드웨어 자원 소모가 큼
    - 암호화, 복호화 시간이 오래 걸림

TLS는 두가지 암호화를 모두 사용한다.

1. 브라우저가 서버에 첫번째 요청을 보낼 때, 안전한 연결에 대한 요청과 함께 자신이 사용할 수 있는 암호화 방식을 서버에 보낸다.
2. 서버에서는 전달 받은 암호화 방식 중에서 자신이 사용 가능한 암호화 방식과 인증서를 응답으로 다시 보낸다. (공개키 포함)
3. 브라우저는 공개키를 통해 대칭키를 만들고, 만들어진 대칭키를 공개키로 암호화 해 서버에 전송한다.
4. 서버는 개인키를 통해 공개키로 암호화된 대칭키를 복호화, 대칭키를 확인한다.
5. 서버에서 대칭키를 통해 응답 암호화를 진행하고 이를 브라우저에 전송한다.
6. 브라우저에서는 대칭키로 암호문을 평문으로 복호화하고, 매 요청을 대칭키로 암호화를 해서 보내게 된다.

<aside>
💡 암호화를 사용한다 하더라도 보안을 위해 민감한 정보를 최대한 보내지 않도록 구현해야 한다.

</aside>

# Login 기본

## 로그인 프로젝트

1. spring initializer
    1. Spring Web
    2. Thymeleaf
    3. **Spring Security**
        
        <aside>
        💡 `implementation 'org.springframework.boot:spring-boot-starter-security'`
        
        </aside>
        
        단순히 의존성을 추가하는 것만으로도 로그인페이지가 생성됨을 볼 수 있다.
        
        - id : user
        
        또한, JSession 쿠키에 사용자 정보를 저장하고 있는 것 또한 확인할 수 있다.
        
    4. Spring Data JPA
    5. H2 Database
    6. MySQL Driver

## 로그인 구현

1. Home Controller 작성 및 templates/index.html 작성
    1. WebSecurityConfig Class
        
        ```java
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antRequest()
        						//어떤 요청에 대해서 누구든 허용한다.
        						.permitAll()
        }
        ```
        
        - `authorizeRequests()` : authorizeRequests() 밑으로는 url 패턴을 기준으로 어떤 것을 허용하고, 허용하지 않을 지 결정
        - `annoymous()` : 로그인하지 않은 사용자의 접근 허용
        - `permitAll()` : 로그인 여부 상관없이 모든 사용자 접근 가능
        - `authenticated()` : 로그인한 사용자의 접근 허용
    2. Home Controller 작성 및 templates/index.html 작성
        - index.html sec 설정 추가
            
            ```html
            <!--인증 관련 설정을 위해-->
            xmlns:sec="http://www.w3.org/1999/xhtml"
            ```
            
        - 우리가 설정한 http설정에 따라서 에러 페이지를 확인할 수 있다.
            - authenticated() → Whitelabel Error Page
                - http 객체에 추가적인 보안 설정을 함으로써 기본 로그인 페이지가 작동하지 않아서 에러페이지가 돌아온다.
            - permitAll() → OK
2. home 페이지 제외 다른 페이지는 로그인 사용자만 접근하도록 설정
    
    ```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
    						// url /home에 대해서는 로그인하지 않아도 접근 허용
                .antMatchers("/home/**")
    						.annoymous()
    						// 그 외 모든 요청 (ex) /admin) 에 대해서는 로그인한 사용자만 접근 허용
    						.antRequest()
    						.authenticated()
    }
    ```
    
    - /home 을 포함 하위 계층 path에 대해 로그인하지 않은 익명 사용자 접근 허용
        - ** : 여러 계층 path
        - * : 하나의 path
    1. AdminController & admin.html 작성
3. 로그인 창 띄우기
    1. 커스텀 로그인 페이지 제작(login-form.html)
        
        ```html
        <!DOCTYPE html>
        <html lang="en"
              xmlns:th="http://www.thymeleaf.org"
              xmlns:sec="http://www.w3.org/1999/xhtml">
        <head>
            <meta charset="UTF-8">
            <title>Simple Login</title>
        </head>
        <body>
        <form th:action="@{/user/login}" method="post">
            <input type="text" name="username" placeholder="아이디">
            <input type="password" name="password" placeholder="비밀번호">
            <button type="submit">로그인</button>
        </form>
        <button onclick="location.href = '/oauth2/authorization/naver'">네이버 아이디로 로그인</button>
        </body>
        </html>
        ```
        
    2. 추가 설정
        
        ```java
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
        						.antMatchers(
                                "/home/**",
                                "/user/**")
        						.annoymous()
        						.authenticated()
        				.and()
        						.formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/home")
                    //위의 authorizeRequest 설정보다 우선순위를 부여한다.
                    // - login페이지 처럼 일부 페이지를 로그인되지 않아도 접근할 수 있게
                    .permitAll()
        }
        ```
        
        - `.and()` : 다른 보안 관련 설정을 진행하고자 할 때, 호출하는 함수. `.and()` 함수 호출로 `HttpSecurity` 객체가 반환 되어 다른 설정 진행이 가능하다.
        - `.formLogin()`
            - 일반적인 Login페이지를 만들기 위한 설정 모음
            - 로그인 페이지, 로그인 처리 url등을 설정할 수 있다.
            - 기본적으로 `/login`으로 정해진다.
        - `.loginPage()` : 로그인 페이지로 가게 되는 URL 정의
        - `.defaultSuccessUrl` : 로그인 성공했을 때의 리다이렉션 URL
            - 설정하지 않는다면 루트 url로 리다이렉션되고 index.html이 응답.
    3. UserController 클래스 제작
        1. 로그인되지 않은 상태에서/admin으로 접근하면 /login으로 리다이렉션되는 것을 확인할 수 있다.
    4. andMachers에서 /users/** 를 지워보자.
        
        ```java
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
        						.antMatchers("/home/**")
        						.annoymous()
        						.authenticated()
        }
        ```
        
4. 로그 아웃
    
    ```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
    						.antMatchers()
                  ...
    				.and()
                    .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessUrl("/home")
                    //로그인 정보는 JSESSIONID 쿠키에 저장된다.
                    .deleteCookies("JSEESIONID")
                    .invalidateHttpSession(true)
                    .permitAll()
    }
    ```
    
    - `.logourUrl` : 로그아웃 요청을 보낼 URL. 해당 URL로 POST요청이 들어올 경우, 브라우저에 로그인 관련된 정보가 사라진다.
    - `.invalidateHttpSession()` : `HttpSession`이 유효하지 않도록 설정.
    1. admin.html작성
        
        해당 부분을 추가해준다.
        
        ```html
        <form th:action="@{/user/logout}" method="post">
                <input type="submit" th:value="로그아웃">
         </form>
        ```
        
5. index.html 다르게 보여주기
    1. index.html 수정
        
        ```html
        <body>
        		<div sec:authorize="isAnonymous()">
        		    <h2>Hello World!</h2>
        		    <button onclick="location.href = '/user/login'">로그인</button>
        		</div>
            <div sec:authorize="isAuthenticated()">
                <h3>
                    반갑습니다. <span sec:authentication="name"></span>님!
                </h3>
                <form th:action="@{/user/logout}" method="post">
                    <input type="submit" th:value="로그아웃">
                </form>
            </div>
        
        </body>
        ```
        
        - `sec:authorize="isAnonymous()`
            - thymeleaf의 security 연장
            - 로그인 여부에 따라서 바뀌게 된다.
        - `<span sec:authentication="name"`

# Login 커스텀

Login 기본 구현과 더불어 누가 로그인 했는지, 회원가입은 어떻게 진행하는 지 좀 더 세세하게 로직을 구성하기 위해서 `UserDetailService`를 구현하여 Login 커스텀 작업을 진행한다.

## WebSecurityConfigurerAdapter

1. 설정
    
    ```java
    //사용자 요청 메소드 관리를 위한 설정
    // - 설정이 안되어 있다면 기본적으로 default security password를 사용하게 된다.(터미널 참고)
    // - 사용자 비밀번호 일치 확인, 로그인에 대한 부분이 여기서 일어나게 된다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
    				//user만들기
    				.withUser("user1")
    				.password("user1pass")
    				.roles("USER");
    }
    ```
    
    - `auth.inMemoryAuthentication()`
        - 메모리 상에서 유저 검증하겠다는 의미.
        - 기본적인 스프링부트에 Map 형태로 내장되어 있는 단순한 유저 관리 객체(`UserDetailService`의 구현체)를 사용
    - 해당 정보로 로그인해보자. → 아무일도 일어나지 않는다.
        - 결국 마지막에 build() 메소드가 작동하고 passwordEncorder가 작동하게 된다.
        - passwordEncorder 설정이 되어 있지 않으면 password 매핑이 안되어서 에러가 생성.
2. @Bean 을 통해서 passwordEncorder 객체 생성
    
    ```java
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return PasswordEncoderFactories.createDelegatingPasswordEncorder();
    }
    ```
    
    ```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        auth.inMemoryAuthentication()
    				//user만들기
    				.withUser("user1")
    				.password(passwordEncoder().encode("user1pass"))
    				.roles("USER")
    				.and()
    				.withUser("admin1")
    				.password(passwordEncoder().encode("user1pass"))
    				.roles("ADMIIN");
    }
    ```
    
    - and() 를 통해서 유저를 계속 추가해줄 수 있다.

## UserDetailService

1. 사용자 정보를 다른 DB나 도구에 저장하고 싶을 때, `UserDetailService`인터페이스 구현체를 통해서 유저를 관리할 수 있다.
    1. `UserEntity`, `UserRepository` 작성
    2. infra 디렉토리에 `CustomeUserDetailServic`e 클래스 작성
        
        사용자 데이터 관리하기 위해서 `UserDetailService` 인터페이스를 구현한 구현체를 만든다.
        
        ```java
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            final UserEntity userEntity = userRepository.findByUsername(username);
        //사용자 이름, 비밀번호만 전달, Collection(빈 컬렉션이어도 괜찮다.)
            return new User(username, userEntity.getPassword(), new ArrayList<>());
        }
        ```
        
        - 사용자가 로그인 과정에서 `username`을 제공한다.
        - `username`을 통해 데이터베이스로 받아온 `UserEntity` 정보를 `UserDetails`에 맞는 형태로 조정해서 반환한다.
        - 내부에서 실행되는 `CustomeUserDetailService`에서는 해당 username을 인자로 받아 사용자의 이름, 비밀번호등이 포함된 `UserDetails` 객체를 반환한다.
        - `UserDetails` 객체
            - 유저 정보 관리를 위한 내부에 존재하는 기본적인 인터페이스
            - 해당 인터페이스의 구현체가 스프링 시큐리티에서 사용할 수 있는 형태라고 할 수 있다.
            - 4개의 boolean 반환 함수 : 4개 중에 하나라도 false라면 valid한 계정이 아니다.
                - `isAccountNonExpired()` : 현재 `UserDetails` 안에 들어가 있는 정보의 만료 여부
                    - 애초에 처음부터 만료 기한을 두고 발급 될 때 사용된다.
                    - 일시적인 계정을 만들어야 할 때
                    - 외부 인력이 프로젝트에 일시적으로 들어올 때, 프로젝트 기간만 계정을 사용할 때
                - `isAccountNonLocked()` : 유저가 잠금 상태인지(휴면상태)
                - `isCredentialsNonExpired()` : 비밀번호의 유통기한
                    - ex) “비밀번호 바꾸기까지 10일 남았습니다.”
                - `isEnabled()` : 사용자의 계정이 사용 가능 한지의 여부
                    - 사용자가 명시적으로 계정을 휴면 상태로 돌릴 때,
            - **User라는 객체를 사용해서 사용자 정보를 돌려줄 수 있다.**
    3. 사용자를 임의로 만들어서 동작 구현
        
        ```java
        public CustomUserDetailsService(
                @Autowired UserRepository userRepository,
                @Autowired PasswordEncoder passwordEncoder
        ) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            final UserEntity testUserEntity = new UserEntity();
            testUserEntity.setUsername("entity_user");
            testUserEntity.setPassword(passwordEncoder.encode("test1pass"));
            this.userRepository.save(testUserEntity);
        }
        ```
        
        1. security 고치기
            
            ```java
            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                //DB나 다른 도구를 통해서 사용자 정보를 저장하고 싶을 때
                auth.userDetailsService(this.userDetailsService);
            }
            ```
            
        2. `passwordEncoder`를 `@Configuration`으로 바꾸기
            1. `WebSecurityConfig` 클래스와 `CustomeUserDetailService`에서 `passwordEncoder`가 `WebSecurityConfig`의 내부 빈 객체로 있기 때문에, 각각의 빈(Bean) 객체를 만들 때, 서로를 참조하는 circular 문제가 발생한다. 따라서 `WebSecurityConfig`안에 정의된 `passwordEncoder` 객체를 미처 만들지 못하고 교착 상태가 지속된다.

## 회원가입

1. UserController에 /sign-up 경로 추가 및 signup-form.html작성
    
    ```html
    <!DOCTYPE html>
    <html lang="en"
          xmlns:th="http://www.thymeleaf.org"
          xmlns:sec="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="UTF-8">
        <title>Simple Login</title>
    </head>
    <body>
    <form th:action="@{/user/signup}" method="post">
        <input type="text" name="username" placeholder="아이디"><br>
        <input type="password" name="password" placeholder="비밀번호"><br>
        <input type="password" name="password_check" placeholder="비밀번호 확인"><br>
        <button type="submit">회원가입</button>
    </form>
    </body>
    </html>
    ```
    
2. index.html에 회원가입 버튼 추가
3. securityConfig에 sign-up 설정 추가
    
    ```java
    .antMatchers(
                            "/home/**",
                            "/user/signup/**", )
    ```
    
4. UserController에서 POST 메소드의 signup 작성
    
    ```java
    @PostMapping("signup")
    public String signUpPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password_check") String passwordCheck
    ) {
        if (!password.equals(passwordCheck)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
    
        return "redirect:/home";
    }
    ```
    

## 서비스 중에 사용자 정보 받아오기

Spring IoC 컨테이너는 사용자의 요청에 정의된 인증 정보를 `RequestMapping` 에 자동으로 할당해 준다. 사용할 인자를 정의하면, 아래와 같이 가져와서 사용할 수 있다

1. Principal 이라는 객체 사용하기
    1. 어떤 개인이나 업체의 로그인 아이디를 판단하기 위한 인터페이스. 세션 id에 저장되어 있는 정보를 반환
    
    ```java
    @GetMapping("/principal")
    public String home(Principal principal){
    	try {
            logger.info("connected user: {}", principal.getName());
    	} catch (NullPointerException e) {
    				logger.info("no user logged in");
    	}
    	return "index";
    }
    ```
    
2. Autentication 인터페이스 사용
    
    ```java
    @GetMapping("/authentication")
    public String home(Autentication autentication){
        try {
            logger.info("connected user: {}", autentication.getUserName());
        } catch (NullPointerException e) {
            logger.info("no user logged in");
        }
        return "index";
    }
    ```
    
3. `SecurityContextHolder`를 사용
    - `SecurityContext` : 현재 스레드랑 연관되어 있는 보완 관련 정보(사용자 정보)
    - 해당 정보를 `SecurityContextHolder`에서 제공해주는 `getContext()` 메소드를 통해 받아온다.
    - `getAuthentication()` : 바로 위의 `authentication` 객체를 받아올 수 있다.
    - 로그인한 사용자가 없다고 하면 `NullPointerException`이 발생하는 것이 아닌 `annonymousUser`라는 값을 반환해준다.
    
    ```java
    @GetMapping
    public String home(Autentication autentication){
        try {
            SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (NullPointerException e) {
            logger.info("no user logged in");
        }
        return "index";
    }
    ```
    
    1. 스프링 IoC 기능과 조합해서 어플리케이션 어디에서든 사용할 수 있는 `authentication` 변수를 만들자.
        - `SecutiryContextHolder`는 자신이 실행되고 있는 쓰레드를 기준으로 `Context`를 확인하기 때문에 동시에 여러 개의 요청이 들어왔을 때, 같은 유저가 동시에 등장하지 않는다. 쓰레드마다 다르게 등장한다.
        - `AuthenticationFacade` 클래스 작성
            
            ```java
            @Component
            public class AuthenticationFacade {
                public Authentication getAuthentication() {
                    return SecurityContextHolder.getContext().getAuthentication();
                }
            
                public String getUserName(){
                    return SecurityContextHolder.getContext().getAuthentication().getName();
                }
            }
            ```
            
            이를 기반으로 `homeController`를 작성할 수 있다.
            
# OAuth2와 JWT

## Social Login OAuth2

### Single Sign On

![oauth2](https://user-images.githubusercontent.com/59648372/161184120-9d326016-0fa4-45a8-9ef3-61666e24b772.png)


한 번에 로그인으로 여러 개의 서비스를 사용할 수 있게 하는 것.

예를 들어, 서브 도메인과 전체 인증을 관리하는 SSO서버가 존재한다고 보자.

1. 사용자가 하나의 서비스로 이동 및 로그인 요청
2. 서브 도메인에서 SSO 서버로 로그인 요청함. 
    1. 로그인 페이지로 forward
3. SSO 서버에서 로그인 진행 및 로그인 정보를 쿠키로 해당 도메인에 저장.
    1. 더불어 부수적인 토큰을 반환해준다.
4. SSO 서버에서 만들어낸 쿠키는 독립적이기 때문에 요청 서브 도메인에서는 확인할 수 없지만, 반환 받은 토큰을 통해 요청함으로써 쿠키 내용을 응답 받는다.
    1. 해당 쿠키를 자신의 브라우저에 저장한다.
5. 다른 서브 도메인에서 로그인 요청
6. 이미 SSO서버에서는 로그인 완료되어 쿠키를 저장하고 있기 때문에 앞선 과정과 똑같이 토큰을 반환, 해당 서브 도메인에서는 토큰으로 권한을 확인할 수 있다.

### Social Login

기본적으로 한 번의 로그인으로 여러 서비스를 사용한다는 관점에서 Single sign on과 맥락을 같이한다. 다만, Single Sign On은 동일한 서비스 범주 안에서 다양한 하위 서비스를 사용한다면, 소셜 로그인은 연관성 없는 서비스를 대상으로 이뤄진다.

이렇게 다른 서비스로 사용자 정보를 안전하게 전달하기 위해 `OAuth(Open Authorization)`라는 규칙이 정해졌다.

1. 사용자가 클라이언트(서비스)에 접속
2. 클라이언트에게 로그인을 요구
3.  사용자가 전달한 로그인 정보를 제공자(소셜 서비스, oauth auth)의 인증서버로 전달.
4. 인증 서버에서 로그인을 진행하고 토큰을 발행.
5. 유저 정보가 필요할 때, access token을 소셜 서비스 resource서버로 전달.
6. resource서버로부터 접속 요청 사용자의 정보 전달
7. 사용자 정보를 기반으로 서비스 제공 

소셜 로그인은 로그인 기능 자체가 진행되는 것이 아니라, **정보를 갖고 있는 원소유주에게 정보를 요청하는 과정**이라고 말할 수 있다. 또한, 해당 정보를 받고 나서는 받은 유저 정보를 관리하는 부분이 필요하다.

### 현실에서의 Social Login

실제 동작 과정에서는 클라이언트에서 로그인이 진행되기 보다는 해당 로그인 사이트로 이동해서 진행하게 된다. 

- 사용자가 로그인하길 원한다는 정보를 url의 query parameter와 같은 형태로 서비스 인증 서버에 전달하게 된다.

이후, 사용자가 로그인을 진행했다는 사실을 소셜 로그인 사용하는 서비스에 알려주게 되며 어플리케이션의 callback url을 통해서 정보를 받게 된다.

## Naver Login 구현하기

### Naver application지정

1. Naver Developers 어플리케이션 등록
    1. 이메일 주소 선택
    2. API 서비스 환경
        1. WEB 선택
            1. 서비스 URL : http:localhost:8080
            2. 콜백
                1. http://localhost:8080/login/oauth2/code/naver
                2. http://localhost:8080/login/callback

### 구현

1. build.gradle
    
    ```java
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
    implementation 'org.springframework.security:spring-security-oauth2-authorization-server:0.2.2'
    ```
    
2. yml파일 작성
    
    ```yaml
    spring:
      datasource:
        url: jdbc:h2:mem:testdb
        driver-class-name: org.h2.Driver
        username: sa
        password: password
      jpa:
        database: h2
        database-platform: org.hibernate.dialect.H2Dialect
      security:
        oauth2:
          client:
            registration:
              # 복수로 여러 서비스를 등록할 수 있다. naver, kakao, google, ...
              naver:
                client-id: <your_client_id>
                client-secret: <your_client_secret>
                # oauth2 서버에서 보내주는 access 토큰을 전달받는 uri 설정
                # - 네이버 어플리케이션에 등록했던 redirection uri
                redirect-uri: "{baseUrl}/{action}/oauth2/code/{registrationId}"
                # 정보 제공 방식에 대한 설정. 단순 로그인뿐만 아니라 다른 형태로 사용자 정보를 받아오는 경우
                authorization-grant-type: authorization_code
                scope: email
                client-name: Naver
            provider:
              naver:
                # 인증해달라는 요청을 어디에 보낼 것인지
                authorization-uri: https://nid.naver.com/oauth2.0/authorize
    			# Access Token 발급, 갱신, 삭제 등을 위한 URI
                token-uri: https://nid.naver.com/oauth2.0/token
                # 사용자 정보 요청을 위한 경로
                user-info-uri: https://openapi.naver.com/v1/nid/me
                # 요청을 보낸 후, 사용자에 대한 정보가 json응답으로 돌아올 때,
                # 돌려받은 데이터에서 어떤 key값을 username으로 사용할 것인지 지정한다.
                # - 네이버의 경우 response 안에 상세 정보가 들어있다.
                user-name-attribute: response
    ```
    
    - `client`: OAuth2 Client, 즉 소셜 로그인 기능을 사용하고자 하는 어플리케이션이 필요로 하는 설정 내역.
    - `provider`: OAuth2 제공자에 대한 정보 설정.
    - `registration`: OAuth2 Client로서 필요한 정보 설정.
    
     네이버 외에 다른 서비스 (예: Google, Facebook 등) 의 경우 미리 준비되어 있어서 이 경우, `provider` 는 생략할 수 있다.
    
3. 네이버 요청 처리를 위한 빈 객체 만들기 (NaverOAuth2Service)
    
    ```java
    //oauth의 과정이 일어난 다음, 사용자 정보를 받아온 상태에서 호출되는 함수
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
    
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String userNameAttributeName = userRequest
                    // yml에 정의했던 registration uri에 대해서 어떤 uri를 기준으로 들어온 것인지 묻는 과정
                    // - 현재 스레드에서 진행된 요청이 어떤 uri를 기준으로 진행된 건지
                    .getClientRegistration()
                    //정의했던 yml파일에 provider 정보를 받아오는 과정
                    .getProviderDetails()
                    //유저 정보를 어디에서 받아 오는지
                    .getUserInfoEndpoint().getUserNameAttributeName();
    
           logger.info(oAuth2User.getAttributes().toString();
    			 return oAuth2User;
        }
    ```
    
    - `OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate` : 사용자의 요청을 대신 받아와 줌.
    - `OAuth2User oAuth2User` : 사용자 정보를 oAuth2User에 저장.
    - `userRequest.getClientRegistration().getRegistrationId();` : 현재 진행중인 서비스를 문자열로 받는다.
        - {registrationId = ‘naver’} 이런식으로 들어있다.
    - `String userNameAttributeName` : Oauth2로그인 시 키값
4. WebSecurityConfig 설정
    
    ```java
    .and()
    // 소셜로그인이 되었다면 로컬 로그인을 한 것처럼 취급하겠다는 의미.
    .oauth2Login()
    .userInfoEndpoint()
    //NaverUserService가 들어가게 된다.
    .userService(this.oAuth2UserService)
    //oauth2Login 자체도 일종의 builder로 작동해서 일단 userService를 구성하고 나서
    //성공여부 및 결과행동을 판단하게 된다.
    .and()
    .defaultSuccessUrl("/home")
    ```
    
5. html파일 수정
6. 반환값 설정
    
    ```java
    //oauth의 과정이 일어난 다음, 사용자 정보를 받아온 상태에서 호출되는 함수
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            ...
    
          Map<String, Object> attributesMap = oAuth2User.getAttributes();
    			Map<?, ?> responseMap = attributeMap.get("response");
    			DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                    //기본적으로 어떤 권한을 가지고 있는지 지정
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    responseMap,
    								"email");
    			return defaultOAuth2User;
        }
    ```
    

## JSON Web Token

![JWT](https://user-images.githubusercontent.com/59648372/161184072-fa415364-2157-4498-8b1d-945edd12d050.png)


공개키 암호화를 이용해서 사용자 권한을 안전하게 주고 받는 용도.

- header
    - JWT를 만들기 위해 사용한 알고리즘 정보
- payload
    - claim 사용자 권한 정보를 담는 부분
- signature
    - JWT가 유효한지 검증하는 부분.

JWT 같은 경우에는 비밀키를 사용해서 암호화를 진행하고 공개키를 통해서 복호화를 한다.

- 비밀키를 이용해 암호화 : JWT 토큰 발행할 수 있는 대상이 한정적임을 의미
- 공개키를 통해 복호화 : 암호를 해석할 수 있는 대상에 대해 제한이 없음

즉, 일종의 자격증처럼 제공할 수 있는 대상이 한정적이다 보니, 사용자의 권한이 같이 포함되면 변조하기가 힘들다는 장점이 있다.

# OAuth2 서버 만들어보기

### 서버 구성

- Authorization 서버
    
    ```java
    implementation 'org.springframework.security:spring-security-oauth2-authorization-server:0.2.2'
    ```
    
- Resource 서버
    - 외적인 페이지가 존재x
    - Authorization에서 발급받은 토큰을 검증하고 사용자가 필요로 하는 데이터를 반환하는 서버
- Client 서버
    
    ```java
    //resource서버에 요청을 보내기 위해 추가된 의존성 : webflux, netty
    	implementation 'org.springframework.boot:spring-boot-starter-webflux'
    	implementation 'io.projectreactor.netty:reactor-netty:1.0.9'
    ```
    

### 서버 간단 구현

- Authorization 서버
    - `RegisteredClientService` : 로그인 제공하는 클라이언트를 다루기 위해 서비스 객체 생성.
    - `AuthServerConfig` : 필터 함수, 암호화 진행을 위한 키 발급 함수 등을 정의
- Client 서버
    - `/home` : 로그인하기 위해서 Authorization 서버로 리다이렉션이 진행된다.
    - `/` : root로 들어오는 요청은 사용자가 로그인되어 있어야 한다.
        - 사용자 토큰 검증 후, resource서버로 데이터 요청(WebClient)
