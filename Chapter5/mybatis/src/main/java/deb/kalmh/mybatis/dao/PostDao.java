package deb.kalmh.mybatis.dao;

import deb.kalmh.mybatis.dto.PostDto;
import deb.kalmh.mybatis.mapper.PostMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDao {
    private final SqlSessionFactory sessionFactory;

    public PostDao(
            @Autowired SqlSessionFactory sessionFactory
    ) {
        this.sessionFactory = sessionFactory;
    }

    //매번, 세션을 여는 이유
    // - thread safe하지 않다.
    // - 요청이 연속적으로 빠르게 들어올 때, 서로의 요청에 의한 함수 결과가
    //   서로에게 영향을 끼칠 수 있다.
    public int createPost(PostDto dto) {
        // try - resource 형태
        // try 안에서만 변수가 살아있도록. 따라서 세션이 중괄호 안에서만 산다.
        // try 안에 넣어줌으로써 try{} 안에서만 세션이 살도록 조정해준다.
        try (SqlSession session = sessionFactory.openSession();) {
            //세션에 존재하는 postMapper랑 동일한 구현체를 받아서 저장
            PostMapper mapper = session.getMapper(PostMapper.class);
            return mapper.createPost(dto); //DB와 통신 완료
        }
    }

    public PostDto readPost(int id) {
        try (SqlSession session = sessionFactory.openSession();) {
            PostMapper mapper = session.getMapper(PostMapper.class);
            return mapper.readPost(id);
        }
    }
    public List<PostDto> readPostAll() {
        try (SqlSession session = sessionFactory.openSession();) {
            PostMapper mapper = session.getMapper(PostMapper.class);
            return mapper.readPostAll();
        }
    }
    public int updatePost(PostDto dto) {
        try (SqlSession session = sessionFactory.openSession();) {
            PostMapper mapper = session.getMapper(PostMapper.class);
            return mapper.updatePost(dto);
        }
    }
    public int deletePost(int id) {
        try (SqlSession session = sessionFactory.openSession();) {
            PostMapper mapper = session.getMapper(PostMapper.class);
            return mapper.deletePost(id);
        }
    }
}