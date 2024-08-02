package com.example.memeber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-multiple")
    public ResponseEntity<String> registerMultipleUsers(@RequestBody CountRequest request) {
        try {
            long count = request.getCount();
            if (count <= 0 || count >= 999_999_999_999L) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Count must be between 1 and 999_999_999_999");
            }

            userService.registerMultipleUsers(count);

            return ResponseEntity.status(HttpStatus.CREATED).body("Users registration started.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the request: " + e.getMessage());
        }
    }

    static class CountRequest {
        private long count;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
