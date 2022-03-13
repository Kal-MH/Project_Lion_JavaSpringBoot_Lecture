package dev.kalmh.crud.post;

import java.util.List;

//Controller와 Model(data) 그리고 비즈니스 로직을 분리하는 것이 중요하다.
public interface PostService {
    void createPost(PostDto dto);
    List<PostDto> readPostAll();
    PostDto readPost(int id);
    void updatePost(int id, PostDto dto);
    void deletePost(int id);
}
