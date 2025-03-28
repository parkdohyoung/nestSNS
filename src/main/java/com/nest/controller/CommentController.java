package com.nest.controller;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Comment;
import com.nest.dto.CommentDto;
import com.nest.dto.EditCommentDto;
import com.nest.dto.PostDto;
import com.nest.dto.mapper.CommentMapper;
import com.nest.repository.AccountRepository;
import com.nest.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nest.common.util.ErrorMessages.UNAUTHORIZED_ACCESS;

@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @GetMapping("/post")
    public ResponseEntity<List<CommentDto>> getParentCommentsByPost(@RequestParam Long postId){
        List<CommentDto> commentByPost = commentService.getParentCommentsByPost(postId);

        return ResponseEntity.ok(commentByPost);
    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentDto>> getCommentsByParentId(@RequestParam Long commentId ){
        List<CommentDto> commentList = commentService.getCommentsByParentId(commentId);

        return ResponseEntity.ok(commentList);
    }

    @PostMapping("/new")
    public ResponseEntity<?> newComment(@RequestBody EditCommentDto commentDto,
                                             HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        if(!accountId.equals(commentDto.getAccountId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",UNAUTHORIZED_ACCESS));
        }

        return ResponseEntity.ok(commentService.addComment(commentDto));
    }

    @GetMapping("/edit/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable Long commentId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        CommentDto commentDto = commentService.getCommentById(commentId);

        if(!accountId.equals(commentDto.getAccountId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",UNAUTHORIZED_ACCESS));
        }
            return ResponseEntity.ok(commentDto);

    }

    @PostMapping("/edit/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable Long commentId,
                                         @RequestBody EditCommentDto editCommentDto,
                                         HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        CommentDto commentDto = commentService.getCommentById(commentId);

        if(!accountId.equals(commentDto.getAccountId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",UNAUTHORIZED_ACCESS));
        }
            return ResponseEntity.ok(commentService.editCommentMessage(editCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        CommentDto commentDto = commentService.getCommentById(commentId);
        if(!accountId.equals(commentDto.getAccountId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",UNAUTHORIZED_ACCESS));
        }
            commentService.deleteCommentById(commentId);
            return ResponseEntity.ok(Map.of("error","댓글이 정상 삭제 되었습니다."));
    }

}
