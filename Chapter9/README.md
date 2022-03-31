# RabbitMQ

## Message Broker

HTTP는 단방향 단일성 통신, 즉 서버와 클라이언트의 역할이 명백한 관계에서 주고받는 통신이라고 할 수 있다.

다음의 상황을 생각해보자.

1. 사용자 요청을 1초에 5번 처리할 수 있는 서버에 대해서 사용자 요청이 1초에 10번 들어올 때,
    
    → Scale Out. 여러 개의 서버를 두어 분산으로 처리할 수 있게 한다.
    
    → 메세지 큐를 가운데 두고 메세지를 적재한 다음, 처리한다.
    
    ```java
    Message Broker
        
    메세지 기반의 통신을 위해 활용하는 부수적 소프트웨어(미들웨어)의 일종.
    서로 다른 어플리케이션 간에 메세지를 주고 받기 위한 중간 매개 역할을 하는 소프트웨어.
    
    비동기 통신을 하는데 다양한 방법으로 활용된다.
    - worker에게 메세지를 분산해 전달
    - subscriber에게 동시에 메세지를 전달.
    
    또한, 이런 형식의 메세지 전달 체계는 기능을 여러 서비스에 걸쳐서 제공하는
    MSA(Micro Service Architecture)에 있어서 필수적인 요소이다.
    ```
    
2. 하나의 사용자가 여러 개의 서비스를 사용할 때, 사용자 정보 변동에 대해서 다른 서비스에도 알려주어 업데이트를 해야 한다.
    - Publish Subscribe Message Pattern

## 메세지 기반 비동기 통신

### Job Queue

Message Queue를 활용해, 처리가 필요한 작업을 한곳에 쌓아두고, 처리를 할 수 있는 프로세스가 특정 규칙을 가지고 적재된 작업을 받아가는 방식으로 활용하는 방법

- Producer
    
    작업에 해당하는 메세지를 만드는 서버/프로세스
    
- Consumer
    
    작업을 처리하기 위해 대기 중인 서버/프로세스
    

### Publish  - Subscriber

동일한 정보를 구독하고 있는 여러 프로세스에게 동시에 전달하는 메세지 패턴.

- Publisher
    
    메세지를 발생시키는 프로세스
    
- Subscriber
    
    발생하는 메세지를 듣기 위해 구독한다는 의미로 메세지를 전달 받는 프로세스를 Subscriber라고 한다.
    

## RabbitMQ 설치

