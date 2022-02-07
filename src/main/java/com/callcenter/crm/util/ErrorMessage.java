package com.callcenter.crm.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private Date timestamp;
    private HttpStatus status;
    private String message;

}
