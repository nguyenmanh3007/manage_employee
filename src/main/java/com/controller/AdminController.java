package com.controller;


import com.converter.ConfirmConverter;
import com.dto.EmCoDTO;
import com.dto.EmWithDto;
import com.dto.EmployeeDTO;
import com.dto.EmployeeWithConfirmDTO;
import com.entity.Confirm;
import com.entity.ERole;
import com.entity.Employee;
import com.entity.Roles;
import com.payload.response.MessageResponse;
import com.service.ConfirmService;
import com.service.EmployeeService;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ConfirmService confirmService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ConfirmConverter confirmConverter;
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO employeeDTO) throws MessagingException {
        if (employeeService.existsByUserName(employeeDTO.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already"));
        }
        if (employeeService.existsByEmail(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already"));
        }
        employeeDTO.setCode(employeeService.createCode());
        String pass = employeeDTO.getPassword();
        employeeDTO.setPassword(encoder.encode(employeeDTO.getPassword()));
        employeeDTO.setEmployeeStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        employeeDTO.setCreated(strNow);
        Set<String> strRoles = employeeDTO.getListRole();
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
        employeeDTO.setListRoles(listRoles);
        sendEmail(employeeDTO.getEmail(), employeeDTO.getUserName(), pass);
        return ResponseEntity.ok(employeeService.SaveEmployee(employeeDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.UpdateEmployee(employeeDTO));
    }

    @GetMapping("/test")
    @Transactional
    public ResponseEntity<?> update(@RequestParam(value = "pageRequest",required = false) int pageRequest,
                                    @RequestParam(value = "limit",required = false) int limit) {
        //List<EmWithDto> list= confirmService.listTest("05/07/2023","14/07/2023");
        //List<Confirm> list= confirmService.findAll();
        //list.forEach(x-> Hibernate.initialize(x.getEmployee()));

        //return ResponseEntity.ok(list);
        Pageable pageable= PageRequest.of(pageRequest-1,limit, Sort.by("code").descending().and(Sort.by("userName")));
        Page<EmCoDTO> list=employeeService.findEmCoDTo("user",pageable);
        //System.out.println(list.getTotalPages()+" "+ list.getNumber()+" "+list.getTotalElements());
        //List<EmWithDto> list= confirmService.listTest("05/07/2023","13/07/2023");
        return ResponseEntity.ok(list);

        //Persit,merge
//        Employee employee = new Employee();
//        employee.setEmployeeId(10);
//        employee.setCode(2597);
//        employee.setPassword("$2a$10$FWdVXt9UrdGyN6LDjCz/guCJwYprIXKAfRvVAd8qPOLBFcDNIIkYG");
//        employee.setUserName("sosad");
//        employee.setEmail("nggdfg@gmail.com");
//
//        System.out.println(confirmService.findAll());
//
//        Confirm confirm1 = new Confirm();
//        confirm1.setEmployee(employee);
//        Confirm confirm2 = new Confirm();
//        confirm2.setStatusCheckOut("vLate");
//        confirm2.setEmployee(employee);
//
//        Set<Confirm> confirms = new HashSet<>();
//        confirms.add(confirm1);
//        confirms.add(confirm2);
//        employee.setConfirms(confirms);

//        employee.getConfirms().add(confirm1);
//        employee.getConfirms().add(confirm2);
//        employeeService.saveOrUpdate(employee);
//        System.out.println(confirmService.findAll());
//        return ResponseEntity.ok("oke nhe");

        //Remove,detach
        //employeeService.deleteByEmployeeId(10);
        //return ResponseEntity.ok("oke nhe");
    }

    @DeleteMapping(value = "/delete")
    @Transactional
    public ResponseEntity<?> updateEmployee(@RequestParam(value = "employeeId") int employeeId) {
        employeeService.deleteByEmployeeId(employeeId);
        return ResponseEntity.ok("Delete employee successful!");
    }

    public void sendEmail(String mail, String username, String password) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMsg = "<h3>Congratulations on your successful account creation</h3>"
                + "<img src='https://ncc.asia/images/logo/logo.png'>";
        message.setContent(htmlMsg, "text/html");
        message.setText("username: " + username);
        message.setText("password: " + password);
        //FileSystemResource file = new FileSystemResource(new File("test.txt"));
        //helper.addAttachment("Demo Mail", file);
        helper.setTo(mail);
        helper.setSubject("Notice of successful registration!");
        javaMailSender.send(message);
    }

    @GetMapping(value = "/search/searchWithName")
    public ResponseEntity<?> searchEmployee(@RequestParam(value = "userName") String username) {
        List<Employee> lEmployees = employeeService.findByUserNameASC(username);
        if (lEmployees.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employee not found!"));
        }
        System.out.println(lEmployees);
        return ResponseEntity.ok(lEmployees);
    }

    @GetMapping(value = "/search/searchWithTime")
    public ResponseEntity<?> searchTimeEmployeeIO(@RequestParam(value = "dateStart", required = false) String dateStart, @RequestParam(value = "dateEnd", required = false) String dateEnd
            , @RequestParam(value = "limit", required = false) int limit, @RequestParam(value = "pageRequest", required = false) int pageRequest) {
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
            Pageable pageable= PageRequest.of(pageRequest -1,limit,Sort.by("employeeId"));
            Page<Confirm> list = confirmService.listEmployeeCheckIO(dateS, dateE,pageable);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } else {
            Pageable pageable= PageRequest.of(pageRequest-1,limit,Sort.by("employeeId"));
            Page<Confirm> list = confirmService.listEmployeeCheckIO(dateStart, dateEnd,pageable);
            List<EmployeeWithConfirmDTO> result = new ArrayList<>();
            list.forEach(confirm -> {
                EmployeeWithConfirmDTO employeeWithConfirmDTO = confirmConverter.toDTO(confirm);
                result.add(employeeWithConfirmDTO);
            });
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping(value = "/search/searchErrorWithMonth")
    public ResponseEntity<?> searchErrorTimeEmployeeIOWithMonth(@RequestParam(value = "time", required = false) String time) {
        if (time == null) {
            LocalDate currentDate = LocalDate.now();
            int month = currentDate.getMonthValue();
            int year = currentDate.getYear();
            String now = month + "/" + year;
            List<Confirm> list = confirmService.listEmployeeCheckInError(now);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } else {
            List<Confirm> list = confirmService.listEmployeeCheckInError(time);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirmConverter::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
    }
}
