package store.aurora.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.aurora.dto.JwtRequestDto;
import store.aurora.util.AESUtil;
import store.aurora.util.JWTUtil;

@RestController
public class AuthController {
    private final JWTUtil jwtUtil;


    public AuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/auth")
    public String createJwt(@RequestBody JwtRequestDto jwtRequestDto) {
        return jwtUtil.createJwt(jwtRequestDto.getUsername());
    }
}
