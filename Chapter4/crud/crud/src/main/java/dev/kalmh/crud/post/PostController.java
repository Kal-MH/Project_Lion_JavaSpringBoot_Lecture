package dev.kalmh.crud.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody //클래스 안에 있는 모든 함수들에 @responseBody가 붙어있는 것과 같은 동작
//@RequestMapping("post") //prefix 경로
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(
            @Autowired PostService postService
    ) {
        this.postService = postService;
    }

    @PostMapping("create")
    public void createPost(@RequestBody PostDto postDto) {
        this.postService.createPost(postDto);
    }

    @GetMapping("read-all")
    public List<PostDto> readPostAll() {
        logger.info("in read all");

        return this.postService.readPostAll();
    }

    @GetMapping("read-one")
    public PostDto readPostOne(@RequestParam("id") int id) {
        logger.info("in read one");
        return this.postService.readPost(id);
    }

    @PostMapping("update")
    public void updatePost(
            @RequestParam("id") int id,
            @RequestBody PostDto postDto
    ) {
        logger.info("target update");
        this.postService.updatePost(id, postDto);
    }

    @DeleteMapping("delete")
    public void deletePost(@RequestParam("id") int id) {
        this.postService.deletePost(id);
    }
}


//package dev.kalmh.crud.post;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Deprecated
//@Controller
//@ResponseBody //클래스 안에 있는 모든 함수들에 @responseBody가 붙어있는 것과 같은 동작
////@RequestMapping("post") //prefix 경로
//public class PostController {
//    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
//
//    private final List<PostDto> postList;
//
//    public PostController() {
//        postList = new ArrayList<>();
//    }
//
//    @PostMapping("create")
//    public void createPost(@RequestBody PostDto postDto) {
//        this.postList.add(postDto);
//    }
//
//    @GetMapping("read-all")
//    public List<PostDto> readPostAll() {
//        logger.info("in read all");
//
//        return this.postList;
//    }
//
//    @GetMapping("read-one")
//    public PostDto readPostOne(@RequestParam("id") int id) {
//        logger.info("in read one");
//        return this.postList.get(id);
//    }
//
//    @PostMapping("update")
//    public void updatePost(
//            @RequestParam("id") int id,
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
//    @DeleteMapping("delete")
//    public void deletePost(@RequestParam("id") int id) {
//        this.postList.remove(id);
//    }
//}
