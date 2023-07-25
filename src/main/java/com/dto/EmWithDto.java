package com.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

public interface EmWithDto {
//    @Value("#{target.timeCheckIn}")
//    String getTimeCheckIn();
//    @Value("#{target.timeCheckOut}")
//    String getTimeCheckOut();
      int getCode();
      String getUserName();
      String getTimeCheckIn();
      String getTimeCheckOut();
      int getCheckInLate();
      int getCheckOutEarly();
      String getStatusCheckIn();
      String getStatusCheckOut();
}
