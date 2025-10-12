package com.pos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecretController {

    @Value("${db-url}")
    private String dbUrl;

    @Value("${db-username}")
    private String dbUserName;

    @Value("${db-password}")
    private String dbPassword;

    @Value("${default.user.profile.image}")
    private String defaultImage;

    @Value("${jwt-secret-key}")
    private String jwtSecretKey;

    @Value("${frontend-url}")
    private String frontendUrl;

    @GetMapping
    public ResponseEntity<?> getAllSecret(){
        Map<String,String> map = new HashMap<>();
        map.put("db_url", dbUrl);
        map.put("db_username", frontendUrl);
        map.put("db_password", dbPassword);
        map.put("default image", defaultImage);
        map.put("jwt-secret-key", jwtSecretKey);
        return ResponseEntity.ok(map);
    }

}
