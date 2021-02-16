package com.ingrupo.cpfvalidator.resource;

import com.ingrupo.cpfvalidator.dto.LoginDto;
import com.ingrupo.cpfvalidator.models.User;
import com.ingrupo.cpfvalidator.service.MyUserDetailsService;
import com.ingrupo.cpfvalidator.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserResource {

    private final MyUserDetailsService myUserDetailsService;

    public UserResource(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) throws Exception {

        System.out.println(user.getUsername());
        return myUserDetailsService.signUpUser(user);
    }

    @PostMapping("/login")
    public ApiResponse login(HttpServletRequest req, @RequestBody LoginDto loginDto){
        return myUserDetailsService.login(req, loginDto);
    }
}

