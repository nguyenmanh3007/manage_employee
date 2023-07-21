package com.controller;

import com.converter.ConfirmConverter;
import com.dto.EmployeeWithConfirmDTO;
import com.entity.Confirm;
import com.entity.ERole;
import com.entity.Employee;
import com.entity.Roles;
import com.jwt.JwtTokenProvider;
import com.payload.request.CheckioRequest;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/employee")
public class EmployeeController {
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
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.print(customUserDetails);
        //sinh JWT tra ve Client
        String jwt = tokenProvider.generateToken(customUserDetails);
        //Lay cac quyen cua user
        List<String> listRoles=customUserDetails.getAuthorities().stream()
                .map(item->item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,customUserDetails.getCode() ,customUserDetails.getUsername(), customUserDetails.getEmail(),customUserDetails.getPhone(),customUserDetails.getTimeCheckin(),customUserDetails.getTimeCheckout(), listRoles));
    }
    @PostMapping("/checkio")
    public ResponseEntity<?> checkIOEmployee(@RequestBody CheckioRequest checkioRequest) {
        if (employeeService.findByCode(checkioRequest.getCode()) == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Please confirm again!"));
        }
        Employee employee = employeeService.findByCode(checkioRequest.getCode());
        Date nowDay = new Date();
        SimpleDateFormat sdf_s = new SimpleDateFormat("dd/MM/yyyy");
        String dayNow = sdf_s.format(nowDay);
        if (confirmService.checkEmployeeCheckedIn(dayNow,employee.getEmployeeId())==null) {
            Confirm confirm = new Confirm();
            String timeIEm = employee.getTimeCheckin();
            confirm.setEmployee(employee);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdf_hour = new SimpleDateFormat("HH:mm:ss");
            String timeNow = sdf.format(nowDay);
            String timeNo=sdf_hour.format(nowDay);
            //time mac dinh
            Date timeDefault = null; //gio defautl
            Date timeCheckIO=null;    //time checkin
            try {
                timeDefault = sdf_hour.parse(timeIEm);
                //time checkin
                timeCheckIO = sdf_hour.parse(timeNo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if(timeCheckIO.before(timeDefault)){
                confirm.setStatusCheckIn("good");
                long diffInMilliseconds = timeDefault.getTime() - timeCheckIO.getTime();
                long diffInMinutes = diffInMilliseconds / (60 * 1000);
                confirm.setCheckInLate((int) diffInMinutes);
            }
            else{
                confirm.setStatusCheckIn("dLate");
                long diffInMilliseconds = timeDefault.getTime() - timeCheckIO.getTime();
                int diffInMinutes = (int) (diffInMilliseconds / (60 * 1000));
                confirm.setCheckInLate(diffInMinutes);
            }
            confirm.setTimeCheckIn(timeNow);
            confirmService.saveOrUpdate(confirm);
            return ResponseEntity.ok("check in successful!");
        } else {
            Confirm confirmTwo = confirmService.checkEmployeeCheckedIn(dayNow,employee.getEmployeeId());
            String timeOEm = employee.getTimeCheckout();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdf_hour = new SimpleDateFormat("HH:mm:ss");
            String timeNow = sdf.format(nowDay);
            String timeNo=sdf_hour.format(nowDay);
            //time mac dinh
            Date timeDefault = null; //gio defautl
            Date timeCheckIO=null;    //time checkin/out
            try {
                timeDefault = sdf_hour.parse(timeOEm);
                //time checkin/out
                timeCheckIO = sdf_hour.parse(timeNo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if(timeCheckIO.before(timeDefault)){
                confirmTwo.setStatusCheckOut("vEarly");
                long diffInMilliseconds = timeCheckIO.getTime()-timeDefault.getTime();
                long diffInMinutes = diffInMilliseconds / (60 * 1000);
                confirmTwo.setCheckOutEarly((int) diffInMinutes);
            }
            else{
                confirmTwo.setStatusCheckOut("good");
                long diffInMilliseconds = timeCheckIO.getTime()-timeDefault.getTime();
                long diffInMinutes = diffInMilliseconds / (60 * 1000);
                confirmTwo.setCheckOutEarly((int) diffInMinutes);
            }
            confirmTwo.setTimeCheckOut(timeNow);
            confirmService.saveOrUpdate(confirmTwo);
            return ResponseEntity.ok("check out successfull!");
        }
    }
    @GetMapping(value = "/searchWithTime")
    public ResponseEntity<?> searchTimeEmployeeIO(@RequestParam(value = "dateStart",required = false) String dateStart,@RequestParam(value = "dateEnd",required = false) String dateEnd,@RequestParam(value = "username",required = false) String username) {
        if (dateStart == null && dateEnd == null) {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dateNow = sdf.format(now);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateNow, formatter);
            LocalDateTime firstDayOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
            LocalDateTime lastDayOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateS = firstDayOfWeek.format(format);
            String dateE = lastDayOfWeek.format(format);
            List<Confirm> list = confirmService.listCheckIOForEmployee(dateS, dateE,username);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
        else {
            List<Confirm> list = confirmService.listCheckIOForEmployee(dateStart, dateEnd,username);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
    }
    @GetMapping(value = "/searchErrorWithMonthForEmployee")
    public ResponseEntity<?> searchErrorTimeEmployeeIOWithMonthForEmployee(@RequestParam(value = "time", required = false) String time,@RequestParam(value = "username", required = false) String username) {
        if(time ==null){
            LocalDate currentDate = LocalDate.now();
            int month = currentDate.getMonthValue();
            int year = currentDate.getYear();
            String now=month+"/"+year;
            List<Confirm> list = confirmService.listCheckIOErrorForEmployee(now,username);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
        else{
            List<Confirm> list = confirmService.listCheckIOErrorForEmployee(time,username);
            List<EmployeeWithConfirmDTO> result= list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
    }
}
