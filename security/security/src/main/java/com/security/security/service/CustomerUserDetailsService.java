// CustomerUserDetailsService.java
package com.security.security.service;

import com.security.security.entity.User;
import com.security.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userInfo = userRepository.findByUsername(username);
        // ye galat tha:
        // pehle findByUser(username) use ho raha tha
        // sahi method findByUsername(username) hai

        if (userInfo == null) {
            // ye galat tha:
            // pehle null check nahi tha
            // user na milne par UsernameNotFoundException throw karna chahiye
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                userInfo.getUsername(),
                userInfo.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole()))
                // ye galat tha:
                // pehle Collections.EMPTY_LIST tha
                // authorities empty hone ki wajah se login ke time role nikalte waqt error aa sakta tha
        );
    }
}