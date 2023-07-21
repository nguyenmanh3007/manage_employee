package com.controller;

import com.converter.ConfirmConverter;
import com.entity.ERole;
import com.entity.Employee;
import com.entity.Roles;
import com.jwt.JwtTokenProvider;
import com.payload.request.LoginRequest;
import com.payload.request.SignupRequest;
import com.payload.response.JwtResponse;
import com.payload.response.MessageResponse;
import com.security.CustomUserDetails;
import com.service.ConfirmService;
import com.service.EmployeeService;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ConfirmService confirmService;
    @Autowired
    private ConfirmConverter confirmConverter;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerEmployee(@RequestBody SignupRequest signupRequest) {
        if (employeeService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already"));
        }
        if (employeeService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already"));
        }
        Employee employee = new Employee();
        employee.setCode(employeeService.createCode());
        employee.setUserName(signupRequest.getUserName());
        employee.setPassword(encoder.encode(signupRequest.getPassword()));
        employee.setEmail(signupRequest.getEmail());
        employee.setPhone(signupRequest.getPhone());
        employee.setTimeCheckin(signupRequest.getTimeCheckin());
        employee.setTimeCheckout(signupRequest.getTimeCheckout());
        employee.setEmployeeStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        employee.setCreated(strNow);
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            // User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Employee is not found"));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                        break;
                    case "employee":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                        break;
                }
            });
        }
        employee.setListRoles(listRoles);
        employeeService.saveOrUpdate(employee);
        return ResponseEntity.ok(new MessageResponse("Employee registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.print(customUserDetails);
        //sinh JWT tra ve Client
        String jwt = tokenProvider.generateToken(customUserDetails);
        //Lay cac quyen cua user
        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt, customUserDetails.getCode(), customUserDetails.getUsername(), customUserDetails.getEmail(), customUserDetails.getPhone(), customUserDetails.getTimeCheckin(), customUserDetails.getTimeCheckout(), listRoles));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Xóa các thông tin đăng nhập khỏi SecurityContextHolder
        SecurityContextHolder.clearContext();

        // Xóa session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Xóa cookie
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
