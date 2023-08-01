package com.controller;


import com.dto.EmCoDTO;
import com.dto.EmployeeDTO;
import com.entity.Confirm;
import com.entity.Employee;
import com.mapper.EmployeeMapper;
import com.payload.response.MessageResponse;
import com.service.ConfirmService;
import com.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/admin/test")
public class TestController {
    private final EmployeeService employeeService;
    private final ConfirmService confirmService;
    private final CacheManager cacheManager;

    @GetMapping("/cache/delete")
    @Transactional
    public ResponseEntity<?> delete(@RequestParam(value = "pageRequest",required = false) int pageRequest,
                                    @RequestParam(value = "limit",required = false) int limit,
                                    @RequestParam(value = "id",required = false) int id) {
        Cache cache = cacheManager.getCache("employees");
        cache.evict(id);
        return ResponseEntity.ok("oke");

    }
    @GetMapping("/cache/get")
    @Transactional
    public ResponseEntity<?> update(@RequestParam(value = "pageRequest",required = false) int pageRequest,
                                    @RequestParam(value = "limit",required = false) int limit,
                                    @RequestParam(value = "id",required = false) int id) {
        Cache cache = cacheManager.getCache("employees");
        Cache.ValueWrapper valueWrapper = cache.get(id);
        if (valueWrapper == null) {
            return ResponseEntity.ok("Employees not found!");
        }
        return ResponseEntity.ok(valueWrapper.get());
    }
    @GetMapping("/cache/find")
    @Transactional
    public ResponseEntity<?> find(@RequestParam(value = "pageRequest",required = false) int pageRequest,
                                  @RequestParam(value = "limit",required = false) int limit,
                                  @RequestParam(value = "id",required = false) int id) {
        Confirm list= confirmService.findById(id);
        return ResponseEntity.ok(list);

    }
    @GetMapping("/mapstruc/get")
    public ResponseEntity<?> findEm() {
        List<Employee> list=employeeService.findByUserNameASC("user");
        List<EmployeeDTO> result=list.stream()
                .map(employee ->  EmployeeMapper.MAPPER.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);

    }
    @GetMapping("/cascade/test")
    @Transactional
    public ResponseEntity<?> testCascade(@RequestParam(value = "pageRequest",required = false) int pageRequest,
                                         @RequestParam(value = "limit",required = false) int limit,
                                         @RequestParam(value = "id",required = false) int id) {
        //Customizing the Result with Class Constructors
        Pageable pageable= PageRequest.of(pageRequest-1,limit, Sort.by("code").descending().and(Sort.by("userName")));
        Page<EmCoDTO> list=employeeService.findEmCoDTo("user",pageable);
        List<Employee> result=employeeService.listEmployeeIOwithTime("12/07/2023","27/07/2023");
        return ResponseEntity.ok(list);

        //Customizing the Result with Spring Data Projection (open/close)
//        List<EmWithDto> list= confirmService.listTest("05/07/2023","14/07/2023");
//        return ResponseEntity.ok(list);


        // LAZY
        //List<Confirm> list= confirmService.findAll();
        //list.forEach(x-> Hibernate.initialize(x.getEmployee()));
        //return ResponseEntity.ok(list);


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
    @GetMapping("/restTemplate/test")
    public ResponseEntity<?> testRestTemplate(@RequestParam(value = "country",required = false)
                                                          String country
                                                          ){
        RestTemplate restTemplate = new RestTemplate();
//        String fooResourceUrl
//                = "http://hrm-api.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn";
        String fooResourceUrl
                = "https://restcountries.com/v3.1/name";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl+"/"+country, String.class);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/webClient/test")
    public ResponseEntity<?> testWebclient(@RequestParam(value = "id",required = false) int id,
                                           @RequestParam(value = "userId",required = false) int userId,
                                           @RequestParam(value = "title",required = false) String title,
                                           @RequestParam(value = "body",required = false) String  body

    ){

        //GET
        WebClient client= WebClient.create();
        String response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("jsonplaceholder.typicode.com")
                        .pathSegment("posts","{post}")
                        .build(id))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,clientResponse ->
                        Mono.error((Supplier<? extends Throwable>) new MessageResponse("post not found")))
                .onStatus(HttpStatus::is5xxServerError,clientResponse ->
                        Mono.error((Supplier<? extends Throwable>) new MessageResponse("server error")))
                .bodyToMono(String.class)
                .block();
                return ResponseEntity.ok(response);
        //POST
//        WebClient client= WebClient.create();
//        String requestBody = "{\"userId\":userId , \"title\": title,\"body\":body}";
//        String response = client.post()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .host("jsonplaceholder.typicode.com")
//                        .pathSegment("posts")
//                        .build(id))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(BodyInserters.fromValue(requestBody))
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError,clientResponse ->
//                        Mono.error((Supplier<? extends Throwable>) new MessageResponse("post not found")))
//                .onStatus(HttpStatus::is5xxServerError,clientResponse ->
//                        Mono.error((Supplier<? extends Throwable>) new MessageResponse("server error")))
//                .bodyToMono(String.class)
//                .block();
//                return ResponseEntity.ok(response);
       }

}
