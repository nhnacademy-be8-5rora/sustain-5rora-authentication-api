package store.aurora.controller;

import org.springframework.web.bind.annotation.*;
import store.aurora.dto.JwtRequestDto;
import store.aurora.util.JWTUtil;

@RestController
public class AuthController {
    private final JWTUtil jwtUtil;


    public AuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // jwt 발급 (access, refresh)
    @PostMapping("/api/auth")
    public String createJwt(@RequestBody JwtRequestDto jwtRequestDto) {
        return jwtUtil.createJwt(jwtRequestDto.getUsername(), jwtRequestDto.getExpiredMs());
    }

    //make new jwt (access token)
    @PostMapping("/api/auth/refresh")
    public String reissue(@RequestHeader(value = "X-USER-ID") String userId) {
        return jwtUtil.createJwt(userId, null);
    }
}