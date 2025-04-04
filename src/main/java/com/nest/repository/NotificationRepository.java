package com.nest.repository;

import com.nest.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.receiver.id = :accountId and n.checked = false ORDER BY n.createDate DESC")
    Page<Notification> findByAccountIdAndCheckedIsFalse(@Param("accountId") Long accountId, Pageable pageable);

}
