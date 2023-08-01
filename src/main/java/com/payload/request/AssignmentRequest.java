package com.payload.request;

import com.entity.ERoleProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequest {
    private int codeEmployee;
    private String codeProject ;
    private ERoleProject roleProject;
}
