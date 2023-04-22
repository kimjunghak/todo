package com.higherx.api.model.dto.user;

import com.higherx.api.model.entity.user.HigherxUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HigherxUserMapper {

    HigherxUserFront.UserInfo userInfoFromEntity(HigherxUser higherxUser);

    HigherxUser toEntity(HigherxUserFront.Signup signup);
}
