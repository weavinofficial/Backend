package Weavin.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Weavin.Entities.User;
import Weavin.Models.WeavinUserDetails;
import Weavin.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class WeavinUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        this.initializeLazyDatas(user);
        return new WeavinUserDetails(user);
    }

    private void initializeLazyDatas(User user) {
        Hibernate.initialize(user.getCommentList());
        Hibernate.initialize(user.getForumPostList());
        Hibernate.initialize(user.getMarketPostList());
        Hibernate.initialize(user.getSemesterList());
    }
    
    

}