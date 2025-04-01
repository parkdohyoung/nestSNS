package com.nest.controller;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Notification;
import com.nest.dto.NotificationDto;
import com.nest.repository.NotificationRepository;
import com.nest.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    //안 읽은 알람
    @GetMapping("/unread")
    public ResponseEntity<Page<NotificationDto>> unread(HttpServletRequest request,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size){
        Long accountId = (Long) request.getAttribute("accountId");
        Page<NotificationDto> unreadNotification = notificationService.getUnreadNotification(accountId, size, page);

        return ResponseEntity.ok(unreadNotification);
    }

    //읽음 처리
    @PostMapping("/read")
    public ResponseEntity<?> checkRead(@RequestParam Long notificationId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException(ErrorMessages.KEYWORD_NOT_FOUND));
        Long targetId = notification.getTargetId();

        if(!accountId.equals(targetId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ErrorMessages.UNAUTHORIZED_ACCESS));
        }

        notificationService.checkRead(notificationId);
        return ResponseEntity.ok(Map.of("message", "알림이 읽음 처리 되었습니다."));
    }


}
