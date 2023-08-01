package com.controller;


import com.dto.*;
import com.entity.*;
import com.mapper.ConfirmMapper;
import com.payload.request.AssignmentRequest;
import com.payload.response.MessageResponse;
import com.repository.CommentRepository;
import com.service.*;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin")
public class AdminController {
    private final EmployeeService employeeService;
    private final ConfirmService confirmService;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final ProjectService projectService;
    private final AssignmentService assignmentService;
    private final CommentService commentService;
    private final JavaMailSender javaMailSender;
    private final CommentRepository commentRepository;


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
        if (strRoles.isEmpty()) {
            Roles userRole = roleService.findByRoleName(ERole.ROLE_EMPLOYEE)
                    .orElseThrow(() -> new RuntimeException("Error: Employee is not found"));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Admin is not found"));
                        listRoles.add(adminRole);
                        break;
                    case "employee":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Admin is not found"));
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

    @DeleteMapping(value = "/delete")
    @Transactional
    public ResponseEntity<?> deleteEmployee(@RequestParam(value = "employeeId") int employeeId) {
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
    public ResponseEntity<?> searchTimeEmployeeCheckIO(@RequestParam(value = "dateStart", required = false) String dateStart,
                                                  @RequestParam(value = "dateEnd", required = false) String dateEnd,
                                                  @RequestParam(value = "limit", required = false) int limit,
                                                  @RequestParam(value = "pageRequest", required = false) int pageRequest) {
        if (dateStart == null && dateEnd == null) {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dateNow = sdf.format(now);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateNow, formatter);
            LocalDateTime firstDayOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                                    .with(LocalTime.MIN);
            LocalDateTime lastDayOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                                                    .with(LocalTime.MAX);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateS = firstDayOfWeek.format(format);
            String dateE = lastDayOfWeek.format(format);
            Pageable pageable= PageRequest.of(pageRequest -1,limit,Sort.by("employeeId"));
            Page<Confirm> list = confirmService.listEmployeeCheckIO(dateS, dateE,pageable);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToEmployeeWithConfirmDTO(confirm))
                                                                .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } else {
            Pageable pageable= PageRequest.of(pageRequest-1,limit,Sort.by("employeeId"));
            Page<Confirm> list = confirmService.listEmployeeCheckIO(dateStart, dateEnd,pageable);
            List<EmployeeWithConfirmDTO> result = new ArrayList<>();
            list.forEach(confirm -> {
                EmployeeWithConfirmDTO employeeWithConfirmDTO = ConfirmMapper.MAPPER.confirmToEmployeeWithConfirmDTO(confirm);
                result.add(employeeWithConfirmDTO);
            });
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping(value = "/search/searchErrorWithMonth")
    public ResponseEntity<?> searchErrorTimeEmployeeCheckIOInMonth(@RequestParam(value = "time", required = false) String time) {
        if (time == null) {
            LocalDate currentDate = LocalDate.now();
            int month = currentDate.getMonthValue();
            int year = currentDate.getYear();
            String now = month + "/" + year;
            List<Confirm> list = confirmService.listEmployeeCheckInError(now);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToEmployeeWithConfirmDTO(confirm))
                                                                .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } else {
            List<Confirm> list = confirmService.listEmployeeCheckInError(time);
            List<EmployeeWithConfirmDTO> result = list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToEmployeeWithConfirmDTO(confirm))
                                                                .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
    }
    @PostMapping("/employee/searchEmployeeByProject")
    public ResponseEntity<?> searchEmployeeByProject(@RequestParam(value = "codeProject") String codeProject){
        return ResponseEntity.ok(employeeService.searchEmployeesByProject(codeProject));
    }
    @PostMapping("/project/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO){
        if (projectService.existsByCode(projectDTO.getCode())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Code of project is already"));
        }
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }
    @PutMapping("/project/update")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDTO projectDTO){
        return ResponseEntity.ok(projectService.updateProject(projectDTO));
    }
    @DeleteMapping("/project/delete")
    public ResponseEntity<?> deleteProject(@RequestParam(value = "projectId") int projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("Deleted project successful! ");
    }
    @PostMapping("/project/assignEmployeeToProject")
    public ResponseEntity<?> assignEmployeeToTheProject(@RequestBody AssignmentRequest assignmentRequest){
        if(assignmentService.existsAssignmentByEmployee_CodeAndProject_Code(assignmentRequest.getCodeEmployee(),assignmentRequest.getCodeProject())){
            return ResponseEntity.badRequest().body("This employee assigned in project!");
        }
        Employee employee= employeeService.findByCode(assignmentRequest.getCodeEmployee());
        Project project= projectService.findByCode(assignmentRequest.getCodeProject());
        AssignmentDTO assignmentDTO= AssignmentDTO.builder()
                .employee(employee)
                .project(project)
                .roleProject(assignmentRequest.getRoleProject())
                .build();
                ;
        return ResponseEntity.ok(assignmentService.createAssignment(assignmentDTO));
    }
    @PostMapping("/comment/create")
    public ResponseEntity<?> createComment(@RequestParam(value = "codeEmployee") int codeEmployee
                                            ,@RequestParam(value = "codeProject") String codeProject
                                            ,@RequestBody CommentDTO commentDTO){
        Employee employee= employeeService.findByCode(codeEmployee);
        Project project= projectService.findByCode(codeProject);
        commentDTO=commentDTO.builder()
                .content(commentDTO.getContent())
                .point(commentDTO.getPoint())
                .employee(employee)
                .project(project)
                .build();
        return ResponseEntity.ok(commentService.createComment(commentDTO));
    }
    @PutMapping("/comment/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.updateComment(commentDTO));
    }
    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> deleteComment(@RequestParam(value = "commentId") int commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Deleted comment successful! ");
    }
    @GetMapping("/comment/filterCommentWithProject")
    public ResponseEntity<?> filterCommentWithProject(@RequestParam(value = "projectCode") String projectCode){
        return ResponseEntity.ok(commentService.filterCommentWithProject(projectCode));
    }
    @GetMapping("/comment/filterCommentWithEmployee")
    public ResponseEntity<?> filterCommentWithEmployee(@RequestParam(value = "employeeCode") int employeeCode){
        return ResponseEntity.ok(commentService.filterCommentWithEmployee(employeeCode));
    }
    @GetMapping("/comment/filterCommentWithTime")
    public ResponseEntity<?> filterCommentWithTime(@RequestParam(value = "start") String start
                                                    ,@RequestParam(value = "end") String end){
        return ResponseEntity.ok(commentService.filterCommentWithTime(start,end));
    }
}
