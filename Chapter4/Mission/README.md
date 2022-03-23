# Basic Mission

간단한 커뮤니티 사이트를 만들어봅시다.

1. 게시판에 대한 CRUD를 작성.
    - 게시판은 게시판 이름에 대한 정보가 포함되어야 합.
2. 게시글에 대한 CRUD 작성
    - 제목, 본문, 작성자, 비밀번호
    - 작성된 게시글은 게시판에 속해 있어야 함.
    - 게시글 삭제를 위해 게시글의 비밀번호를 받아야 함

- 관계형 데이터베이스의 PK와 같은 정보가 포함하여 각 자원을 쉽게 식별할 수 있도록 하자.
- `post`와 `board`의 관계가 URL 상에 잘 나타날 수 있도록 설계하자.

# Challenge Mission

게시글 파일 첨부하기

1. Spring Boot로 보내진 multipart/form-data 의 요청에서 파일을 어떻게 저장할지를 고려해보자.
    - 게시글을 만들기 위한 CRUD 작업 자체는 Content-Type: application/json 을 유지할 수 있도록 하자.
    - 게시글을 제시할 웹 페이지에서는, 게시글의 content를 HTML문서에서 확인할 수 있다고 가정하자.
        - 만약, Thymeleaf를 사용한다면,
          ```
            <h1 th:text="${post.title}" />
            <!-- 게시글에 이미지가 있다면 아래 element 내부에서 출력될것 입니다. -->
            <div th:utext="${post.content}" /> 
            <p th:text="${post.writer}" />
          ```
