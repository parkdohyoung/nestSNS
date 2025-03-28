package com.nest.dto.mapper;

import com.nest.domain.Account;
import com.nest.dto.ProfileDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel= "spring")
public interface AccountMapper {
    ProfileDto toProfileDto(Account account);
    List<ProfileDto> toProfileDtoList(List<Account> accounts);
}
