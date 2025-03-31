package com.nest.controller;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.TargetType;
import com.nest.dto.LikesDto;
import com.nest.service.LikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesService likesService;

    @Autowired
    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    private Long accountId;
    private Long targetId;
    private TargetType targetType;
    private boolean isIncrease;

    @PostMapping()
    public ResponseEntity<?> like(@RequestParam Long targetId ,
                                  @RequestParam TargetType targetType ,
                                  @RequestParam Boolean isIncrease,
                                  HttpServletRequest request){

        Long accountId = (Long) request.getAttribute("accountId");

        // 유효성 체크
        if (targetId == null || targetType == null) {
            return ResponseEntity.badRequest().body(Map.of("message","targetId와 targetType은 필수 값입니다."));
        }

        LikesDto likesDto = new LikesDto();
        likesDto.setAccountId(accountId);
        likesDto.setTargetId(targetId);
        likesDto.setTargetType(targetType);
        likesDto.setIncrease(isIncrease);

        likesService.adjustLike(likesDto);
        return ResponseEntity.ok(Map.of("message",likesDto.isIncrease() ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다."));
    }

}