```java
docker run -it --rm --name rabbit-demo -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

- management 화면 로그인
    - username : guest
    - password : guest
    
    <aside>
    💡 단, 해당 guest계정으로는 localhost, 즉 자신의 컴퓨터에서만 접속이 가능하다.
    
    </aside>
    
- RabbitMQ는 메세지 큐를 구성하는 미들웨어
    - Queue
        
        메세지를 받는 쪽은 큐를 선언하게 된다. Producer가 발생시킨 메세지들을 Message Broker는 순차적으로 적재하기 위해 Queue의 형태로 자료구조를 갖고 있다.
        
        - test-queue & add queue
    - ExChanges와 Queue는 우체국과 우편함의 관계.
        - Exchange는 RabbitMQ에서 실제로 메세지를 적재하는 주체이다. 메세지는 Queue에 바로 적재되는 것이 아니라 Exchanges를 통해서 메세지를 저장하게 된다.

# Message Broker활용

1. spring initialize
    1. Spring web
    2. Spring for RabbitMQ
        
        <aside>
        💡 implementation 'org.springframework.boot:spring-boot-starter-amqp'
        
        </aside>
        
        `amqp`(Advanced Message Queueing Protocol)란, RabbitMQ를 비롯한 Message Broker들이 메세지를 주고 받기 위한 통신 규약.
        
    3. Spring Data JPA
    4. H2 Database
    
    또한, 메세지 큐에 연결되어 있는 두 서버에 대해서 메세지를 생성하는 서버를 `producer`, 받는 쪽을 `consumer`라고 한다.
    

## Worker Queue

Worker Queue를 구성하기 위해, Producer와 Consumer역할을 하는 각각의 프로세스가 있다고 상황을 가정하고, 하나의 Exchange에 하나의 Queue를 작성한다.

### Producer

1.  config 패키지에 ProducerConfig 생성 및 설정
    
    ```java
    @Configuration
    public class ProducerConfig {
        @Bean
        public Queue queue(){
            return new Queue("boot.amqp.worker-queue", true, false, true);
        }
    }
    ```
    
    메세지를 받기 위한 Exchange는 이미 정의가 되어 있으므로 큐에 대한 부분만 Bean 객체로 정의해준다.
    
    - durable : 서버가 종료되었다가 다시 켜져도 큐가 그대로 남아 있을 것인지
    - exclusive : 중복 허용 여부
    - autoDelete : 큐가 더 이상 필요하지 않을 때, 자동으로 삭제 여부. 모든 consumer가 unsubscribe하면 자동으로 없어진다.
2. service 패키지 ProducerService 생성 및 작성
    
    ```java
    @Service
    public class ProducerService {
        private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    
        // RabbitMQ 요청을 주고 받기 위한 스프링 부트의 인터페이스 -> restTemplate과 비슷하다.
        private final RabbitTemplate rabbitTemplate;
        // config패키지에 선언한 큐를 가져옴.
        // - 상황에 따라서 여러 개의 큐(exchange)에 메세지를 따로 보관하는 경우도 있다.
        private final Queue rabbitQueue;
    
        public ProducerService(
                @Autowired
                RabbitTemplate rabbitTemplate,
                @Autowired
                Queue rabbitQueue
        ) {
            this.rabbitTemplate = rabbitTemplate;
            this.rabbitQueue = rabbitQueue;
        }
    
        //메세지를 만드는 데 있어서 서로 다른 스레드에서 접근할 수 있기 때문에
        // 몇 개의 메세지가 지나갔는 지 파악해서 스레드간의 꼬임을 막기 위해서 사용.
        AtomicInteger dots = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);
    
        public void send() {
            StringBuilder builder = new StringBuilder("Hello");
            if (dots.incrementAndGet() == 4) {
                dots.set(1);
            }
            builder.append(".".repeat(Math.max(0, dots.get())));
            builder.append(count.incrementAndGet()); //count++
            String message = builder.toString();
            rabbitTemplate.convertAndSend(rabbitQueue.getName(), message);
            logger.info(" [x] Sent '" + message + "'");
        }
    
    }
    ```
    
3. controller 패키지 ProducerController 생성 및 작성
    
    ```java
    @GetMapping("/")
    public void sendMessage(){
        producerService.send();
    }
    ```
    

### Consumer

1. config에 ConsumerConfig 추가
    
    사실, Consumer서버에서는 따로 Queue를 구성할 필요 없이 `@RabbitListener`와 `@RabbitHandler` 어노테이션을 사용해서 바로 만들어진 Queue에 연결해서 사용할 수 있다.
    
    하지만, 이 경우에 이미 만들어진 Queue가 존재하지 않는다면, 즉 Producer가 먼저 실행중이지 않는다면 Queue에 연결하지 못해 에러가 발생한다.
    
    이런 상황을 방지하고 싶다면 Consumer에서도 동일한 설정의 Bean객체를 만들면 된다.
    
    ```java
    @Bean
        public Queue queue(){
            return new Queue("boot.amqp.worker-queue", true, false, false);
        }
    ```
    
2. 서비스 패키지에 ConsumerService 생성 및 작성
    
    ```java
    @Service
    //실제 사용할 큐를 지정한다.
    // - 지정한 큐를 찾기 때문에 producer가 먼저 실행중이지 않으면(큐가 먼저 생성되어 있지 않으면) 에러가 난다.
    @RabbitListener(queues = "boot.amqp.worker-queue")
    public class ConsumerService {
        private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    
        @RabbitHandler
        public void receive(String message){
            logger.info(" [x] Received '" + message + "'");
        }
    }
    ```
    

### 실행 확인

콘솔창을 통해서 producer, consumer를 테스트해보자.

```java
java -jar build/libs/producer-0.0.1-SNAPSHOT.jar

