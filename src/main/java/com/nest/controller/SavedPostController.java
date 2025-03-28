package com.nest.controller;

import com.nest.dto.PostDto;
import com.nest.service.SavedPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saved-post")
public class SavedPostController {

    private final SavedPostService savedPostService ;

    @Autowired
    public SavedPostController(SavedPostService savedPostService) {
        this.savedPostService = savedPostService;
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> mySavedPost(HttpServletRequest request,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){
        Long accountId = (Long) request.getAttribute("accountId");

        return ResponseEntity.ok(savedPostService.getSavedPost(accountId, page, size));
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<String> addSavedPost(@PathVariable Long postId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        savedPostService.addSavedPost(accountId, postId);
        return ResponseEntity.ok("저장하였습니다.");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deleteSavedPost(@PathVariable Long postId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        savedPostService.removeSavedPost(accountId,postId);
        return ResponseEntity.ok("저장된 포스트 삭제하였습니다.");
    }
}
