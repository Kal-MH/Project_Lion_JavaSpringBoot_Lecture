# Mybatis 사용해보기

## Mybatis

데이터베이스 관련된 자바 프레임워크.

```cpp
database를 사용하는 이유?

→ 자바 클래스를 통해서 데이터를 다루게 되는데, (POJO) 
이런 데이터는 메모리에 저장이 된다. 
즉, spring boot앱이 종료되는 순간 모두 사라진다.

→ 여러 서버 프로세스가 같은 기능을 하면서 Data를 공유해야
 하는데 데이터베이스를 통해서 이와 같은 일이 가능해진다.
```

따라서, 스프링부트 앱의 여러 기능에서 사용할 데이터를 외부 Database에 저장하고, 여러 프로세스에서 공유하면서 사용할 수 있도록 활용하는 것이 필요하다.

### Mybatis 사용

1. java 함수를 sql 선언문과 연결 지어 사용
2. java 클래스를 이용하여 sql 결과를 받거나, sql 선언문에서 사용할 인자를 전달한다.

따라서 테이블의 row 하나를 클래스의 객체 단위로 저장할 수 있고 이를 `Dto` (값을 나타내기 위한 object, 데이터를 주고 받기 위한 object)라고 부르기 시작했다.

Mybatis는 이런 sql 선언문을 xml파일 형태로 저장하게 된다. 어플리케이션이 실행될 때, xml 파일을 실행하면서 데이터를 가져오게 되고, 미리 구현해 놓은 자바 코드의 함수와 연결을 시켜준다.

### application.yml

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc.mysql://127.0.0.1:3306/demo_schema
    username: demo-user
    password: demo-user-password1!

mybatis:
  mapper-locations: "classpath:mybatis/mappers/*.xml"
  configuration:
    map-underscore-to-camel-case : true
```

## Mybatis로 Database 사용해보기

### 프로젝트 구조

- dao(data access object)
    - 데이터 주고받는 기능을 위한 클래스들을 부르는 말
    - 다른 부분들과 소통을 하기 위한 클래스
    - ex) 레포지토리
- dto
    - 실제 데이터를 담기 위한 오브젝트
- mapper
    - 인터페이스
    - xml 설정에 따라서 연결될 부분

### post-mappers.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="deb.kalmh.mybatis.mapper.PostMapper">
    <insert id="createPost" parameterType="dev.kalmh.mybatis.dto.PostDto">
        insert into POST(title, content, writer, board)
        values (#{title}, #{content}, #{writer}, ${board})
    </insert>
</mapper>
```

1. namespace를 통해서 설정해준 PostMapper 인터페이스를 참조하여 id의 createPost를 찾아가게 된다.
2. values의 파라미터 지정하기
    1. parameterType을 통해 지정해준 PostDto의 멤버 필드 이름을 #{}으로 지정한다.
        1. #{} → ‘’ 로 대치된다고 볼 수 있다.
    2. 문자열이 아닌 경우는 ${}를 사용한다.

## Mybatis 제어문

## Auto Generated keys

