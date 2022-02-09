package com.example.UserAPI.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
public class ResponseObject {

    public HttpStatus httpStatus;
    public String message;
    public Timestamp timestamp;

    public ResponseObject(HttpStatus httpStatus, String message){

        this.httpStatus=httpStatus;
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());

    }


}
