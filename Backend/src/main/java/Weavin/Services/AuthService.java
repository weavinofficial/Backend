package Weavin.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Weavin.Entities.User;
import Weavin.Enums.Role;
import Weavin.Mappers.UserMapper;
import Weavin.Models.SignUpUserHolder;
import Weavin.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public void signup(SignUpUserHolder signUpUserHolder) {
        User newUser = userMapper.signUpUserHolderToUser(signUpUserHolder);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.UNVERIFIED);

        /*
         * Other sign up logics
         * 
         * save profile image in S3 then retrieve link, save in user object
         * 
         * send email verification link
         * 
         * etc...
         * 
         */

        userRepository.save(newUser);
    }

    public void deleteUser() {
        // userRepository.delete(user);
    }

    public void recoverPassword() {
        // userRepository.delete(user);
    }

}
