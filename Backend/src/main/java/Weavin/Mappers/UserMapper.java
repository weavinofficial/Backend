package Weavin.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import Weavin.Entities.User;
import Weavin.Models.SignUpUserHolder;

@Mapper
public interface UserMapper {
    
    @Mapping(target = "commentList", ignore = true)
    @Mapping(target = "forumPostList", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastSeenAt", ignore = true)
    @Mapping(target = "marketPostList", ignore = true)
    @Mapping(target = "presence", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "reportStatus", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "semesterList", ignore = true)
    @Mapping(target = "usernameAlreadyChanged", ignore = true)
    User signUpUserHolderToUser(SignUpUserHolder signUpUserHolder);

    SignUpUserHolder userToSignUpUserHolder(User user);
}
