package deb.kalmh.jpa;

import deb.kalmh.jpa.entity.BoardEntity;
import deb.kalmh.jpa.entity.PostEntity;
import deb.kalmh.jpa.repository.BoardRepository;
import deb.kalmh.jpa.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class TestComponent {
    public TestComponent(
            //crud repository를 확장했기 때문에 이미 메소드가 구현되어 있는 것을 알 수 있음.
            @Autowired BoardRepository boardRepository,
            @Autowired PostRepository postRepository
            ) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setName("new Board");
        BoardEntity newBoardEntity = boardRepository.save(boardEntity);
        //save()의 결과물로 돌려주는 엔티티를 사용하자.
        System.out.println(newBoardEntity.getName());

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle("hello ORM");
        postEntity.setContent("hello WWW");
        postEntity.setWriter("kalmh");
        postEntity.setBoardEntity(newBoardEntity);
        PostEntity newPostEntity = postRepository.save(postEntity);

        //sql에 익숙하다면 인자만 전해줄 때, 자바 객체로 반환해주는 mybatis도 꽤 매력적이다
        //혹은 이미 필요한 메소드가 구현되어 있는 jpa도 사용하기 간편할 수 있다.
        // - 어차피 내부에서는 entity 형태로 작동됨.
    }
}
