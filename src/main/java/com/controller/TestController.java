package com.controller;


import com.dto.EmployeeDTO;
import com.entity.Confirm;
import java.util.Optional;
import com.entity.Employee;
import com.mapper.EmployeeMapper;
import com.payload.response.MessageResponse;
import com.service.ConfirmService;
import com.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

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
        List<EmployeeDTO> list=employeeService.findByUserNameASC("user");
        return ResponseEntity.ok(list);

    }
    @GetMapping("/cascade/test")
    public ResponseEntity<?> testCascade(@RequestParam(value = "pageRequest",required = false) int pageRequest,
                                         @RequestParam(value = "limit",required = false) int limit,
                                         @RequestParam(value = "id",required = false) int id) {
        //Customizing the Result with Class Constructors
//        Pageable pageable= PageRequest.of(pageRequest-1,limit, Sort.by("code").descending().and(Sort.by("userName")));
//        Page<EmCoDTO> list=employeeService.findEmCoDTo("user",pageable);
//        List<EmployeeDTO> result=employeeService.listEmployeeIOwithTime("12/07/2023","27/07/2023");
//        return ResponseEntity.ok(list);

        //Customizing the Result with Spring Data Projection (open/close)
//        List<EmWithDto> list= confirmService.listTest("05/07/2023","14/07/2023");
//        return ResponseEntity.ok(list);

        //Persit,merge
        Employee employee = Employee.builder()
           //     .employeeId(93)
                .code(2593)
                .password("$2a$10$FWdVXt9UrdGyN6LDjCz/guCJwYprIXKAfRvVAd8qPOLBFcDNIIkYG")
                .userName("aloha")
                .email("nsdfds@gmail.com")
                .build();
        System.out.println(confirmService.findAll().size());

        Confirm confirm1 = Confirm.builder()
             //   .id(40)
                .statusCheckIn("DLATE")
                .employee(employee)
                .build();
        Confirm confirm2 = Confirm.builder()
             //   .id(41)
                .statusCheckOut("VEARLY")
                .employee(employee)
                .build();
        Set<Confirm> confirms = new HashSet<>();
        confirms.add(confirm1);
        confirms.add(confirm2);
        employee.setConfirms(confirms);
        employeeService.saveEmployee(employee);
        System.out.println(confirmService.findAll().size());
        return ResponseEntity.ok(EmployeeMapper.MAPPER.employeeToEmployeeDto(employee));

        //Remove,detach
//        employeeService.deleteByEmployeeId(92);
//        return ResponseEntity.ok("oke nhe");
    }
    @GetMapping("/fetch/test")
    public ResponseEntity<?> testFetchType() {
        //LAZY
        List<Confirm> list= confirmService.findAll();
//        list.forEach(x-> Hibernate.initialize(x.getEmployee()));
        return ResponseEntity.ok(list);
    }
    @GetMapping("/restTemplate/test")
    public void testRestTemplate(@RequestHeader("Authorization") String token, @RequestParam(value = "country",required = false)
                                                          String country
                                                          ){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.substring(7));
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://hrm-api.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn";
//        String fooResourceUrl
//                = "https://restcountries.com/v3.1/name";
//        ResponseEntity<String> response
//                = restTemplate.getForEntity(fooResourceUrl+"/"+country, String.class);
        restTemplate.exchange(
                "http://localhost:8080/api/employee/getEmployee",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
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
                        .build(userId))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,clientResponse ->
                        Mono.error((Supplier<? extends Throwable>) new MessageResponse("post not found")))
                .onStatus(HttpStatus::is5xxServerError,clientResponse ->
                        Mono.error((Supplier<? extends Throwable>) new MessageResponse("server error")))
                .bodyToMono(String.class)
                .block();
//        Flux<Staff> response_two = client.get().uri("http://hrm-api.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn")
//                                    .retrieve().bodyToFlux(Staff.class);
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
    @Transactional(rollbackOn = {ChangeSetPersister.NotFoundException.class})
    @DeleteMapping("/employee/delete")
    public void deleteUser(@RequestParam Integer userId) throws ChangeSetPersister.NotFoundException {
        Employee user = employeeService.findByEmployeeId(userId);
//                .orElseThrow(() -> new RuntimeException("Error: Employee role is not found"))
        employeeService.deleteByEmployeeId(userId);
        throw new ChangeSetPersister.NotFoundException();
    }

}
