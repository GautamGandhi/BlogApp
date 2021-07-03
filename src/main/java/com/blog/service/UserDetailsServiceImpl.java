package com.blog.service;

import com.blog.config.CustomUserDetails;
import com.blog.repository.UserRepository;
import com.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(email);
        if(user == null){
            throw new UsernameNotFoundException("Could not found user !!");
        }
        return new CustomUserDetails(user);
    }
}
