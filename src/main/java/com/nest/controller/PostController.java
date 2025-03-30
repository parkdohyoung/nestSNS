package com.nest.controller;


import com.nest.common.util.ErrorMessages;
import com.nest.domain.Post;
import com.nest.dto.PostDto;
import com.nest.service.AccountService;
import com.nest.service.CommentService;
import com.nest.service.KeywordService;
import com.nest.service.PostService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final KeywordService keywordService;


    @Autowired
    public PostController(PostService postService, KeywordService keywordService) {
        this.keywordService = keywordService;
        this.postService = postService;

    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> followingPost(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){

        Long accountId = (Long) request.getAttribute("accountId");
        return ResponseEntity.ok(postService.getFollowingPost(accountId, page,size));

    }

    @GetMapping("/{targetAccountId}")
    public ResponseEntity<Page<PostDto>> AccountPost(
            @PathVariable Long targetAccountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Long accountId = (Long) request.getAttribute("accountId");
        Page<PostDto> accountPost = postService.getAccountPost(targetAccountId, page, size);
        return ResponseEntity.ok(accountPost);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<PostDto>> searchPostByKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request){
        Page<PostDto> postByKeyword = keywordService.findPostByKeyword(keyword, page, size);
        return ResponseEntity.ok(postByKeyword);
    }

    @PostMapping("/new")
    public ResponseEntity<PostDto> newPost(@RequestBody PostDto postDto,
                                           HttpServletRequest request) throws IOException {
        Long accountId = (Long) request.getAttribute("accountId");
        return ResponseEntity.ok(postService.createPost(accountId, postDto.getMessage()));
    }


    @GetMapping("/edit/{postId}")
    public ResponseEntity<?> editPost(@PathVariable Long postId , HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        PostDto postDto = postService.findPostById(postId);
        log.info("token accountID: {}/ post.account.id:{}",accountId, postDto.getAccountId());
       if(!accountId.equals(postDto.getAccountId())) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ErrorMessages.UNAUTHORIZED_ACCESS));
       }
           return ResponseEntity.ok(postDto);

    }

    @PostMapping("/edit/{postId}")
    public ResponseEntity<?> editPost(
            @PathVariable Long postId,
            @RequestBody PostDto postDto,
            @RequestParam(required = false) MultipartFile image,
            HttpServletRequest request) {

        Long accountId = (Long) request.getAttribute("accountId");

        PostDto originPostDto = postService.findPostById(postId);

        if(!accountId.equals(originPostDto.getAccountId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ErrorMessages.UNAUTHORIZED_ACCESS));
        }else{
            postService.updatePost(postId, accountId, postDto.getMessage());
            return ResponseEntity.ok(Map.of("message","포스트가 정상 저장 되었습니다."));
        }
    }

    @PostMapping("/edit/image")
    public ResponseEntity<?> editPostImage(@RequestParam(required = false) Long postId,
                                           @RequestParam("image") MultipartFile image ,
                                           HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");

        Long ValidAccountId = (postId == null) ? accountId : postService.findPostById(postId).getAccountId() ;
        if(!accountId.equals(ValidAccountId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ErrorMessages.UNAUTHORIZED_ACCESS));
        }
        PostDto savedPostDto = postService.editImage(postId, accountId, image);
        return ResponseEntity.ok(savedPostDto);
    }



    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam Long postId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        PostDto postDto = postService.findPostById(postId);
        if(!accountId.equals(postDto.getAccountId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ErrorMessages.UNAUTHORIZED_ACCESS));
        }
        postService.deletePost(postId);
            return ResponseEntity.ok(Map.of("message","포스트가 정상 삭제 되었습니다."));
    }
    @DeleteMapping("/delete/image")
    public ResponseEntity<?> deletePostImage(@RequestParam Long postId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        PostDto postDto = postService.findPostById(postId);
        if(!accountId.equals(postDto.getAccountId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ErrorMessages.UNAUTHORIZED_ACCESS));
        }
        postService.deleteImage(postId);
            return ResponseEntity.ok(Map.of("message","이미지가 정상 삭제 되었습니다."));
    }


}


