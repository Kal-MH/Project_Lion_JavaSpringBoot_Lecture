# CRUD란?

## 서비스의 자원

웹 서비스를 개발한다는 것은 사용자가 원하는 자원을 관리하는 것이다. 자원을 제공하고 제작, 갱신을 진행한다.

식당, 메뉴, 요리사라는 자원이 존재할 때, ‘주문’이라는 자원을 추가함으로써, 식당에서는 주문을 확인, 메뉴라는 자원을 제작하기 위해 요리사가 일하는 등의 일들이 이뤄진다.

따라서, 서비스가 사용할 자원의 관리가 중요하다. 

또한 부수적인 기능은 서비스 별로 다르게 만들어진다. 자원관리는 우선적으로 두고, 다른 필요한 기능들은 그 시점에 차근차근히 추가해나가는 것이 좋다.

## Create, Read, Update, Delete

- Create : 생성
- Read : 조회
- Update: 갱신
- Delete : 삭제

위 4가지의 동작이 가능해야 서비스의 자원을 관리할 수 있다고 말할 수 있다.

## CRUD 실습

# CRUD와 REST

## RESTful이란?

REpresentational State Transfer.

서버의 형태에 따라서 클라이언트의 형태가 고정되어 있을 경우 결합성이 높다고 하는데, 이는 하나의 변화가 다른 쪽에 큰 영향을 미친다고 볼 수 있으므로 좋은 관계라고 볼 수 없다.

결합성을 줄이므로써, 확장성, 성능 개선, 사용 간편함 등의 장점이 있기 때문이다. 또한, 실행 중간에도 기능이 변할 수 있기 때문에 결합성을 줄이는 것이 좋다.

REST란 기본적으로 Clinet와 Server간의 결합성을 줄이기 위한 가이드라고 말할 수 있다. 따라서 클라이언트가 사용할 기술의 제한을 없애는 것이 REST를 갖추기 위한 기본 목표라고 할 수 있다.

따라서 REST를 지키기 위한 제약상항들이 존재한다.

1. Client Server Architecture : 서로의 역할이 잘 분담되어야 한다.
    1. 클라이언트와 서버가 잘 분리되어 있어야 한다.
    2. 서로의 변화가 서로에게 영향을 주지 않는 형태가 되어야 한다.
    3. 엔드포인트는 그대로 있고 서버 내부 코드만 변경. 즉, RequestMapping이 바뀌진 않는다.
2. Statelessness : API자체가 상태를 저장하고 있지 않아야 한다.
    1. 서버 안에 사용자 정보를 저장하지 않는다.
    2. 모든 요청은 독립적이다.
    3. 요청하는 당사자가 자신을 증명할 책임이 있다. 즉, 원하는 기능을 위한 상태는 클라이언트가 가지고 있어야 한다.
3. Cacheability : 재사용가능해야 한다.
    1. 수정이 많지 않음 이미지 같은 데이터를 연속적으로 받지 않고, 저장해둔다.
    2. 즉, 이런 데이터를 서버가 알려줘야 한다.
    3. 자원의 캐싱 가능성 여부를 서버가 항상 표기해줘야 한다.
4. Layered System : 실제 서버까지 도달하는 과정을 클라이언트가 알 필요가 없다
    1. 계층 구조
    2. 물리적 서버의 증축에도 상관없이 클라이언트는 항상 같은 방법으로 요청한다.
5. Uniformed Interface : 일관된 인터페이스
6. Code on Demand(Optional) : 앱이 실행 중일 때도 기능이 변할 수 있다.
    1. 일시적 기능의 확장
    2. 사용 가능한 코드를 응답으로 보내 사용자의 기능을 일시적으로 확장시킬 수 있다.

## API를 RESTful하게 설계하는 방법

서비스가 커지면 100% REST의 조건을 만족하기는 어렵다.

1. 경로(path)를 통해 도달하고자 하는 자원을 지정
2. 방법(method)을 통해 자원에 실행할 기능을 지정

## RequestMapping 재구성

# Spring Stereotypes

## Component란?

Spring boot에서는 ComponentScan을 이용해 사용할 Bean의 범위를 정해줄 수 있다. 

- 함수 단위 : @Bean
- 클래스 단위 : @Component

Component에서 탄생한 부수적인 어노테이션.

- Controller : RequestMapping과 함께 사용해서 MVC의 컨트롤러 역할임을 알림
- Repository : Data Access Object(DAO)와 같이 실제 데이터 근원과 소통하는 부분. 데이터 회수, 주고 받는 부분에 붙여줌
- Service : 비즈니스 로직이 구현된 부분임을 알림.

모든 Bean에 Component를 사용해도 작용하기에는 무리가 없다.

## Service, Repository 사용하기

# Database 다뤄보기

## 관계형 데이터베이스와 ERD

Codd의 12규칙을 따르고자 하는 Database를 관계형 데이터베이스라고 하지만 전부 다 따르지는 않는다.

테이블의 형태로 데이터를 저장하는 것이 가장 기본적인 형태이다. 또한 관계형 연산자로, 테이블의 형태로 데이터를 반환한다.

1. Primary Key
    1. 테이블 내에서 분명하게 하나의 entity(row)를 가져올 수 있는 명백한 값
    2. 주요키의 여부에 따라서 성능 차이가 존재한다.

Entity-Relationship Diagram

엔티티와 그 사이의 관계를 나타내기 위한 모델. 데이터 표현 방식 중에 하나.

1. user 하나에 order가 여러 개 가능하지만, order하나에 user가 여러 개일 수 없다. → 일대 다 관계
2. order 하나에 product 여러 개 가능, product 하나에 order 여러 개 가능
    1. 하나의 주문에 여러 개의 상품 포함
    2. 상품은 여러 주문에 포함됨
    3. → 다대다 관계

## MySql과 Workbench 설치하기

### 도커 이용 방법

```java
docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d -p 3306:3306 mysql:8
```

### 워크벤치

관계형 데이터베이스 서버(RDMS)에 대해서 서버에 접속하기 위한 클라이언트 프로그램.

```java
name : servername-계정

```

## MySQL에 스키마 / 유저 생성

1. Schema에 들어가서 create Schema
    1. demo_schema라고 명명하자.
2. User Privilege
    1. add account
        1. name : demo_user
        2. password : demo_user_password1!
        3. 만들어진 유저는 권한이 없기 때문에 schema_priviliege를 추가해줘야 한다.
            1. add Entity
                1. demo_schema 선택
                2. selecte_all
3. 해당 유저에 대한 mysql 연결 만들어주기.
    1. default schema : demo_schema
    2. selecte 1 from dual; → test용 sql문.

## 기본적인 SQL 작성법

```sql
truncate post;
```
