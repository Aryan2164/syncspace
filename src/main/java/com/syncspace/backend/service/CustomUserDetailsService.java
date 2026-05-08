package com.syncspace.backend.service;

import com.syncspace.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.syncspace.backend.entity.User;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

  public UserDetails loadUserByUsername(String email){
    User user =   userRepository.findByEmail(email);
    if(user==null){
        throw new UsernameNotFoundException("User not Found!");
    }
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
            Collections.emptyList()
            );
  }
}
