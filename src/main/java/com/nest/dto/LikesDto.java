package com.nest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nest.domain.Account;
import com.nest.domain.TargetType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikesDto {
    private Long id;
    private Long accountId;
    private Long targetId;
    private TargetType targetType;
    private boolean isIncrease;
}
