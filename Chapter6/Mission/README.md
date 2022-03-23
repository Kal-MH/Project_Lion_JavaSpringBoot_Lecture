# 공통조건

spring properties를 활용해서 개발환경과 상용환경의 데이터베이스를 구분하여 실행할 수 있도록 하자.

## Basic Mission

커뮤니티 사이트에 데이터베이스 추가

이전 Basic Mission에서 만들었던 서비스에서 사용한 DTO를 기반으로 Entity를 만들어 보자.

1. `PostEntity` 와 `BoardEntity` 를 만들어 봅시다.
    2. `PosetEntity` 와 `BoardEntity` 의 관계.
        1. `@ManyToOne` , `@OneToMany`, `@JoinColumn`
3. `PostEntity` 의 작성자를 저장하기 위한 `UserEntity` 를 만들고, 마찬가지로 관계를 표현해 보자.

### 세부 조건

1. `UserEntity` 에 대한 CRUD를 작성.
2. `Post` 를 작성하는 단계에서, `User` 의 정보를 어떻게 전달할지 고민해 보자.

## Challenge Mission

목적을 가진 커뮤니티 사이트 만들기

1. 위치정보를 담기 위한 `AreaEntity`.
    1. ‘도, 광역시’, ‘시,군,구’, ‘동,면,읍’ 데이터를 따로 저장한다.
    2. ‘위도’, ‘경도’ 데이터를 저장한다.
2. `UserEntity`.
    1. 자신의 거주지 정보인 `AreaEntity`를 포함.
    2. `UserEntity` 는 사용자 하나를 나타내며, 일반 사용자 또는 상점 주인인지에 대한 분류가 되어야 한다.
    3. 특정 `UserEntity` 만 가질 수 있는 `ShopEntity` 를 작성. 
        1. 해당 `ShopEntity` 가 취급하는 품목에 대한 `CateogoryEntity` 또한 존재. 
        2. `ShopEntity` 는 어디 지역의 상점인지에 대한 정보를 가지고 있어야 한다.
    4. `ShopEntity` 에 대한 게시글인, `ShopPostEntity` 와 `ShopReviewEntity`.
        1. `ShopReviewEntity` 는 어떤 사용자든 작성할 수 있으나, `ShopPostEntity` 는 해당 `ShopEntity` 에 대한 주인 `UserEntity` 만 작성할 수 있어야 한다.

### 세부 조건

1. 생성된 테이블의 실제 이름에는 `Entity` 라는 문구가 들어가지 않도록 `@Table` 어노테이션을 활용.
2. 변동될 가능성이 있는 데이터와 변동될 가능성이 없는 데이터를 잘 구분하여, `Entity` 작성 여부를 잘 판단하자.
3. `Entity` 를 먼저 구성하되, 시간이 남으면 CRUD까지 구성.