java -jar -Dserver.port=8081 build/libs/producer-0.0.1-SNAPSHOT.jar
```

## Pub-Sub Queue

### publisher

앞선 Worker Queue구형과 다른 점은 Queue를 선언하는 것이 아닌 Exchange를 통해서 구현한다는 점이다. 

Publisher에서는 메세지를 전달하는 역할의 Exchange만 선언하게 되고 해당 Exchange를 구독하고 있는 각각의 어플리케이션이 별도의 익명 Queue를 선언해서 갖고 있다.

1. config 패키지에 PublisherConfig 생성 및 작성
    1. FanoutExchange() Bean 객체 선언
        
        ```java
        @Configuration
        public class PublisherConfig {
            @Bean
            public FanoutExchange fanoutExchange(){
                return new FanoutExchange("boot.fanout.exchange");
            }
        }
        ```
        
        - FanoutExchange : 차별 없이 구독하고 있는 모든 Queue에 메세지를 전달,
        - direct Exchange : 각 큐가 Routing Key에 바인딩되어 있고, Exchange에 Routing Key가 들어오면, Exchange와 연결된 큐 중에서 해당 Routing Key에 매핑된 큐로 메세지를 전달.
2. service 패키지에 PublisherService 생성 및 작성
3. controller 패키지에 Publishercontroller 생성 및 작성

### Subscriber

1. config 패키지에 SubscriberConfig 생성 및 작성
    
    ```java
    @Configuration
    public class SubscriberConfig {
        //pulisher와 같은 exchange를 지정해야 한다.
        @Bean
        public FanoutExchange fanoutExchange(){
            return new FanoutExchange("boot.fanout.exchange");
        }
    
        //메세지를 받는 주체이기 때문에 메세지를 저장할 큐가 필요하다.
        @Bean
        public Queue autoGenQueue(){
            return new AnonymousQueue();
        }
    
        //exchage와 queue를 연결하는 함수
        @Bean
        public Binding binding(
                Queue queue,
                FanoutExchange fanoutExchange
        ){
            return BindingBuilder
                    .bind(queue)
                    .to(fanoutExchange);
        }
    }
    ```
    
2. service 패키지에서 SubscriberService 작성

# Redis

미들웨어 중의 하나. (REmote Dictionary Server)

## Key-Value Database

key-value 형식의 데이터를 저장하는 Collection을 dictionary라고 한다. Redis는 key-value형식의 데이터를 저장하고 꺼내 쓰는데 용이한 NoSQL 데이터베이스이다. 

- In-Memory : 휘발성 데이터를 담기 위한 DB.
- NoSql : SQL를 이용한 조회를 하지 않는다.
    - 대신에, key값을 전달해서 데이터를 조회한다.
    - 문자열, 리스트 같은 자료형 지원
- 외부 캐시 또는 Message Broker로 활용한다.
    - 외부 캐시 예
        - 서버가 여러 개로 분산 되어 있는 상태에서(scale out) 사용자 로그인 정보를 서버 내부에 저장하게 되면 다른 서버로 요청을 보냈을 때, 설령 이전에 로그인했다고 하더라도 누구인지 알 수 없게 된다.
        - 로그인 요청 결과를 key-value 형태로 Redis 내부에 저장하게 되면, Redis key값을 통해서 여러 서버에서 사용자 조회를 할 수 있게 된다.
    - meessage broker
        - producer에서 메세지 큐에 메세지를 적재하고, consumer에서 해당 메세지를 메세지 큐를 통해 받는다고 할 때, consumer로부터의 요청 처리 결과를 Redis에 적재할 수 있다.
        - 사용자의 요청을 받는 endpoint역할을 하는 producer에서 비동기적으로 Redis에 저장된 요청 처리 결과를 조회할 수 있다. 이 때, 어떤 요청에 대한 응답인지 확인하기 위한 `requestKey`와 같은 부수적 데이터를 key값으로 활용할 수 있다.

## 데이터 비동기 처리

1. redis 설치
    
    ```java
    docker run --name redis-stub -d -p 6379:6379 redis:6-alpine
    ```
    
2. producer, consumer yml 서버포트 설정
3. 의존성 추가
    1. spring data redis

### producer

1. build.gradle
    
    jedis를 기반으로 redis를 실행하기 위한 환경 설정
    
    ```java
    implementation('org.springframework.boot:spring-boot-starter-data-redis') {
    		exclude group: 'io.lettuce', module: 'lettuce-core'
    	}
    	implementation 'redis.clients:jedis'
    implementation 'com.google.code.gson:gson:2.9.0'
    ```
    
2. model패키지
    1. JobRequest(JobMessage)
        1. WorkerQueue에 적재하는 요청을 나타내는 데이터
        
        ```java
        public class JobMessage implements Serializable {
            private String jobId;
        		...
        }
        ```
        
    2. JobProcess
        1. 요청이 처리된 응답 데이터를 담기 위한 용도
        2. Redis에서 관리하는 객체로 등록하기 위해 `@RedisHash` 어노테이션을 사용한다.
        
        ```java
        @RedisHash("Job")
        public class JobProcess implements Serializable {
            private String id;
            private int status;
            private String message;
            private String result;
        		...
        }
        ```
        
3. config에 RedisConfig 추가
4. redis와 상호작용을 하기 위한 repository를 작성
5. producerService
    1. json형식으로 추가하기 위한 gson 설정 build.gradle에 추가.
        
        `JobMessage` 객체를 JSON String형태로 변환하여 전송하도록 Gson 의존성을 추가해준다.
        
    2. producerConfig 클래스에서 gson을 만들어주는 메소드 추가
    3. ProducerService send()메소드 수정
    
    ```java
    public String send() {
            JobRequest jobRequest = new JobRequest(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(rabbitQueue.getName(), gson.toJson(jobRequest));
            logger.info("Sent Job: {}", jobMessage.getJobId());
            return jobMessage.getJobId();
        }
    ```
    
6. 요청 처리 결과가 들어있는 redis와 소통하기 위해 RedisService 작성
    1. CrudRepository를 활용한다. 또한 지정할 인자로써 Repository에서 사용할 Entity로는 `JobMessage`를  조회하기 위한 키 값으로는 Redis이기 때문에 `String`으로 지정한다.
7. 요청처리 결과를 위한 RedisService와 PostController 함수 추가
    - RedisService의 retrieveJob()
        
        ```java
        public JobProcess retrieveJob(String jobId) {
                Optional<JobProcess> jobProcess = this.redisRepository.findById(jobId);
                if (jobProcess.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
        
                return jobProcess.get();
            }
        ```
        
    - PostController
        
        ```java
        @GetMapping("/{jobId}")
            public JobProcess getResult(@PathVariable("jobId") String jobId){
                return redisService.retrieveJob(jobId);
            }
        ```
        

### consumer

1. ConsumerService
    1. receive 함수 구현
        
        ```java
        @RabbitHandler
        public void receive(String message) throws InterruptedException {
            String jobId = "";
            try {
                JobMessage newJob = gson.fromJson(message, JobMessage.class);
                jobId = newJob.getJobId();
                logger.info("Received Job: {}", jobId);
                JobProcess jobProcess = new JobProcess();
                jobProcess.setId(newJob.getJobId());
                jobProcess.setMessage("Job being processed");
                jobProcess.setStatus(1);
                jobProcess.setResult("");
                redisRepository.save(jobProcess);
            } catch (RuntimeException e){
                throw new AmqpRejectAndDontRequeueException(e);
            }
        //임의로 처리에 걸리는 시간이 5초라고 가정하자.
        		Thread.sleep(5000);
            JobProcess jobProcess = redisRepository.findById(jobId).get();
            jobProcess.setId(jobId);
            jobProcess.setMessage("Finished");
            jobProcess.setStatus(0);
            jobProcess.setResult("Success");
            redisRepository.save(jobProcess);
            logger.info("Finished Job: {}", jobId);
        }
        ```
        
        - `message`를 정상적으로 해석한다면, 해당 `jobId`를 기준으로 새로운 객체를 만들고 1차적으로 Redis에 저장한다.
        - 임의로 정한 처리시간 5초가 지나면 해당 `job`이 처리되었음을 알려주기 위해 업데이트한다.

### 단점

RabbitMQ, Redis 모두 분산 과정에서 도움이 되긴 하지만 상대방에 대해서 잘 알아야 함으로, 클라이언트, 서버간의 결합력이 높다는 단점이 보인다.

# WebSocket

## WebSocket

양방향 통신을 구현하기 위해서 사용되는 방법. AMQP(Message Broker), HTTP와 마찬가지로 Application Layer상에 정의된 통신 규약의 일종이다.

한 번 연결된 이후, 한 쪽이 종료하기 전까지는 계속 양방향 연결 상태(full-duplex)가 유지된다. 또한 하나의 서버에 대해서 여러 클라이언트가 연결될 수 있기 때문에 실시간 채팅에 많이 사용된다.

1. Handshake 요청
    
    일종의 http 요청. `Upgrade`필드의 `websocket`이라고 명시함으로써 서버에 웹소켓을 요청하게 되며 서버에서는 응답으로 몇 가지 헤더가 추가된 상태로 응답을 보내게 된다.
    
    ```
    GET /chat HTTP/1.1
    Host: server.example.com
    Upgrade: websocket
    Connection: Upgrade
    Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==
    Sec-WebSocket-Protocol: chat, superchat
    Sec-WebSocket-Version: 13
    Origin: http://example.com
    
    [Client Handshake 요청]
    ```
    
    ```
    HTTP/1.1 101 Switching Protocols
    Upgrade: websocket
    Connection: Upgrade
    Sec-WebSocket-Accept: HSmrc0sMlYUkAGmm5OPpG2HaGWk=
    Sec-WebSocket-Protocol: chat
    
    [Server Handshake 요청]
    ```
    
2. `Event Driven Programming`
    
    연결된 채널을 통해서 요청과 응답을 주고 받게 될 때, 아래 4가지의 이벤트를 기준으로 행동양식을 정하게 된다. 이를  `Event Driven Programming`이라고 한다.
    
    1. onOpen() : 연결 형성
    2. onMessage() : 메세지 도달
    3. onclose() : 연결 종료
    4. onError() : 오류 발생

## STOMP

일반적인 WebSocket을 이용한 통신은 주고 받는 메세지의 형태에 대해서는 정해진 게 없기 때문에 데이터의 해석이 어렵다. http 요청에서는 request header, line, body등에 대해서 자세히 정의되어 있지만, WebSocket을 이용한 통신은 개발자가 직접 데이터를 해석해야 함을 의미한다.

따라서 요청을 주고 받을 때, http처럼 `Frame`이라고 부르는 주고 받는 메세지의 형태를 정의한, 서브 프로토콜을 사용한다.

→ Simple/Streaming Text Oriented Message Protocol (STOMP)

- 웹소켓 위에서 사용되는 프로토콜
    
    ```
    COMMAND => Request Method(http)
    header1:value1
    header2:value2
    
    Body^@
    ```
    
- destination으로 메세지를 받을 클라이언트를 결정
    
    WebSocket은 다양한 URI를 정의하는 HTTP서버와 달리, 하나의 URI상에서 지속적으로 메세지를 주고받게 된다. STOMP는 destination필드를 통해 서버에서 정의한 특정 경로를 클라이언트에게 전달하게 된다.
    
    ```
    SEND
    destination:/queue/test
    
    Hello from TCP!
    ^@
    ```
    

Http, WebSocket, STOMP에 대한 특징을 정리하면 다음과 같다.

1. HTTP
 - 다양한 경로를 기준으로 기능 분리
2. WebSocket
 - 하나의 경로로 많은 클라이언트 접속
 - 메세지 기반
3. STOMP
 - Destination으로 메세지를 받을 클라이언트 결정

## Chatting

1. 의존성
    1. websocket
        
        ```groovy
        implementation 'org.springframework.boot:spring-boot-starter-websocket'
        ```
        
2. static file 추가
    
    `resource/static` 경로에 해당 html, js파일들을 추가한다.
    
3. WebSocketConfig
    
    ```java
    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    		@Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/ws/chat");
            registry.addEndpoint("/ws/chat").withSockJS();
        }
    
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/receive-endpoint");
            registry.setApplicationDestinationPrefixes("/send-endpoint");
        }}
    ```
    
    - `@EnableWebSocketMessageBroker`
        - Spring Boot에서 STOMP를 활용한 WebSocket 통신할 것을 알림.
    - `WebSocketMessageBrokerConfigurer` 를 구현함으로서, Spring Security에서 진행한것 처럼 미리 정의된 함수들을 통해 구성을 한다
    - `registerStompEndpoints()`
        - 최초로 WebSocket Handshake를 진행할 Endpoint를 설정한다.
        - WebSocket을 지원하지 않는 브라우저를 위해서 `withSockJS()` 함수를 통해 `SockJS` 를 지원한다.
            - `SockJS` 는 WebSocket을 지원하지 않는 브라우저에서, 일반 HTTP 규약을 가지고, WebSocket의 행동규칙을 흉내내는 객체를 제공해주는 라이브러리.
    - `configureMessageBroker()`
        - `destination` 을 정의.
        - `setApplicationDestinationPrefixes()` : 서버가 메시지를 받기 위한 Destination
        - `enableSimpleBroker()` 의 경우 클라이언트가 메시지를 받기 위해 구독할 Destination.
        - 단, Destination 자체에 구독하는 것이 아닌 Prefix(접두사) 형태로 구독할 수 있는 Destination이 적용된다.
4. WebSockerMapping
    
    WebSocket의 Endpoint도 `@Controller` 객체를 사용한다. 다만 `@RequestMapping` 이 아닌 `@MessageMapping` 을 이용한다. 
    
    ```java
    @Controller
    public class WebSocketMapping {
        private static final Logger logger = LoggerFactory.getLogger(WebSocketMapping.class);
        private final SimpMessagingTemplate simpMessagingTemplate;
    
        public WebSocketMapping(SimpMessagingTemplate simpMessagingTemplate) {
            this.simpMessagingTemplate = simpMessagingTemplate;
        }
    
        @MessageMapping("/ws/chat") // WebSocketConfig 에서 구성했던 endpoint 의 값
        public void sendRoom(ChatMessage chatMessage) {
            logger.info(chatMessage.toString());
            final String time = new SimpleDateFormat("HH:mm").format(new Date());
            simpMessagingTemplate.convertAndSend(
                    String.format("/receive-endpoint/%s", chatMessage.getRoomId()),
                    new ChatMessage(chatMessage.getRoomId(), chatMessage.getSender(), chatMessage.getMessage(), time)
            );
        }
    }
    ```
    
    - endpoint와 `WebSocketConfig` 에서 구성한 `enableSimpleBroker()` 함수에서 정의했던 destination을 합쳐, 클라이언트가 메시지를 보내는 경로가 완성된다.
    - Client의 메시지를 받고, 필요한 작업을 처리한 뒤에는 `SimpMessagingTemplate` 객체를 이용하여 다시 Destination에 구독한 Client들에게 메시지를 전송한다.
