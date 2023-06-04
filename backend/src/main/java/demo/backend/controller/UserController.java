package demo.backend.controller;

import demo.backend.model.User;
import demo.backend.repo.UserRepo;
import org.springframework.web.bind.annotation.*;
import demo.backend.exception.UserNotFoundException;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    private UserRepo userRepo;

    // Created for IoC container. Instead of @Autowired annotation. This is contructor injection
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/user")
    User newUser(@RequestBody User user ){
        return userRepo.save(user);
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @GetMapping("user/{id}")
    User getUserById(@PathVariable Long id ){
        return userRepo.findById(id)
                .orElseThrow( ()->new UserNotFoundException(id) );
    }

    @PutMapping("user/{id}")
    User updateUserById(@PathVariable Long id, @RequestBody User newUser){
        return userRepo.findById(id)
                .map( user -> {
                    user.setName(newUser.getName());
                    user.setUsername(newUser.getUsername());
                    user.setEmail(newUser.getEmail());
                    return userRepo.save(user);
                }).orElseThrow( ()->new UserNotFoundException(id) );
    }

    @DeleteMapping("user/{id}")
    String deleteUserById(@PathVariable Long id){
        if(!userRepo.existsById(id)){
            throw  new UserNotFoundException(id);
        }
        userRepo.deleteById(id);

        return "user with id " + id + " has been deleted successfully";
    }

}
