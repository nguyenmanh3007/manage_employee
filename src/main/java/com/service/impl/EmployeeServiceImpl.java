package com.service.impl;

import com.converter.EmployeeConverter;
import com.dto.EmCoDTO;
import com.dto.EmployeeDTO;
import com.entity.Confirm;
import com.entity.Employee;
import com.entity.Roles;
import com.mapper.EmployeeMapper;
import com.repository.ConfirmRepository;
import com.repository.EmployeeRepository;
import com.service.EmployeeService;
import com.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final ConfirmRepository confirmRepository;
    private EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    private final RoleService roleService;

    @Override
    @Cacheable(value = "customer",key = "#username")
    public Employee findByUserName(String username) {
        return employeeRepository.findByUserName(username);
    }

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeDTO> findAll() {
        List<EmployeeDTO> result= employeeRepository.findAll().stream()
                .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean existsByUserName(String un) {
        return employeeRepository.existsByUserName(un);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public EmployeeDTO saveOrUpdate(EmployeeDTO employeeDTO) {
        return employeeMapper.employeeToEmployeeDto(employeeRepository.save(employeeMapper.employeeDtoToEmployee(employeeDTO)));
    }

    @Override
    public EmployeeDTO UpdateEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee= new Employee();
        Employee oldEmployee=  findByEmployeeId(employeeDTO.getEmployeeId());
        Set<String> strRoles = employeeDTO.getListRole();
        Set<Roles> listRoles = roleService.getRole(strRoles);
        employeeDTO=new EmployeeDTO().builder()
                .employeeId(employeeDTO.getEmployeeId())
                .userName(employeeDTO.getUserName())
                .password(employeeDTO.getPassword())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .timeCheckin(employeeDTO.getTimeCheckin())
                .timeCheckout(employeeDTO.getTimeCheckout())
                .created(oldEmployee.getCreated())
                .code(oldEmployee.getCode())
                .employeeStatus(oldEmployee.isEmployeeStatus())
                .confirms(oldEmployee.getConfirms())
                .comments(oldEmployee.getComments())
                .listRoles(listRoles)
                .build();
        newEmployee= employeeConverter.toEntity(oldEmployee,employeeDTO);
        return employeeMapper.employeeToEmployeeDto(employeeRepository.save(newEmployee));
    }

    @Override
    public EmployeeDTO SaveEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee= employeeMapper.employeeDtoToEmployee(employeeDTO);
        return employeeMapper.employeeToEmployeeDto(employeeRepository.save(newEmployee));
    }


    @Override
    public Employee findByCode(int id) {
        List<Employee> list= employeeRepository.findAll();
        for(Employee employee:list) {
            if (employee.getCode() == id) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public Employee findByEmployeeId(int id) {
        return employeeRepository.findByEmployeeId(id);
    }


    @Override
    @Transactional
    public void deleteByEmployeeId(int id) {
        employeeRepository.deleteRoleEmployee(id);
        employeeRepository.deleteByEmployeeId(id);
    }

    @Override
    public List<EmployeeDTO> listNotCheckOut() {
        Date now= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow=sdf.format(now);
        List<EmployeeDTO> result= employeeRepository.listNotCheckOut(dateNow).stream()
                .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<EmployeeDTO> listNotCheckIn() {
        Date now= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow=sdf.format(now);
        List<EmployeeDTO> result= employeeRepository.listNotCheckIn(dateNow).stream()
                .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<EmployeeDTO> findByUserNameASC(String username) {
        return employeeRepository.findByUserNameASC(username).stream()
                .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> listEmployeeIOwithTime(String dateStart, String dateEnd) {
        return employeeRepository.listEmployeeIOwithTime(dateStart,dateEnd).stream()
                .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public int createCode() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public Confirm employeeCheckInOut(int code) {
        Employee employee = employeeRepository.findByCode(code);
        String dayNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (confirmRepository.checkEmployeeCheckedIn(dayNow,employee.getEmployeeId())==null) {
            Confirm confirm = new Confirm();
            confirm.setEmployee(employee);
            String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String timeNo=new SimpleDateFormat("HH:mm:ss").format(new Date());
            //time mac dinh
            Date timeDefault = null;
            Date timeCheckIO=null;
            try {
                timeDefault = new SimpleDateFormat("HH:mm:ss").parse(employee.getTimeCheckin());
                timeCheckIO = new SimpleDateFormat("HH:mm:ss").parse(timeNo);
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
            return confirm;
        } else {
            Confirm confirmTwo = confirmRepository.checkEmployeeCheckedIn(dayNow,employee.getEmployeeId());
            String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String timeNo = new SimpleDateFormat("HH:mm:ss").format(new Date());
            //time mac dinh
            Date timeDefault = null;
            Date timeCheckIO=null;
            try {
                timeDefault = new SimpleDateFormat("HH:mm:ss").parse(employee.getTimeCheckout());
                //time checkin/out
                timeCheckIO = new SimpleDateFormat("HH:mm:ss").parse(timeNo);
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
            return confirmTwo;
        }
    }

    @Override
    public Page<EmCoDTO> findEmCoDTo(String username, Pageable pageable) {
        return employeeRepository.findEmCoDTo(username,pageable);
    }

    @Override
    public String[] getWeekAtNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), formatter);
        LocalDateTime firstDayOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        LocalDateTime lastDayOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateS = firstDayOfWeek.format(format);
        String dateE = lastDayOfWeek.format(format);
        String[] result= {dateS,dateE};
        return result;
    }

    @Override
    public List<EmployeeDTO> searchEmployeesByProject(String code) {
        List<EmployeeDTO> result= employeeRepository.searchEmployeesByProject(code).stream()
                .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
        return result;
    }

}
