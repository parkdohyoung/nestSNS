package com.nest.controller;

import com.nest.domain.Notification;
import com.nest.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //안 읽은 알람
    @GetMapping("/unread")
    public ResponseEntity<Page<Notification>> unread(HttpServletRequest request, @RequestParam int page, int size){
        Long accountId = (Long) request.getAttribute("accountId");
        Page<Notification> unreadNotification = notificationService.getUnreadNotification(accountId, size, page);

        return ResponseEntity.ok(unreadNotification);
    }

    //읽음 처리
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<?> checkRead(@PathVariable Long notificationId){
        notificationService.checkRead(notificationId);
        return ResponseEntity.ok(Map.of("message", "알림이 읽음 처리 되었습니다."));
    }
}
