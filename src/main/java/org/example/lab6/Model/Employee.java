package org.example.lab6.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID can not be empty")
    @Size(min = 2,message = "ID length must be grater then 2")
    private String ID;
    @NotEmpty(message = "name can not be empty")
    @Size(min = 4,message = "name length must be grater than 4")
    @Pattern(regexp = "^[A-Za-z]+$",message = "name must be only characters")
    private String name;

    @Email(message = "in valid email")
    private String Email;

    @Pattern(regexp = "^05\\d*$",message = "phone number must start with 05 ")
    @Size(min = 10,max = 10,message = "phone Number must be 10 numbers")
    private String phoneNumber;

    @NotNull(message = "age cant be null ")
    @Min(value = 25,message = "age must be greater than 25")
    private int age;

    @NotEmpty(message = "position cant be empty")
    @Pattern(regexp = "supervisor|coordinator",message = "position must be ether supervisor or coordinator")
    private String position;

    @AssertFalse(message = "onLeave must initiate false")
    private boolean onLeave;

    @NotNull(message = "hire Date cant be null")
    @PastOrPresent(message = "hire date must be in the past or today")
    private LocalDate hireDate;

    @NotNull(message = "annual leave cant be null ")
    @Positive(message = "annual leave cant be negative")
    private int annualLeave;
}
