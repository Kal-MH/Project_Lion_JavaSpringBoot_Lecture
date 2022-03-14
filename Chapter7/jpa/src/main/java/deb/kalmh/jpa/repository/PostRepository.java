package deb.kalmh.jpa.repository;

import deb.kalmh.jpa.entity.BoardEntity;
import deb.kalmh.jpa.entity.PostEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<PostEntity, Long> {
//    List<PostEntity> findAllByWriter(String writer); //where writer = ?
//    List<PostEntity> findAllByWriterAndBoard(String writer, BoardEntity boardEntity); // where writer = ? and board = ?
//    List<PostEntity> findAllByWriterContaining(String writer); //
}
