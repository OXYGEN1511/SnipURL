package com.myproject.snipurl.services;

import com.myproject.snipurl.models.SnipUser;
import com.myproject.snipurl.repositories.SnipUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    SnipUserRepository snipUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SnipUser user = snipUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username "+username));
        return UserDetailImpl.build(user);
    }
}
