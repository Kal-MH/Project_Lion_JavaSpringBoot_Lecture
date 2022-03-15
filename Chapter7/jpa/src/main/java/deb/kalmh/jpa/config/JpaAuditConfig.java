package deb.kalmh.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//테스트하는 과정에서 몇 개의 클래스만 선택적으로 돌리기 때문에
//JPA어플리케이션 엔트리에 붙으면 동작하지 않는 상황이 발생할 수 있다.
@EnableJpaAuditing
public class JpaAuditConfig {

}
