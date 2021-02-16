package com.ingrupo.cpfvalidator.service;

import com.ingrupo.cpfvalidator.dto.LoginDto;
import com.ingrupo.cpfvalidator.models.MyUserDetails;
import com.ingrupo.cpfvalidator.models.User;
import com.ingrupo.cpfvalidator.repository.UserRepository;
import com.ingrupo.cpfvalidator.util.ApiResponse;
import com.ingrupo.cpfvalidator.util.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomAuthenticationProvider authManager;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        Optional<User> user = userRepository.findByUsername(userName);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

        return user.map(MyUserDetails::new).get();
    }

    public String signUpUser(User user) throws Exception {

        Optional<User> findUser = userRepository.findByUsername(user.getUsername());

        if(!findUser.isEmpty()){
            return("Usuário já existe");
        }else {
            try {
                final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(encryptedPassword);
                final User createdUser = userRepository.save(user);

                return "Usuário criado com sucesso";
            }catch (Exception err){
                throw new Exception(err);
            }
        }
    }

    public ApiResponse login(HttpServletRequest req, LoginDto loginDto) {

        User user = userRepository.findByUsername(loginDto.getUsername()).get();

        if(user == null) {
            throw new RuntimeException("User does not exist.");
        }
        if(!bCryptPasswordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            throw new RuntimeException("Password mismatch.");
        }

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication auth = authManager.authenticate(authReq);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        return new ApiResponse(200, "Login success", null) ;
    }
}
