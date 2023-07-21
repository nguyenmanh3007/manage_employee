package com.service.impl;


import com.dto.EmWithDto;
import com.entity.Confirm;
import com.repository.ConfirmRepository;
import com.service.ConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ConfirmServiceImpl implements ConfirmService {
    @Autowired
    private ConfirmRepository confirmRepository;
    @Override
    public Confirm saveOrUpdate(Confirm confirm) {
        return confirmRepository.save(confirm);
    }

    @Override
    public List<Confirm> findAll() {
        return confirmRepository.findAll();
    }

    @Override
    public boolean checkEmployeeCI(int EmployeeCode) {
        List<Confirm> list = confirmRepository.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date now= new Date();
        String dateNow= sdf.format(now);
        for(Confirm cf:list){
            if(cf.getEmployee().getCode()==EmployeeCode){
                if(cf.getTimeCheckIn().substring(0,cf.getTimeCheckIn().length()-9).equals(dateNow)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Confirm checkEmployeeCheckedIn(String timeCheck, int id) {
        return confirmRepository.checkEmployeeCheckedIn(timeCheck,id);
    }

    @Override
    public List<Confirm> listEmployeeCheckIO(String dStart, String dEnd) {
        return confirmRepository.listEmployeeCheckIO(dStart,dEnd);
    }

    @Override
    public List<Confirm> listEmployeeCheckInError(String time) {
        return confirmRepository.listEmployeeCheckInError(time);
    }

    @Override
    public List<Confirm> listCheckIOForEmployee(String dateStart, String dateEnd, String username) {
        return confirmRepository.listCheckIOForEmployee(dateStart,dateEnd,username);
    }

    @Override
    public List<Confirm> listCheckIOErrorForEmployee(String time, String username) {
        return confirmRepository.listCheckIOErrorForEmployee(time,username);
    }

    @Override
    public List<EmWithDto> listTest(String dateStart, String dateEnd) {
        return confirmRepository.listTest(dateStart,dateEnd);
    }
}
