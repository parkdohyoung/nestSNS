package com.nest.service;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Account;
import com.nest.domain.Comment;
import com.nest.domain.Post;
import com.nest.dto.CommentDto;
import com.nest.dto.EditCommentDto;
import com.nest.dto.NewCommentDto;
import com.nest.dto.mapper.CommentMapper;
import com.nest.repository.AccountRepository;
import com.nest.repository.CommentRepository;
import com.nest.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository ;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final KeywordService keywordService ;
    private final NotificationService notificationService;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          AccountRepository accountRepository,
                          PostRepository postRepository,
                          CommentMapper commentMapper,
                          KeywordService keywordService,
                          NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.commentMapper =commentMapper;
        this.keywordService = keywordService;
        this.notificationService = notificationService;
    }

    @Transactional
    public CommentDto addComment(Long accountId,NewCommentDto newCommentDto){

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Comment comment = new Comment() ;
        comment.setAccount(account);
        comment.setCommentMessage(newCommentDto.getCommentMessage());

        Optional.ofNullable(newCommentDto.getPostId()).ifPresent(postId -> {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));
            comment.setPost(post);
        });

        Optional.ofNullable(newCommentDto.getParentCommentId()).ifPresent(parentCommentId -> {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.COMMENT_NOT_FOUND));
            comment.setParentComment(parentComment);
        });

        List<String> keywordList = keywordService.extractKeyword(comment.getCommentMessage());
        keywordService.saveKeywords(keywordList, null , comment);

        List<String> mentions = notificationService.extractUserMention(comment.getCommentMessage());
        notificationService.createNotification(mentions,null,comment);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto editCommentMessage(Long commentId, EditCommentDto editCommentDto){
        //댓글 검증
        if (editCommentDto.getCommentMessage() == null || editCommentDto.getCommentMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어 있을 수 없습니다.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException(ErrorMessages.COMMENT_NOT_FOUND));

        comment.setCommentMessage(editCommentDto.getCommentMessage());

        List<String> keywordList = keywordService.extractKeyword(comment.getCommentMessage());
        keywordService.updateKeywords(keywordList,null,comment);

        List<String> mentions = notificationService.extractUserMention(comment.getCommentMessage());
        notificationService.createNotification(mentions,null,comment);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }


    @Transactional
    public void deleteCommentById(Long commentId)  {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException(ErrorMessages.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> findCommentByPost(Long postId){
        return commentMapper.toCommentDtoList(commentRepository.findByPostId(postId));
    }

    public CommentDto getCommentById(Long id){

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.COMMENT_NOT_FOUND));

        return commentMapper.toCommentDto(comment);
    }

    public List<CommentDto> getParentCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        List<Comment> parentComment = commentRepository.findByPostIdAndParentCommentIsNull(postId);

        for (Comment comment : parentComment) {
            comment.getReplies().size();
        }
        return commentMapper.toCommentDtoList(parentComment);
    }

    public List<CommentDto> getCommentsByParentId(Long parentId){
        List<Comment> byParentCommentId = commentRepository.findByParentCommentId(parentId);
        return commentMapper.toCommentDtoList(byParentCommentId);
    }


}
