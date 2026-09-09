package org.example.lab6.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.lab6.Model.Employee;
import org.example.lab6.api.apiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class employeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmpolyee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            String massage=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(massage);
        }

        employees.add(employee);
        return ResponseEntity.status(200).body(new apiResponse("employee added successfully"));
    }

@PutMapping("/update/{index}")
    public ResponseEntity<?> updateEmployee(@PathVariable int index , @RequestBody @Valid Employee employee,Errors errors){
        if(errors.hasErrors()){
            String massage=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(massage);
        }
        if(index<0||index>=employees.size()){
            return ResponseEntity.status(400).body(new apiResponse("index out of pound"));
        }


        employees.set(index,employee);
        return ResponseEntity.status(200).body(new apiResponse("employee updated successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index){
        if(index<0||index>=employees.size()){
            return ResponseEntity.status(400).body(new apiResponse("index out of pound"));
        }

        employees.remove(index);
        return ResponseEntity.status(200).body(new apiResponse("employee deleted successfully"));
    }

    @GetMapping("/search/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position){
        if(!position.equalsIgnoreCase("supervisor")&&!position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(400).body(new apiResponse("position must be ether supervisor or coordinator"));
        }

        ArrayList<Employee> found = new ArrayList<>();

        for(Employee i:employees){
            if(i.getPosition().equalsIgnoreCase(position)){
                found.add(i);
            }
        }

        return ResponseEntity.status(200).body(found);
    }

@GetMapping("/ageRange/{minAge}/{maxAge}")
    public ResponseEntity<?> getByAgeRange(@PathVariable int minAge,@PathVariable int maxAge){
        if(minAge<25){
            return ResponseEntity.status(400).body(new apiResponse("min age must be more than 25"));
        }

        ArrayList<Employee> found = new ArrayList<>();
        for(Employee i:employees){
            if(i.getAge()>=minAge&&i.getAge()<=maxAge){
                found.add(i);
            }
        }

        return ResponseEntity.status(200).body(found);
    }


    @PutMapping("/applay/{id}")
    public ResponseEntity<?> applayForAnnualLeave(@PathVariable String id){
        for(Employee i :employees){
            if(i.getID().equalsIgnoreCase(id)){

                if(i.isOnLeave()){
                    return ResponseEntity.status(400).body(new apiResponse("you are in leave you cant apply to one "));
                }

                if(i.getAnnualLeave()<1){
                    return ResponseEntity.status(400).body(new apiResponse("you dont have annual leave remaining"));
                }

                i.setOnLeave(true);
                i.setAnnualLeave(i.getAnnualLeave()-1);
                return ResponseEntity.status(200).body(new apiResponse("your applying to annual leave success"));

            }
        }

        return ResponseEntity.status(400).body(new apiResponse("id not found"));
    }

    @GetMapping("/get/noAnnual")
public ResponseEntity<?> getEmployeeWithNoAnnualLeave(){
    ArrayList<Employee> found = new ArrayList<>();
    for(Employee i:employees){
        if(i.getAnnualLeave()<=0){
            found.add(i);
        }
    }

    return ResponseEntity.status(200).body(found);
}

@PutMapping("/promote/{sId}/{pId}")
public ResponseEntity<?> promote(@PathVariable String sId ,@PathVariable String pId){
        boolean found=false;

        for(Employee i:employees){
            if(i.getID().equalsIgnoreCase(sId)){
                if(!i.getPosition().equalsIgnoreCase("supervisor")){
                    return ResponseEntity.status(400).body(new apiResponse("you dont have access to promote"));
                }
                found= true;
            }
        }

        if(!found){
            return ResponseEntity.status(400).body(new apiResponse("supervisor id not found"));
        }

        for(Employee i:employees){
            if(i.getID().equalsIgnoreCase(pId)){
                if(i.getAge()<30){
                    return ResponseEntity.status(400).body(new apiResponse("must be older than 30"));
                }

                if(i.isOnLeave()){
                    return ResponseEntity.status(400).body(new apiResponse("in leave cant promote"));
                }

                i.setPosition("supervisor");
                return ResponseEntity.status(200).body(new apiResponse("the employee promoted"));
            }
        }

        return ResponseEntity.status(400).body(new apiResponse("employee id  not found"));
}




}
