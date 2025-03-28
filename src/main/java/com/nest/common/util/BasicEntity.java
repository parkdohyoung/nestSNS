package com.nest.common.util;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate(){
        createDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        modifiedDate = LocalDateTime.now();
    }
}
