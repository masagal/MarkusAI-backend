package tech.masagal.markusai.user;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto, ServletRequest req) {
        User commissioner = (User) req.getAttribute("user");
        User newUser = userDto.toUser();

        UserDto savedUser = UserDto.fromUser(userService.saveUser(commissioner, newUser));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(ServletRequest req) {
        User user = (User) req.getAttribute("user");

        return ResponseEntity.ok(user);
    }

    @GetMapping("/resolve-invitation")
    public ResponseEntity<UserDto> resolveInvitation(ServletRequest req, @RequestParam String token) {
        String clerkId = (String) req.getAttribute("clerkId");
        if(clerkId == null || clerkId.isEmpty()) {
            throw new IllegalArgumentException("Valid token not supplied.");
        }
        User user = userService.associateUser(clerkId, token);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
