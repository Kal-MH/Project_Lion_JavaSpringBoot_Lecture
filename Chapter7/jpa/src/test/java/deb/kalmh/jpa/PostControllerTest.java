package deb.kalmh.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//어떤 어플리케이션을 기준으로 테스트하는 지
@RunWith(SpringRunner.class)
//Controller의 함수들을 테스트하기 위해 붙여주는 어노테이션
//단위테스트할 때 붙여주는 어노테이션
@WebMvcTest(PostController.class)
public class PostControllerTest {
    @Autowired
    //http client인 척 한다.
    private MockMvc mockMvc;

    @MockBean
    //만들어지지 않은 bean이 만들어진 것으로 가정하고 사용하게 함.
    //실제 구현체와는 별개로 빈 객체로서 IoC 컨테이너에 등록이 되어 사용되게 한다.
    private PostService postService;

    @Test
    public void readPost() throws Exception {
        // given : 어떤 데이터가 준비가 되어 있다. (조건)
        // - PostEntity가 존재할 때 (PostService가 PostEntity를 잘 돌려줄 때)
        final int id = 10;
        PostDto testDto = new PostDto();
        testDto.setId(id);
        testDto.setTitle("Unit Title");
        testDto.setContent("Unit Content");
        testDto.setWriter("unit");

        //특정 행동을 하길 바를 함수를 지정
        //readPost()함수의 행동을 흉내낼 수 있도록 도와준다.
        given(postService.readPost(id)).willReturn(testDto);
        // when : 어떤 행위(함수 호출)이 일어났을 때
        // - 경로에 GET 요청이 오면

        final ResultActions actions = mockMvc.perform(get("/post/{id}", id))
                .andDo(print());
        // then : 어떤 결과가 올것인지(조건이 주어졌고(given), 어떤 행위가 일어났을 때(when) 어떤 일이 일어날 것이다.
        // - PostDto가 반환된다.

        //perform() 결과로 돌려준 인터페이스를 통해 우리가 원하는 결과가 맞는 지 확인한다.
        actions.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.title", is("Unit Title")),
                jsonPath("$.content", is("Unit Content")),
                jsonPath("$.writer", is("unit"))
        );
    }

    @Test
    public void readPostAll() throws Exception{
        PostDto post1 = new PostDto();
        post1.setTitle("title 1");
        post1.setContent("test");
        post1.setWriter("test");

        PostDto post2 = new PostDto();
        post1.setTitle("title 2");
        post1.setContent("test2");
        post1.setWriter("test2");

        List<PostDto> readAllPost = Arrays.asList(post1, post2);
        given(postService.readPostAll()).willReturn(readAllPost);

        //when
        final ResultActions actions = mockMvc.perform(get("/post"))
                .andDo(print());
        // then
        actions.andExpectAll(
          status().isOk(),
          content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
          jsonPath("$", hasSize(readAllPost.size()))
        );
    }
}