package dev.kalmh.crud.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Service : 비즈니스 로직.
// - 실제 실행하기 전, 혹은 여러가지 실행결과를 조합
//Repository
// - 데이터를 실제 주고 받는 파트 구현할 때 사용

@RestController
@RequestMapping("post")
public class PostRestController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    public PostRestController(
            @Autowired PostService postService
    ) {
        this.postService = postService;
    }

    //HttpServletRequest 를 통해 헤더 내용 또한 확인할 수 있다.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // status code를 제공한다.
    public void createPost(@RequestBody PostDto postDto) {
        logger.info(postDto.toString());
        this.postService.createPost(postDto);
    }

    // http://localhost:8080/post
    // GET /post
    //read에 사용되기 권장되는 메소드
    @GetMapping()
    public List<PostDto> readPostAll() {
        logger.info("in read post all");
        return this.postService.readPostAll();
    }

    //GET /post/0/ -> pathVariable
    // - 쿼리는 질문을 하기 위해 사용
    // - 어떤 명백한 자원을 지정할 때는 경로 변수를 사용한다.
    @GetMapping("{id}")
    public PostDto readPost(@PathVariable("id") int id) {
        logger.info("read one");
        return this.postService.readPost(id);
    }

    //POST 는 없었던 자원을 새로 생성
    //PUT 은 이미 있는 자원에 대해서 일부분을 대체할 때 사용
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@ResponseStatus(HttpStatus.ACCEPTED)
    public void updatePost(
            @PathVariable("id") int id,
            @RequestBody PostDto postDto
    ) {
        logger.info("update");
        this.postService.updatePost(id, postDto);
    }

    // DELETE /post/0
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePost(@PathVariable("id") int id) {
        this.postService.deletePost(id);
    }
}


//package dev.kalmh.crud.post;
//
////springboot에서 템플릿을 사용할 경우,
////Constoller에서 사용자가 볼 화면을 제공하고,
////RestContoller에서 화면에서 사용하기 위한 API를 작성하는 형태로 분리할 수도 있다
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("post")
//public class PostRestController {
//    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
//    private final List<PostDto> postList;
//
//    public PostRestController() {
//        this.postList = new ArrayList<>();
//    }
//
//    // http://localhost:8080/post -> 게시글에 대한 정보를 보고싶다.
//    //create에 사용하는 메소드는 Post (권장)
//    // POST 메소드로 /post요청이 들어오면
//    // Request_body 에 따라서 게시글을 만들어내야 한다고 서버에서 이해
//    //  - 경로에 동작에 관한 것은 넣어주지 않는다.
//    //  - 새로운 정보를 전달할 때는 RequestBody를 사용하자.
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED) // status code를 제공한다.
//    public void createPost(@RequestBody PostDto postDto) {
//        logger.info(postDto.toString());
//        this.postList.add(postDto);
//    }
//
//    // http://localhost:8080/post
//    // GET /post
//    //read에 사용되기 권장되는 메소드
//    @GetMapping()
//    public List<PostDto> readPostAll() {
//        logger.info("in read post all");
//        return this.postList;
//    }
//
//    //GET /post/0/ -> pathVariable
//    // - 쿼리는 질문을 하기 위해 사용
//    // - 어떤 명백한 자원을 지정할 때는 경로 변수를 사용한다.
//    @GetMapping("{id}")
//    public PostDto readPost(@PathVariable("id") int id) {
//        logger.info("read one");
//        return this.postList.get(id);
//    }
//
//    //POST 는 없었던 자원을 새로 생성
//    //PUT 은 이미 있는 자원에 대해서 일부분을 대체할 때 사용
//    @PutMapping("{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    //@ResponseStatus(HttpStatus.ACCEPTED)
//    public void updatePost(
//            @PathVariable("id") int id,
//            @RequestBody PostDto postDto
//    ) {
//        PostDto targetPost = this.postList.get(id);
//
//        if (postDto.getTitle() != null) {
//            targetPost.setTitle(postDto.getTitle());
//        }
//        if (postDto.getContent() != null) {
//            targetPost.setContent(postDto.getContent());
//        }
//        this.postList.set(id, targetPost);
//    }
//
//    // DELETE /post/0
//    @DeleteMapping("{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void deletePost(@PathVariable("id") int id) {
//        this.postList.remove(id);
//    }
//
//    // POST /post
//    // GET /post
//    // GET /post/0
//    // PUT /post/0
//    // DELETE /post/0
//
//    // POST /board/
//    // GET /board/
//    // GET /board/0
//    // PUT /board/0
//    // DELETE /board/0
//
//    // 즉, 경로를 통해서 자원을 전달하고, 메소드를 통해서 해당 자원을 어떻게
//    // 하고 싶은 것인지를 나타낸다.
//    // 또한, 이는 상태를 전달하는 과정이지 실질적인 것을 들고 움직이는 것과
//    // 차이가 있다.
//}
