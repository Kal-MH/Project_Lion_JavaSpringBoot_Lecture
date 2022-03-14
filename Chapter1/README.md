# Chapter 1.

# Hello World in java

## Java 프로젝트

### Java Run을 위해 기억해야 할 키워드 3가지

- `JVM`(Java Virtual Machine)
    - 자바 `바이트코드`를 받아서 기계어로 변환, CPU에서 돌릴 수 있도록 도와주는 자바가상머신. 자바 가사머신은 플랫폼마다 다르게 기계어를 작성하여 배포한다.
    - 자바 `바이트코드`는 자바 가상기계에서만 실행되는 기계어로서, 어떤 CPU와도 관계없는 바이너리 코드이다.
- `JRE`(Java Run Environment)
    - 자바 가상머신은 환경만 제공할 뿐, 기타 필요한 표준라이브러리는 제공하지 않는다. 실행을 하기 위한 환경은 `JRE`에 포함되어 있다.
    - 따라서, `JRE`에는 `JVM`과 실행에 필요한 다양한 라이브러리를 포함하고 있다.
- `JDK`
    - `JVM`, `JRE`를 모두 포함하며, 자바 언어를 바이트코드로 변환하는 **자바 컴파일러**를 포함하고 있다.
    - [[JDK 다운로드 사이트]](https://jdk.java.net/)

## Code Editor와 IDE

## Docker Setting

- OS 위에 컨테이너를 올려 어플리케이션을 활용. 가상화 기술
- 스프링부트는 웹서비스를 위해서 사용하는 웹 프레임워크. 이를 위해서 Mysql, redis rabbitMQ 등의 다양한 서비스가 필요한데, 하드웨어 OS위에 직접 설치해서 모든 환경변화, 변수에 대해서 대응하기 보다는 동일한 환경에서 일괄적으로 관리하는 것이 필요하다.
- [[설치 사이트]](https://www.docker.com/get-started)

```java
[[Docker Error Handling]](https://www.lainyzine.com/ko/article/a-complete-guide-to-how-to-install-docker-desktop-on-windows-10/)

도커를 윈도우에서 실행하기 위해서는 WSL2라는 윈도우에서 리눅스 기능을 사용하게 하는 CLI라고
생각하면 된다.

도커는 WSL2위에서 동작하기 때문에 해당 소프트웨어를 설치해줘야 한다.
```

## Client - Server Model

1. 스프링 부트
    
    (Web) Application Framework
    
    HTTP요청 등을 받으며 요청에 따른 응답을 돌려주는 웹 애플리케이션을 만드는 프레임워크
    
2. `HTML`, `CSS`, `JS`
    
    Internet의 Corner stone. 즉, **인터넷의 주축돌**이 된다.
    
    - `HTML` : 브라우저에 표시될 내용을 기술하는 언어. 그림그리기 전의 설명서
    - `CSS` : 표현되는 형식을 정의하는 언어. HTML 문서 상에서 각각의 태그들의 디테일을 명시해 놓음.
    - `JS` : 브라우저에 동작을 제공하는 언어. 유저 동작에 따른 여러 반응들을 정의한다.
    
    인터넷 브라우저는 이 세 언어를 사용해서 여러 웹 문서들을 보여준다. 즉, 세 언어들를 해석하여 실행하는 동작 실행 환경이 된다.
    
3. 프론트엔드 vs 백엔드
    1. 프론트엔드 : 브라우저에 보이는 걸 중점으로 공부하는 개발자. 페이지를 제작하고 UI/UX를 겸하는 작업이 된다.

## SprintBoot 프로젝트 시작하기

[start.spring.io]

1. 오류
    1. JDK가 설정되어 있지 않음
        
        ```java
        Project Structure -> Project -> JDK 지정
        ```
        
    2. run 버튼 활성화 x (실행 버튼 활성화란 키워드로 구글 검색)
        1. gradle 파일이 실행되어 sync가 맞춰졌는가
        2. run configuration에 직접 추가하자.[[블로그]](https://mchch.tistory.com/158)
