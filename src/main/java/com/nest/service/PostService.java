package com.nest.service;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Account;
import com.nest.domain.Post;
import com.nest.dto.PostDto;
import com.nest.dto.mapper.PostMapper;
import com.nest.repository.AccountRepository;
import com.nest.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PostService {

    @Value("${file.default-dir}")
    private String defaultDir;


    private final PostRepository postRepository ;
    private final AccountRepository accountRepository;
    private final PostMapper postMapper;
    private final FileStorageService fileStorageService;
    private final KeywordService keywordService;
    private final NotificationService notificationService;



    public PostService(PostRepository postRepository,
                       AccountRepository accountRepository,
                       PostMapper postMapper,
                       FileStorageService fileStorageService,
                       KeywordService keywordService,
                       NotificationService notificationService) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.postMapper = postMapper;
        this.fileStorageService = fileStorageService;
        this.keywordService = keywordService;
        this.notificationService = notificationService;
    }

    @Transactional
    public PostDto createPost(Long accountId ,String message, @Nullable MultipartFile file){
        Account account = findAccountById(accountId);
        Post post = new Post();
        post.setAccount(account);
        post.setMessage(message);
        String email = account.getEmail();

        if(file!= null && !file.isEmpty()) {
            String savedPath = fileStorageService.savePostImage(email, file);
            post.setImagePath(savedPath);
        }
        Post savedPost = postRepository.save(post);

        List<String> keywords = keywordService.extractKeyword(message);
        keywordService.saveKeywords(keywords,savedPost,null);


        List<String> mentions = notificationService.extractUserMention(message);
        notificationService.createNotification(mentions,savedPost,null);


        return postMapper.toPostDto(savedPost);
    }

    @Transactional
    public PostDto updatePost( Long postId, Long accountId ,String message, @Nullable MultipartFile file){
        Post post =  postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));
        Account account = findAccountById(accountId);
        post.setAccount(account);
        post.setMessage(message);

        String email = account.getEmail();
        String oldImagePath = post.getImagePath();

        if(file!= null && !file.isEmpty()){
            log.info("file ={}", file.getOriginalFilename());
            if(oldImagePath != null){
                fileStorageService.deletePostImage(post.getImagePath());
            }
            String savedPath = fileStorageService.savePostImage(email, file);
            post.setImagePath(savedPath);
        }else if(oldImagePath != null) {
            fileStorageService.deletePostImage(post.getImagePath());
            post.setImagePath(null);
        }

        List<String> keywords = keywordService.extractKeyword(message);
        keywordService.updateKeywords(keywords,post,null);



        return postMapper.toPostDto(postRepository.save(post));

    }


    @Transactional
    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getFollowingPost(Long accountId, int page, int size){
        Page<Post> posts = postRepository.findPostByFollowerId(accountId, PageRequest.of(page, size));
        return (posts != null ) ? postMapper.toPostDtoPage(posts) : Page.empty(PageRequest.of(page,size));
    }
    @Transactional(readOnly = true)
    public Page<PostDto> getAccountPost(Long accountId, int page, int size){
        Page<Post> posts = postRepository.findByAccountId(accountId, PageRequest.of(page, size));
        return (posts != null ) ? postMapper.toPostDtoPage(posts) : Page.empty(PageRequest.of(page,size));
    }

    @Transactional(readOnly = true)
    public PostDto findPostById(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));
        return postMapper.toPostDto(post);

    }
    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }


}
