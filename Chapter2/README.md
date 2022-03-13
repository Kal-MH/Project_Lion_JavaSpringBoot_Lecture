# chapter 2.

# Lecture1. Maven & Gradle

[2-1_Maven_Gradle.pptx](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ea7f038c-f4b3-4e8c-9d57-d0741215c354/2-1_Maven_Gradle.pptx)

### 버전 이해

Major - Minor - Patch

1. Major
    
    대규모 변경사항이 있을 때, 버전이 올라간다.
    
    하위 호환이 불가능함
    
    ex) release
    
2. Minor
    
    Major 버전은 유지, 새로운 기능 추가 시 변화
    
    상위, 하위 호환 가능
    
3. Patch
    
    기능적인 변경사항이라기 보다, 버그 수정을 나타냄.
    
- SNAPSHOT : 현재 고정된 버전이 아님. 개발 단계에서 빠르게 변경 사항들이 생기고 변할 수 있음.

<aside>
💡 [[Maven Repository]](https://mvnrepository.com/)

</aside>

# Lecture2

[2-2_Start_Spring_Boot.pptx](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/eb8400cf-f2c6-419d-be6b-6c5f7449ba72/2-2_Start_Spring_Boot.pptx)

## Java & Jar

## Wep Application의 구조

일반적으로 사용자가 브라우저에 주소를 치면 물리적 컴퓨터(서버)에게 자료를 요청하게 된다.

<aside>
💡 ip주소:Port 번호

</aside>

네트워크 상에서 하나의 컴퓨터를 가리키는 ip주소와 컴퓨터 내부의 프로세스를 가리키는 Port 번호를 통해서 서버를 지정해 원하는 데이터를 요청하게 된다.

일반적으로 3개의 티어로 나누어서 구성하게 된다.

- **Presentation Layer**
    
    UX/UI 에 맞닿아 있는 레이어.
    
    사용자와 직접적으로 맞닿아 있는 부분
    
- **Logic**
    
    요청을 처리하고 결정을 내리는 부분
    
- **Data**
    
    데이터를 저장하고 불러오는 부분
    

![server.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9f8567c3-e62c-4465-80d7-55f80f6dbda7/server.png)

보통 사용자로부터 요청이 온다면, 다음과 같이 동작하게 된다.

1. `Dispatcher Servlet`이 요청을 받고 `Controller`에 넘겨준다
2. `Controller`는 요청을 검증하고 `Service`로 요청을 넘겨준다.
3. `Service`는 상황에 따라 데이터 조작을 위해 `Repository`로 요청을 한다.
4. 외부에 있는 `database`를 통해 `Repository`는 실제 데이터를 요청하고 받는다.

### 웹 서버

전통적으로는 이미 웹 서버 안에 만들어진 페이지들을 보여주는 방식이었다. 혹은 다른 **웹 애플리케이션 서버**에 요청을 전달해서 사용자가 원하는 데이터를 찾는다.

```java
웹 서버와 웹 애플리케이션 서버는 서로 다르다.

웹 서버는 사용자에게 요청을 받고, 요청을 전달하거나 파일을 다시 사용자에게
돌려주는 기능을 수행한다.
웹 애플리케이션 서버는 서비스를 제공하기 위한 기능들을 갖춘 서버이다.
```

### 웹 애플리케이션 서버

웹 애클리케이션을 열기 위한 서버.

웹 애플리케이션 서버 중 하나인 Tomcat의 경우, Spring을 통해 만들어진 `WAR`파일 혹은 스프링부트를 사용해서 만들어진 파일들을 실행할 수 있다. 

<aside>
💡 Spring을 통해 만들어진 war 파일은 Tomcat같은 웹 애플리케이션 서버가 있어야 실행이 가능했다. Spring boot는 이런 웹 애플리케이션 서버 기능이 내장된 것으로써 jar파일을 형태로 실행이 가능하다.

</aside>

Nginx를 웹 서버로 두고 정적파일과 springboot 웹 애플리케이션 서버를 연결하거나, 물리적 하드웨어에 바로 스프링부트를 놓거나 혹은 사이에 docker를 놓기도 한다.

### 스프링 부트

스프링부트의 결과물은 `jar(jar ARchive)`파일이 나온다.

Java로 작성 후, 컴파일된 자바 바이트 코드, 실행을 위해 필요한 다양한 자원을 배포를 위하여 모아놓은 파일의 형태(압축파일)의 결과물을 얻을 수 있다.

<aside>
💡 [[CLI에서 JAR파일 실행하기]](https://velog.io/@kekim20/Spring-Boot-%EB%B9%8C%EB%93%9C%ED%95%98%EC%97%AC-jar%ED%8C%8C%EC%9D%BC-%EC%83%9D%EC%84%B1%EC%8B%A4%ED%96%89%ED%95%98%EA%B8%B0)

</aside>

# Lecture3

[2-3_Spring_IOC.pptx](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/118f7e05-b9a0-4e26-b543-cf246ccebe44/2-3_Spring_IOC.pptx)

## Interface

- 함수의 인자와 반환 값은 interface를 활용하자.

### IoC Container(Inversion of Controle, 제어 역전)

일반적으로 개발자가 있고 스프링이 있다고 생각해보자.

스프링이 있기 전에는 정해져 있는 라이브러리 코드를 개발자가 가져와서 개발을 했다. 하지만 스프링 IoC가 등장하면서 **제어 역전**이라는 개념이 생겨났다. 즉, 프레임워크가 자신이 해야 하는 역할에 대해서 개발자의 코드를 가져다가 사용하게 되는 것이다.

스프링 프레임워크는 웹 애플리케이션 프레임워크로써 웹 어플리케이션이 가질 수 있는 기능에는 요청을 받고 응답을 보내는 것이 가장 클 수 있다고 할 수 있다. 통신을 하는 부분들은 거의 항상 동일하겠지만, 요청에 대한 액션은 어떻게 할 것인지는 매번 바뀔 것이다. 그렇다면 요청에 대한 응답 부분만 개발자가 작성을 해 놓는다면 스프링에서 그 코드를 가져와 적절한 곳에 배치하면 될 것이다.

따라서 IoC Container는 개발자가 작성한 코드와 설정 정보를 합쳐서 스프링 프레임워크 기반의 애플리케이션이 작동할 때, 필요한 객체들을 만들어준다. 

<aside>
💡 스프링 프레임워크를 잘 활용한 프로젝트라면 new키워드가 최소화 되어 있다.

</aside>

즉, 개발자가 작성한 코드와 설정 정보를 합쳐서 완전한 웹 서비스를 만든다. 스프링에서는 이렇게 만들어진 객체를 `Beans`라고 하고 `Beans`는 **IoC Container에서 관리하는 객체**라고 말할 수 있다.

IoC Container에서 이미 만들어져 있는 Bean객체를 가져와서 사용할 수도 있는데, 이렇게 필요한 시점에 Bean객체를 다시 가져와서 사용하는 것이 **Dependancy Injection(DI)**라고 한다.
