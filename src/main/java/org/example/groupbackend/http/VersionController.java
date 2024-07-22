package org.example.groupbackend.http;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/version")
public class VersionController {

    @GetMapping
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("0.0.1");
    }
}
