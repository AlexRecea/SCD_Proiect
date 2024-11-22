package com.example.ProiectReceaSCD2.Controllers;

import com.example.ProiectReceaSCD2.Services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    public TestService testService;

    @GetMapping("/getTest")
    @ResponseBody
    public String testServiciu () {
       testService.functie();
       return "test";
    }


}
