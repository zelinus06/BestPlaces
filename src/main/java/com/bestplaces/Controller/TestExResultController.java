package com.bestplaces.Controller;

import com.bestplaces.Service.FindExpectedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExResultController {
    @Autowired
    private FindExpectedResult findExpectedResult;
    @GetMapping("/rs")
    public String test() {
        return findExpectedResult.FindExResult();
    }
}
