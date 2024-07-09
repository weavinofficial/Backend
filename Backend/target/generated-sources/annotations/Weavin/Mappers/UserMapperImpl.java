package Weavin.Mappers;

import Weavin.Entities.User;
import Weavin.Models.SignUpUserHolder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-09T03:35:19+0900",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240620-1855, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User signUpUserHolderToUser(SignUpUserHolder signUpUserHolder) {
        if ( signUpUserHolder == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( signUpUserHolder.getEmail() );
        user.field( signUpUserHolder.getField() );
        user.password( signUpUserHolder.getPassword() );
        user.username( signUpUserHolder.getUsername() );

        return user.build();
    }

    @Override
    public SignUpUserHolder userToSignUpUserHolder(User user) {
        if ( user == null ) {
            return null;
        }

        SignUpUserHolder signUpUserHolder = new SignUpUserHolder();

        signUpUserHolder.setEmail( user.getEmail() );
        signUpUserHolder.setField( user.getField() );
        signUpUserHolder.setPassword( user.getPassword() );
        signUpUserHolder.setUsername( user.getUsername() );

        return signUpUserHolder;
    }
}
