package deb.kalmh.jpa;

import deb.kalmh.jpa.entity.PostEntity;
import deb.kalmh.jpa.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//전체 어플리케이션을 테스트한다는 의미
@SpringBootTest(
        //test진행시, 실제 환경이 어디일지를 가정(어디에 생성할 지)
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        //
        classes = JpaApplication.class
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
//h2 database를 사용함을 의미
@AutoConfigureTestDatabase
public class PostControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    //test 실행전에 호출되는 함수
    @Before
    public void setEntitties() {
        createTestPost("fist post", "fist post content", "test-writer");
        createTestPost("second post", "second post content", "test-writer");
    }

    //test 함수 명명 규칙에는 정해진 게 없으나
    //몇 단계인지 구분을 해서 짓는 것이 일반적
    @Test
    void givenValidId_whenReadPost_then200() throws Exception {
        //given
        Long id = createTestPost("Read Post", "Created on readPost()", "read-tester");

        //when
        final ResultActions actions = mockMvc.perform(get("/post/{id}", id))
                .andDo(print());

        //then
        actions.andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.title", is("Read Post")),
                jsonPath("$.content", is("Created on readPost()"))
        );
    }

    private Long createTestPost(String title, String content, String writer) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setWriter(writer);
        return postRepository.save(postEntity).getId();
    }

}
