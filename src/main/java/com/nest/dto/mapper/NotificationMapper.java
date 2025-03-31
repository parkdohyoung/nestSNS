package com.nest.dto.mapper;

import com.nest.domain.Notification;
import com.nest.domain.Post;
import com.nest.dto.NotificationDto;
import com.nest.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "receiver.id" , target = "receiverId")
    @Mapping(source = "sender.id", target = "senderId")
    NotificationDto toNotificationDto(Notification notification);

    default Page<NotificationDto> toNotificationDtoPage(Page<Notification> notifications){
        return notifications.map(this::toNotificationDto);
    }
}
