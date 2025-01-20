package store.aurora.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Generate JWT", description = "Generates a new JWT token (Access/Refresh) using username(required) and expiration time(optional).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "user Id가 null이거나 user Id 암호화 실패",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/api/auth")
    public String createJwt(@RequestBody JwtRequestDto jwtRequestDto) {
        return jwtUtil.createJwt(jwtRequestDto.getUsername(), jwtRequestDto.getExpiredMs());
    }

    //make new jwt (access token)
    @Operation(summary = "Reissue access token)", description = "Creates a new access token using the user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "access token reissued successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "user Id 암호화 실패",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/api/auth/refresh")
    public String reissue(@RequestHeader(value = "X-USER-ID") String userId) {
        return jwtUtil.createJwt(userId, null);
    }
}