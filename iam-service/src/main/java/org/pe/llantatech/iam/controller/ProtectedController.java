package org.pe.llantatech.iam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/iam/secure")
    public String secureEndpoint() {
        return "Access granted to protected IAM resource!";
    }
}
