package deb.kalmh.mybatis.mapper;

import deb.kalmh.mybatis.dto.PostDto;

import java.util.List;

public interface PostMapper {
    //insert, delete update의 경우, int를 반환
    //결과로써 몇 개의 row가 영향을 받았는지 알려주기 위해서
    int createPost(PostDto dto);
    int createPostAll(List<PostDto> dtoList);
    PostDto readPost(int id);
    List<PostDto> readPostAll();
    PostDto readPostQuery(PostDto dto);
    int updatePost(PostDto dto);
    int deletePost(int id);
}
