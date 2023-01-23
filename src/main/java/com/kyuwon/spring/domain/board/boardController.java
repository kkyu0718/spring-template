package com.kyuwon.spring.domain.board;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api/board")
public class boardController {
    @GetMapping("/user")
    public String testUser() {
        return "hello user";
    }

    @GetMapping("/admin")
    public String testAdmin() {
        return "hello admin";
    }
}
