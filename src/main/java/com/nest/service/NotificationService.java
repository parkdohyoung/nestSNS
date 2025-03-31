package com.nest.service;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.*;
import com.nest.dto.NotificationDto;
import com.nest.dto.mapper.NotificationMapper;
import com.nest.repository.AccountRepository;
import com.nest.repository.LikesRepository;
import com.nest.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository ;
    private final NotificationMapper notificationMapper;

    public NotificationService(AccountRepository accountRepository,
                               NotificationRepository notificationRepository,
                               NotificationMapper notificationMapper) {
        this.accountRepository =  accountRepository;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public List<String> extractUserMention(String message){
        List<String> mentions = new ArrayList<>();
        String[] words =message.split(" ");
        for (String word : words) {
            if(word.startsWith("@") && word.length() > 1 ){
                mentions.add(word.substring(1));
            }
        }
        return mentions;
    }

    @Transactional
    public void createNotification(List<String> mentions, Post post , Comment comment){
        for (String accountName : mentions) {

            Account receiver = accountRepository.findByName(accountName)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ACCOUNT_NOT_FOUND));

            Account sender = (post != null) ? post.getAccount() : comment.getAccount();
            Long targetId = (post != null) ? post.getId() : comment.getId();
            TargetType targetType = (post != null) ? TargetType.POST : TargetType.COMMENT;


            Notification notification = new Notification();
            String content = (post!= null)?
                    sender.getName()+ "님이 게시물에서 "+accountName+"님을 언급하였습니다. : "+ post.getMessage():
                    sender.getName() +"님이 댓글에서 "+accountName+"님을 언급하였습니다. : " + comment.getCommentMessage();

            notification.setContent(content);
            notification.setSender(sender);
            notification.setReceiver(receiver);
            notification.setTargetId(targetId);
            notification.setTargetType(targetType);

            notificationRepository.save(notification);


        }
    }

    public Page<NotificationDto> getUnreadNotification(Long accountId, int size, int page) {
        Page<Notification> notificationPage = notificationRepository.findByAccountIdAndCheckedIsFalse(accountId, PageRequest.of(page, size));
        return notificationMapper.toNotificationDtoPage(notificationPage);
    }

    public void checkRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.KEYWORD_NOT_FOUND));

        notification.setChecked(true);
        notificationRepository.save(notification);

    }
}
