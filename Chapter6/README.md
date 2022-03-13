# Chapter 6.

# Spring Boot Properties

## 설정 파일 작성

실제 서비스를 실행하는 데는 여러가지 다양한 환경을 구성할 수 있다. 서버 컴퓨터 안에 도커같은 컨테이너를 올린다거나, 컨테이너를 통해 AWS 클라우드 서비스를 사용한다던가, 아니면 AWS 클라우드 서비스의 ec2혹은 관계형 데이터베이스 서비스를 사용하는 등으로 다양하게 구성할 수 있다.

```java
Spring 버전 이후로 설정파일이 변경되었다.

YAML 또는 properties 파일
- spring.profiles.active로 사용할 파일 결정
- 2.4.0 이전 : spring.profiles.include 사용
- 2.4.0 이후 : spring.config.activate.on-profile 사용
```

스프링 어플리케이션을 실행시키면 다음과 같은 로그를 볼 수 있다.

![run](https://user-images.githubusercontent.com/59648372/158056890-4737e747-2d9a-4610-ab1d-7cd5b61a69d0.png)

active profile 세팅한다면, profile에 세팅한 정보를 택해서 어플리케이션을 실행한다. 현재는 정해지지 않았음으로 기본값으로 실행한다는 문구를 볼 수 있다.

## Spring Boot Profiles

### Multi Document Yaml

하나의 Yaml 안에 여러 개의 문서를 넣을 수 있도록 해준다.

![profiles](https://user-images.githubusercontent.com/59648372/158056889-e244cc8f-70d1-4562-8588-22f6392420a8.png)

1. 현재 실행할 때 사용할 `local`이라는 설정을 `profiles` 안에 지정한다.
2. 현재 설정값을 `local`이라고 이름 붙이고, 실행할 때 해당 설정값을 사용한다.
    
    ![run2](https://user-images.githubusercontent.com/59648372/158056888-ab391b06-7224-4b27-9c68-292fed5b67d7.png)
    

만약, 개발 모드와 상용 모드를 구분하고 싶다면, 마찬가지로 이름을 바꿔주고 원하는 설정값들로 채워 넣으면 된다.  데이터 꼬임을 방지하기 위해 개발할 때의 데이터베이스와 테스팅할 때의 데이터베이스를 다르게 설정할 수도 있다.

```yaml
---
spring:
  config:
    activate:
      on-profile: test
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb
    username: sa
    password: password
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
```

`h2 Driver`를 사용하기 위해서는 `build.gradle` 파일에 `dependency`를 추가해줘야 한다.

```yaml
dependencies {
  ...
  runtimeOnly 'mysql:mysql-connector-java'
  **runtimeOnly 'com.h2database:h2'**
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

결국에는 데이터베이스가 어디에 있는 지에 따라서 ip주소를 조정해줄 수 있고, 이를 통해서 여러 모드로 나눠서 개발을 진행할 수 있다.

또한, Multi Document Yaml 기능이 있기 때문에 설정파일을 여러 군데에 나눠서 작성할 수 있다.

<aside>
💡 application-[setting_name].yml

</aside>

위와 같이 `local`이라는 이름으로 설정값을 세팅해 놓고 싶다면 `application-local.yml` 이라고 명명한 파일에 작성하면 된다. 또 이렇게 파일명을 설정했다면 `activate-on` 부분은 제거해도 정상 작동한다.

### jar 파일로 모드 설정하기

gradle창에 보면 다음과 같이 `bootjar`를 통해서 `jar`파일을 만들 수 있다.

![gradle](https://user-images.githubusercontent.com/59648372/158056887-995c53b1-0cc5-4fe0-90bb-f8be1de52abf.png)

그리고 만들어진 `jar`파일은 `build/libs` 폴더 안에서 찾을 수 있다.

![jarfile](https://user-images.githubusercontent.com/59648372/158056891-7cadb02c-aba4-447a-84f0-23a66a49c6a3.png)

터미널 상에서 다음과 같은 명령어를 통해서 jar파일을 실행시킨다.

```yaml
$> java -jar ./build/libs/jpa-0.0.1-SNAPSHOT.jar

$> java -Dspring.profiles.active=test -jar ./build/libs/jpa-0.0.1-SNAPSHOT.jar

$> export SPRING_PROFILES_ACTIVE=test
$> SPRING_PROFILES_ACTIVE=test java -jar ./build/libs/jpa-0.0.1-SNAPSHOT.jar
```

### Error

```yaml
Error: Could not find or load main class .profiles.active=test
Caused by: java.lang.ClassNotFoundException: /profiles/active=test
```

### @Component, @Profile 어노테이션 활용

작성하는 클래스들은 @Component 어노테이션을 통해서 Bean으로 만들어져 IoC 객체로 넘겨지게 된다. @Profile 어노테이션을 활용하면 설정하는 모드에 따라 원하는 부분만 Bean 객체로 만들 수 있다.

## Configuration을 통한 Bean 생성

### @Configuration

설정을 담기 위해 만들어진 어노테이션. 해당 어노테이션이 붙은 클래스는 여러 설정을 담고 있다는 의미를 지닌다.

해당 어노테이션을 사용하면 외부 라이브러리 또한 Bean 객체로 넘겨서 사용할 수 있게 된다.

```java
@Bean
public Gson gson() {
    return new Gson();
}
```

### Custom을 통해 환경변수 값 설정

```yaml
custom:
  property:
    single: custom-property
    comlist: comma, sparated, list, to, value
# custome.property.single
# custome.property.comlist
```

스프링부트와는 연관성이 없지만, 상황에 따라서 바뀔 수 있는 부분들에 대한 것도 설정해줄 수 있다.(예를 들어, 외부 API를 사용해야 할 때)

```java
@Value("${custom.property.single}")
private String customeProperty;

@Value("${custom.property.comlist}")
private List<String> customeCommaList;
```

다음과 같이 `@Value` 을 통해서 custom에 설정해 놓은 값이 들어가게 된다.

# Logging

## Loggin의 기본 개념들

로그는 기본적으로 프로그램 진행 상황을 확인하기 위해 남긴다. 그리고 영어로 작성하는 것이 보편적이다. 또한 어떤 일이 일어났는지를 구체적으로 명시한 내용을 메세지로 남긴다.

### Log Level

- trace : 가장 사소한 진행 상황, 상용 서비스에서는 제거해주는 게 좋다.(깃헙에는 올리지 않음)
- debug : 어떤 행위가 이뤄졌다.
- info
- warn : 미래에는 문제가 생길 수도 있다.
- error : 위험한 수준의 메세지

현재 기본값은 info이고, 이와 레벨이 동일하거나 좀 더 위험한 수준의 메세지까지만 출력된다. 따라서 trace, debug는 출력되지 않았다. 만약 이 부분을 조정하고 싶으면 yml파일에서 가능하다.

```yaml
logging:
  level:
    root: debug

전체 어플리케이션에 적용된다.

logging:
  level:
    root: debug
    dev.kalmh: info

# private static final Logger logger = LoggerFactory.getLogger(ProfileComponent.class);
```

클래스에서 logger의 이름을 getLogger()호출과 동시에 초기화하는데, 그 이름이란 결국 패키지명까지 포함한 클래스이름이 된다. 그 이름을 기반으로 패키지를 기준으로 로그레벨을 설정할 수도 있다.

혹은 터미널에서 바로 설정 가능

```yaml
java -jar ... -trace
java -jar ... -debug
```

## [[Logback]](https://logback.qos.ch/)

로그는 서비스가 실행중이 아니더라도 훗날을 위해서 살펴보기 위한 목적도 지닌다.

```yaml
[unix 기준]
$> nohub java -jar jarfilename.jar > log_files.log
---> 하지만 이런 방법은 계속해서 log_files내용이 쌓이기 때문에 별로 효율적이지 않은 방법이다.
```

이런 로그파일을 효율적으로 관리하기 위해서 로깅 라이브러리를 사용할 수 있다. 

### logback-spring.xml 파일 설정

# Spring AOP(심화)

# Validation


---

(출처 - ProjectLion The Origin : Springboot 강사 박지호)
