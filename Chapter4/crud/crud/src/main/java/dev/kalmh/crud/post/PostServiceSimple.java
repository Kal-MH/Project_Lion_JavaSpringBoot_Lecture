package dev.kalmh.crud.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceSimple implements PostService{
    private static final Logger logger = LoggerFactory.getLogger(PostServiceSimple.class);

    //인터페이스 자료형을 선언함으로 구현체의 자료형에 대해서 조금 자유롭다.
    private final PostRepository postRespository;

    public PostServiceSimple (
            // @autowrired 어노테이션은 인터페이스를 만족하는 클래스를 IoC에서 제공해서 자동으로 주입해줌(DI)
            // - PostRepository 인터페이스 구현체 중에 우선순위를 고려해서 가져와준다.
            @Autowired PostRepository postRespository
    ) {
        this.postRespository = postRespository;
    }

    @Override
    public void createPost(PostDto dto) {
        // TODO check validity
        if (!this.postRespository.save(dto)) {
            throw new RuntimeException("save failed");
        }
    }

    @Override
    public List<PostDto> readPostAll() {
        return this.postRespository.findAll();
    }

    @Override
    public PostDto readPost(int id) {
        return this.postRespository.findById(id);
    }

    @Override
    public void updatePost(int id, PostDto dto) {
        this.postRespository.update(id, dto);
    }

    @Override
    public void deletePost(int id) {
        this.postRespository.delete(id);
    }
}
