package deb.kalmh.jpa;

/*
RabbitMQ, KafkaMQ를 사용하는 인터페이스는 동일하지만, 구현되는 코드 내용은 다를 것이다.
만약, RabbitMQ를 구현단계에서, KafKaMQ를 상용단계에서 사용한다면
@Component, @Profile 어노테이션을 사용해서 구분할 수 있다.
 */
public interface MessageQueueInterface {
    String readMessage();
}
