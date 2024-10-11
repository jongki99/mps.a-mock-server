package com.example.demo.app.config.test.modelmapper;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
	
    private String name;
//    private String birthDate;  // 날짜를 String으로 표현
    private Date birthDate;  // 날짜를 String으로 표현

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

//    public String getBirthDate() {
//        return birthDate;
//    }
    public Date getBirthDate() {
    	return birthDate;
    }

//    public void setBirthDate(String birthDate) {
//        this.birthDate = birthDate;
//    }
}