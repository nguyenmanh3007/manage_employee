package com.controller;

import com.dto.CommentDTO;
import com.dto.ConfirmDTO;
import com.dto.EmployeeDTO;
import com.entity.*;
import com.mapper.ConfirmMapper;
import com.payload.response.MessageResponse;
import com.repository.ConfirmRepository;
import com.repository.EmployeeRepository;
import com.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final CommentService commentService;
    private final ProjectService projectService;
    private final ConfirmService confirmService;
    private final EmployeeRepository confirmRepository;

    @PostMapping("/checkio")
    public ResponseEntity<?> checkIOEmployee(@RequestParam(value = "code") int code) {
        if (employeeService.findByCode(code) == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Please confirm again!"));
        }
        if (confirmService.checkEmployeeCheckedIn(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),employeeService.findByCode(code).getEmployeeId())==null) {
            Confirm confirm = employeeService.employeeCheckInOut(code);
            confirmService.saveOrUpdate(confirm);
            return ResponseEntity.ok("check in successful!");
        }
        else {
            Confirm confirm = employeeService.employeeCheckInOut(code);
            confirmService.saveOrUpdate(confirm);
            return ResponseEntity.ok("check out successful!");
        }
    }
    @GetMapping(value = "/searchWithTime")
    public ResponseEntity<?> searchTimeEmployeeIO(@RequestParam(value = "dateStart",required = false) String dateStart,
                                                  @RequestParam(value = "dateEnd",required = false) String dateEnd,
                                                  @RequestParam(value = "username",required = false) String username) {
        if (dateStart == null && dateEnd == null) {
            System.out.println(employeeService.getWeekAtNow()[0] +" "+ employeeService.getWeekAtNow()[1]);
            List<Confirm> list = confirmService.listCheckIOForEmployee(employeeService.getWeekAtNow()[0],employeeService.getWeekAtNow()[1],username);
            System.out.println(list);
            List<ConfirmDTO> result = list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToConfirmDTO(confirm)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
        else {
            List<Confirm> list = confirmService.listCheckIOForEmployee(dateStart, dateEnd,username);
            List<ConfirmDTO> result = list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToConfirmDTO(confirm)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
    }
    @GetMapping(value = "/searchErrorWithMonthForEmployee")
    public ResponseEntity<?> searchErrorTimeEmployeeIOWithMonthForEmployee(@RequestParam(value = "time", required = false) String time,
                                                                           @RequestParam(value = "username", required = false) String username) {
        if(time ==null){
            List<Confirm> list = confirmService.listCheckIOErrorForEmployee(confirmService.getMonthAtNow(),username);
            List<ConfirmDTO> result = list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToConfirmDTO(confirm)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
        else{
            List<Confirm> list = confirmService.listCheckIOErrorForEmployee(time,username);
            List<ConfirmDTO> result= list.stream().map(confirm ->  ConfirmMapper.MAPPER.confirmToConfirmDTO(confirm)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
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
        return ResponseEntity.ok("Deleted comment successful!");
    }
    @GetMapping(value = "/getEmployee")
    public ResponseEntity<?> getEmployee() {
        List<Employee> employee = confirmRepository.findAll();
        return ResponseEntity.ok(employee);
    }
}
