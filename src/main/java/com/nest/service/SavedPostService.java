package com.nest.service;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Account;
import com.nest.domain.Post;
import com.nest.domain.SavedPost;
import com.nest.dto.PostDto;
import com.nest.dto.mapper.PostMapper;
import com.nest.repository.AccountRepository;
import com.nest.repository.PostRepository;
import com.nest.repository.SavedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SavedPostService {

    private final SavedPostRepository savedPostRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public SavedPostService(SavedPostRepository savedPostRepository, PostMapper postMapper, AccountRepository accountRepository , PostRepository postRepository) {
        this.savedPostRepository = savedPostRepository;
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.accountRepository =accountRepository ;
    }

    @Transactional
    public void addSavedPost(Long accountId, Long postId){

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));

        Boolean exists = savedPostRepository.existsByAccountAndPost(accountId, postId);
        if(exists){
            throw new IllegalArgumentException("이미 추가가 된 포스트입니다.");
        }


        SavedPost savedPost = new SavedPost();
         savedPost.setPost(post);
         savedPost.setAccount(account);
         savedPostRepository.save(savedPost);
    }
    @Transactional
    public void removeSavedPost(Long accountId, Long postId){
        SavedPost savedPost = savedPostRepository.findByAccountIdAndPostId(accountId, postId)
                .orElseThrow(()->new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));

        savedPostRepository.delete(savedPost);


    }
    @Transactional(readOnly = true)
    public Page<PostDto> getSavedPost(Long accountId, int page, int size){

        Page<Post> posts = savedPostRepository.findByAccountId(accountId, PageRequest.of(page, size));
        return (posts != null ) ? postMapper.toPostDtoPage(posts) : Page.empty(PageRequest.of(page,size));
    }





}