board-mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="deb.kalmh.mybatis.mapper.BoardMapper">
    <insert
            id="createBoard"
            useGeneratedKeys="true"
            keyProperty="id"
            parameterType="deb.kalmh.mybatis.dto.BoardDto"
    >
        insert into board(name) values(#{name})
    </insert>
</mapper>
```

- userGeneratedKeys
    - db에서 만들어준 id값을 바로 활용하겠다는 뜻
- keyProperty
    - 어떤 속성을 사용할 것인지 활용

# ORM

## 관계형 데이터베이스의 한계

관계형 데이터베이스에서는 `foreign key`라는 관점에서 테이블이 서로 연결되어 있다. 하지만 이러한 자료의 형태가 객체지향관점에서는 맞지 않을 수 있다.

다른 데이터의 `id`값을 클래스가 가지고 있으면서 이를 탐색해서 찾기 보다는 내부 클래스로 가지고 있어야 하는 경우도 존재하기 때문이다. 

![ORM](https://user-images.githubusercontent.com/59648372/158056724-0705cb9d-fc74-4fa2-af1e-affeae9fb77e.png)

(출처 - ProjectLion The Origin : Springboot 강사 박지호)

## Object Relational Mapping

데이터베이스를 어떤 식으로 다루는 지에 대한 부분. 즉, 관계형 데이터를 객체로 표현하는 프로그래밍 기법이라고 말할 수 있다.

즉, `OOP(Object Oriented Programming)`에서 쓰이는 객체라는 개념을 구현한 클래스와 `RDB(Relational DataBase)`에서 쓰이는 데이터인 테이블을 자동으로 매핑(연결)하는 것을 의미한다. 이 둘은 처음부터 호환 가능성을 두고 만들어진 것이 아니기 때문에 불일치가 발생하는데, 이를 `ORM`을 통해 객체 간의 관계를 바탕으로 `SQL`문을 자동으로 생성, 불일치를 해결한다.

따라서, `ORM`을 이용하면 따로 `SQL`문을 짤 필요 없이 객체를 통해 간접적으로 데이터베이스를 조작할 수 있다.


### 장점

- **객체지향적인 코드 사용 가능**
    
    `SQL`문이 아닌 클래스의 메서드를 통해 데이터베이스를 조작할 수 있어, 개발자가 객체 모델만 이용한 프로그래밍 하는데 집중할 수 있게 한다.
    
- **재사용, 유지보수, 리팩토링**
- **DBMS 종속성 하락**
    
    객체 관계를 바탕으로 SQL문이 자동으로 생성될뿐 아니라 객체의 자료형 타입까지 사용할 수 있기 때문에 `RDBMS` 교체하는 큰 작업에 대한 리스크도 줄어든다.
    

### 단점

- **신중한 설계가 필요**
    
    프로젝트의 복잡성이 커질수록 난이도가 올라가고, 잘못된 설계로 인한 속도 저하 및 일관성이 무너지는 문제점들이 발생할 수 있다. 
    
    또한, `ORM`방식을 사용한다 하더라고 속도를 위한 별도 튜닝으로 SQL문을 작성해야 할 수도 있다. 
    
- **객체 - RDBMS 관계 간의 불일치 여전히 존재**
    - **상속성**: `RDBMS`에는 상속 개념이 없다.
    - **세분성** : 경우에 따라서 데이터베이스에 있는 데이블보다 더 많은 클래스를 가진 프로그램이 생길 수 있다.
    - **일치** : `RDBMS`에서의 동일성은 기본적으로 **PK**를 통해서 정의되지만, 자바에서는 **객체식별**(a==b)와 **객체 동일성**(a.equals(b)) 모두 정의한다.
    - **연관성** : 객체지향언어는 객체의 참조(reference)를 통해 연관성을 나타내고, 이는 방향성이 있다. 하지만 `RDBMS`에서는 외래키를 통해 나타내며 이는 방향성이 없다.
    - **탐색 :** 자바의 경우, 그래프를타고 하나의 객체에서 다른 객체로 레퍼런스를 참고하며 탐색하지만, `RDBMS`의 경우, `SQL`문과 함께 JOIN을 통해서 탐색을 진행한다.

## JPA(Jakarta Persistence API) & Hibernate

> JPA 자체가 ORM 기술 자체를 구현하는 것은 아니다. 이미 존재하는 자바의 클래스를 가져다가 데이터 상에서 어떻게 존재하게 될 지를 정의하기 위한 일련의 어노테이션이라고 할 수 있다.  따라서, JAP 자체는 관계형 데이터를 객체로 표기하는 기능 뿐이라고 말할 수 있다.
> 

### JPA

자바 ORM 기술에 대한 API.

특정기능을 수행하는 라이브러리가 아닌 말 그대로 인터페이스로, 자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스이다.

JPA를 정의한 `javax.persistence`패키지의 대부분은 `interface`, `enum`, `Exception`, 그리고 각종 `Annotation`으로 이루어져 있다. 예를 들어, JPA의 핵심이 되는 `EntityManager`는  `javax.persistence.EntityManager`라는 파일에 `interface`로 정의되어 있다.

![jpa](https://user-images.githubusercontent.com/59648372/158056701-1b6df2b0-5697-4ffa-b621-3680a965c4f5.png)

                 [출처 : [https://ultrakain.gitbooks.io/jpa/content/chapter1/chapter1.3.html](https://ultrakain.gitbooks.io/jpa/content/chapter1/chapter1.3.html) ]

### Hibernate

일련의 인터페이스 모음인 JPA를 구현한 구현체의 한 종류. 즉, 인터페이스를 직접 구현한 라이브러리이다. 

![hibernate](https://user-images.githubusercontent.com/59648372/158056699-b58424a2-54c5-4af1-b41e-ad8b92973972.png)

(출처 - Documentation - 5.6 - Hibernate ORM)

JPA의 핵심인 `EntityManagerFactory`, `EntityManager`, `EntityTransaction`을 Hibernate에서는 각각 `SessionFactory`, `Session`, `Transaction`으로 상속 받고 각각 `Impl`로 구현하고 있음을 확인할 수 있다.

### Reference

[Documentation - 5.6 - Hibernate ORM](https://hibernate.org/orm/documentation/5.6/)

[JPA는 도대체 뭘까? (orm, 영속성, hibernate, spring-data-jpa)](https://velog.io/@adam2/JPA%EB%8A%94-%EB%8F%84%EB%8D%B0%EC%B2%B4-%EB%AD%98%EA%B9%8C-orm-%EC%98%81%EC%86%8D%EC%84%B1-hibernate-spring-data-jpa)

---

# JPA 활용하기

## Entity 작성하기

1. 새로운 schema, user정의 및 권한 설정
    1. username : demo-jpa
    2. password: demo-jpa-password1!
2. yml 작성

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/demo_jpa_schema
    username: demo-jpa
    password: demo-jpa-password1!
  jpa:
    hibernate:
//테이블 추가, 제거를 자동으로 해줌. 상용에서는 최대 update까지 해주는 정도
      ddl-auto: create
//jpa가 작동하면서 실제 적용되는 sql문 보여줄 것인지.
    show-sql: false
    properties:
      hibernate:
//mysql사용할 것을 알려줌.
        dialect: org.hibernate.dialect.MySQL8Dialect
```

- `jdbc`
    - Java Database Connectivity
    - Java SE library for queryging/updating database data
    - Mainly focused on relational DBs
    - Manages Connections to the DB

JPA Hibernate는 객체에 넣은 JPA 어노테이션에 따라서 그 형태로 테이블을 작성 한다.

### BoardEntity, PostEntity 작성

- BoardEntity
    
    ```java
    @Entity
    public class BoardEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
    
        @OneToMany(
                targetEntity = PostEntity.class,
                fetch = FetchType.LAZY,
                mappedBy = "boardEntity" //postEntity에 정의된 ManyToOne관계와 매핑하기 위해서 추가
                                         //postEntity에서 사용되는 boardEntity를 가리킨다.
        )
    		private List<PostEntity> postEntityList = new ArrayList<>();
    ```
    
    - `@Entity`
        - 데이터베이스 테이블과 일대일 매칭되는 객체 단위.
        - `Entity` 인스턴스 하나가 테이블에서의 하나의 레코드 값
    - `@Id`
        - 테이블상의 `PK(Primary Key)`와 같은 의미.
        - `PK`특성상, 구분이 가능하고, 빠르고 안전하게 생성이 되어야 한다는 점에서 자바에서는 `Long`형을 주로 사용한다.
    - `@GeneratedValue(strategy = GenerationType.IDENTITY)`
        - table을 생성하면서 AI등과 같이, id생성 규칙을 설정
        - `IDENTITY` : mysql의 경우, PK의 auto increment방식을 사용할 수 있게 지정.
        - `SEQUENCE` : 만약, Oracle을 사용하고 있다면, sequence방식으로 PK값을 증가시킴
        - `AUTO(default)` : JPA 구현체에서 자동으로 생성 전략 결정
        - `TABLE` : 데이터베이스에 키 생성 전용 테이블을 하나 만들고 이를 사용하여 기본키를 생성.
    - `@Table(name = "board")`
        - 데이터베이스 상의 실제 테이블 이름 지정.
        - 지정하지 않는다면 `Entity` 클래스 이름 그대로 지정된다.
    - `@Column`
        - 객체 멤버변수에 적용 가능.
        - 데이터베이스 컬럼과 객체 멤버변수가 1:1로 매칭되며, 별다른 옵션이 없다면 생략이 가능하다.
        - length 지정 가능
            - 문자열의 경우 기본 255자가 지정된다.
            - 큰 숫자의 경우 precision, scale로 최대 길이 지정가능.
        - name 지정 가능 - 지정하지 않을 경우, 멤버변수의 이름과 동일하게 컬럼명이 정해진다.
    - `@OneToMany`
        - 현재, `board` 하나에 여러 개의 게시글 `post` 엔티티가 들어가는 상황으로 일대다로 연관관계를 설정할 수 있다.
            - targetEntity : 연관관계의 타겟이 되는 entity 클래스
            - fetch : 연관된 entity의 정보를 어느 시점에 불러올 것인지 결정
                - EAGER : 관계된 엔티티 정보를 미리 읽어옴
                - LAZY : 실제 요청하는 순간 가져옴.
            - mappedBy : 양방향 관계 설정시 관계 주체가 되는 쪽에서 정의
    
    [OneToMany (hibernate-jpa-2.1-api 1.0.0.Final API)](https://docs.jboss.org/hibernate/jpa/2.1/api/javax/persistence/OneToMany.html)
    
    [JPA 의 Fetch Type 과 친해지기](http://jaynewho.com/post/39)
    
- PostEntity
    
    ```java
    @Entity
    public class PostEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; //@Id라는 어노테이션을 통해서 PK임을 알려줌
        private String title;
        private String content;
        private String writer;
    
        //Board의 PK인 id를 가리키는 foreign key라고 말할 수 있다.
        @ManyToOne(
                targetEntity = BoardEntity.class,
                fetch = FetchType.LAZY //연관관계 엔티티를 어느 시점에 불러올 것인가.
        )
    		private BoardEntity boardEntity;
    ...
    ```
    

### postRepository, boardRepository 작성

```java
public interface BoardRepository extends CrudRepository<BoardEntity, Long> {}
```

- `CrudRepository`
    - 엔티티 클래스에 대한 정교한 CRUD 기능 제공
    - 어떤 엔티티를 위한 것인지, id(PK)가 어떤 타입으로 작성되어 있는지 지정.

[CrudRepository (Spring Data Core 2.6.2 API)](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)

### BaseEntity 작성

모든 Entity들이 가져야 하는 기본 속성을 정의. 추상클래스로 정의한 다음, 이후 다른 클래스들이 상속 받을 수 있게 한다. 

- 생성일자
- 수정일자
- 식별자

```java
//모든 entity가 공통으로 가져야 할 속성
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract  class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private Instant createAt; //언제 생성되었는가

    @LastModifiedDate
    @Column(updatable = true)
    private Instant updatedAt; //언제 마지막으로 수정되었는가

    public Instant getCreateAt() {
        return createAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

---------------------------
user.setCreatedAt(LocalDateTime.now());
```

- `@MappedSuperclass` : 해당 추상클래스를 상속할 경우, createDate, modifiedDate등을 컬럼으로 인식
- `@EntityListeners`
    
    생성, 수정일자의 경우, 모든 엔티티의 공통적인 특성으로 모든 엔티티에 대해 수작업으로 지정해주기 보다는 엔티티의 변화를 감지, 테이블의 데이터를 조작과 같은 이벤트를 감지하고 처리하는 Listener 클래스를 만들어 달아주는 것이 더 효율적이다.
    
    - AuditingEntityListener.class
        
        `@CreateDate` , `@LastModifiedDate` 어노테이션을 통해서 각각 createAt, updateAt이 테이블의 컬럼으로 인식될 때, hibernate에서는 자동으로 엔티티 생성, 수정일자를 채워주는 기능을 제공하는데 이를 `Auditing(감시)` 라고 한다.
        

스프링 부트 Entry포인트인 실행 클래스에서 `@EnableJpaAuditing` 를 추가함으로써 JPA Auditing 기능을 활성화 해야 한다. 

[[Spring JPA] Entity Listener](https://velog.io/@seongwon97/Spring-Boot-Entity-Listener)

# CRUD에 데이터베이스 적용

## PostController API

- POST /post
- Get /post
- Get /post/0
- Put /post/0
- Delete /post/0

## PostDto의 정의

```java
public class PostDto {
    private int id;
    private String title;
    private String content;
    private String writer;
    private int boardId;
}
```

- 엔티티와는 대비되는 데이터를 저장하기 위한 순수 자바 객체
- 요청자와 컨트롤러와 데이터를 주고받고, `Dao`까지 사용하게 되는 객체

엔티티는 데이터 표현만을 나타내는 것이기도 하고, 내부에 객체가 실제로 존재하기 때문에 데이터만 전송되지 않고 추가적인 정보가 많이 전달될 수 있다는 단점이 있다. 단순한 crud 연산에서 엔티티를 있는 그대로 데이터 전송 과정에서 사용하는 것은 좋은 선택이 아니다.

## PostService

`PostService`에서는`postDao`를 통해 넘어온 `postEntity`를 `PostDto`형식으로 바꿔주는 로직이 추가되었다.

```java
public PostDto readPost(int id) {
    PostEntity postEntity = this.postDao.readPost(id);
    return  new PostDto(
            Math.toIntExact(postEntity.getId()),
            postEntity.getTitle(),
            postEntity.getContent(),
            postEntity.getWriter(),
            postEntity.getBoardEntity() == null ?
                    0 : Math.toIntExact(postEntity.getBoardEntity().getId())
    );
}
```

## PostDao(= PostRepository)

- `PostService`를 통해 넘어온 데이터인 `PostDto`를 `PostEntity`로 바꿔주기
    
    ```java
    public void createPost(PostDto dto) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(dto.getTitle());
        postEntity.setContent(dto.getContent());
        postEntity.setWriter(dto.getWriter());
        postEntity.setBoardEntity(null);
        this.postRepository.save(postEntity);
    }
    ```
    
- `postRepository`로 넘어온 값을 `Optional Wrapper`객체로 받기
    
    ```java
    public void updatePost(int id, PostDto dto) {
        Optional<PostEntity> targetEntity = this.postRepository.findById(Long.valueOf(id));
        if (targetEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        PostEntity postEntity = targetEntity.get();
        postEntity.setTitle(dto.getTitle() == null ? postEntity.getTitle() : dto.getTitle());
        postEntity.setContent(dto.getContent() == null ? postEntity.getContent() : dto.getContent());
        this.postRepository.save(postEntity);
    }
    ```
    
    - Class Optional<T>
        - NullPointException의 문제를 해결하기 위한 해결책(Java 8에서부터 지원). null일 수 있는객체를 감싸는 일종의 Wrapper 클래스
        - 값이 있는지 없는지, 메소드를 통해서 확인할 수 있게.
            - isEmpty()
            - isPresent()
        - 일종의 Wrapper객체로 실제 객체가 Optional객체로 감싸진 형태가 된다.
            - get()
                - If a value is present in this `Optional`, returns the value, otherwise throws `NoSuchElementException.`
    
    [Optional (Java Platform SE 8 )](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html?is-external=true)
  
  ---
  
  (출처 - ProjectLion The Origin : Springboot 강사 박지호)
  
 
  
