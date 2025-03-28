package com.nest.service;

import com.nest.domain.Comment;
import com.nest.domain.Keyword;
import com.nest.domain.Post;
import com.nest.dto.PostDto;
import com.nest.dto.mapper.PostMapper;
import com.nest.repository.KeywordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository  ;
    private final PostMapper postMapper;

    public KeywordService(KeywordRepository keywordRepository, PostMapper postMapper) {
        this.keywordRepository = keywordRepository;
        this.postMapper = postMapper;
    }

    //키워드 추출
    public List<String> extractKeyword(String message){
        List<String> keywords = new ArrayList<>();
        String[] words = message.split( " ");
        for (String word : words) {
            if(word.startsWith("#")  && word.length()> 1 ){
                keywords.add(word.substring(1));
            }
        }

        return keywords;
    }

    //키워드 저장
    @Transactional
    public void saveKeywords(List<String> keywords, Post post, Comment comment ){
        for (String key : keywords) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(key);
            keyword.setPost(post);
            keyword.setComment(comment);
            keywordRepository.save(keyword);

        }
    }

    @Transactional
    public void updateKeywords(List<String> keyword , Post post , Comment comment){
        if(post != null ){
            keywordRepository.deleteByPost(post);
        } else if( comment != null){
            keywordRepository.deleteByComment(comment);
        }
        saveKeywords(keyword, post, comment);
    }
    @Transactional(readOnly = true)
    public Page<PostDto> findPostByKeyword(String keyword, int page, int size){
        Page<Post> postByKeyword = keywordRepository.findPostByKeyword(keyword, PageRequest.of(page, size));
        return (postByKeyword != null ) ? postMapper.toPostDtoPage(postByKeyword) : Page.empty(PageRequest.of(page,size));

    }
}
