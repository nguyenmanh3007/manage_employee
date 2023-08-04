package com.controller;

import com.dto.EmployeeDTO;
import com.entity.Employee;
import com.entity.RefreshToken;
import com.entity.Roles;
import com.exception.TokenRefreshException;
import com.jwt.JwtTokenProvider;
import com.payload.request.LoginRequest;
import com.payload.request.SignupRequest;
import com.payload.request.TokenRefreshRequest;
import com.payload.response.JwtResponse;
import com.payload.response.MessageResponse;
import com.payload.response.TokenRefreshResponse;
import com.security.CustomUserDetails;
import com.service.EmployeeService;
import com.service.RefreshTokenService;
import com.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerEmployee(@RequestBody SignupRequest signupRequest) {
        if (employeeService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already"));
        }
        if (employeeService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already"));
        }
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = roleService.getRole(strRoles);
        EmployeeDTO employeeDTO=EmployeeDTO.builder()
                .code(employeeService.createCode())
                .userName(signupRequest.getUserName())
                .password(encoder.encode(signupRequest.getPassword()))
                .email(signupRequest.getEmail())
                .phone(signupRequest.getPhone())
                .timeCheckin(signupRequest.getTimeCheckin())
                .timeCheckout(signupRequest.getTimeCheckout())
                .employeeStatus(true)
                .created(new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .listRoles(listRoles)
                .build();
        employeeService.saveOrUpdate(employeeDTO);
        return ResponseEntity.ok(new MessageResponse("Employee registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateTokenFromUsername(customUserDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetails.getUserId());
        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,refreshToken.getToken(), customUserDetails.getCode(), customUserDetails.getUsername(), customUserDetails.getEmail(), customUserDetails.getPhone(), customUserDetails.getTimeCheckin(), customUserDetails.getTimeCheckout(), listRoles));
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getEmployee)
                .map(employee -> {
                    String token = tokenProvider.generateTokenFromUsername(employee.getUserName());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return ResponseEntity.ok(new MessageResponse("Logout successfully"));
    }
}
