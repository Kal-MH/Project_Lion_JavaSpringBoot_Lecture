package deb.kalmh.jpa.repository;

import deb.kalmh.jpa.entity.BoardEntity;
import org.springframework.data.repository.CrudRepository;

//CRUD 작업을 하기위한 레포지토리로 CrudRepository를 사용하여 확장
// - 어떤 엔티티를 위한 것인가, id가 어떤 타입으로 작성되느냐
public interface BoardRepository extends CrudRepository<BoardEntity, Long> {
}
