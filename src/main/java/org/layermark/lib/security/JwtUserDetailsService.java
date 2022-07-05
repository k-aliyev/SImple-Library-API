package org.layermark.lib.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.layermark.lib.dto.UserDto;
import org.layermark.lib.repository.UserRepository;
import org.layermark.lib.service.LibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<org.layermark.lib.model.User> user = userRepository.findByEmail(username);

        if (user.isPresent()) {
            return new User(user.get().getEmail(), user.get().getPassword(), user.get().getRole().getAuthorities());
        } else {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
    }

}