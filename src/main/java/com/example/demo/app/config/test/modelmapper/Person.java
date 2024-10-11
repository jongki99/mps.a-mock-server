package com.example.demo.app.config.test.modelmapper;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Person {
	
    private String name;
    private LocalDateTime birthDate;

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

//    public void setBirthDate(LocalDateTime birthDate) {
//        this.birthDate = birthDate;
//    }
}