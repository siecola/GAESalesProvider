package br.com.siecola.salesprovider.controller;

import br.com.siecola.salesprovider.exception.UserAlreadyExistsException;
import br.com.siecola.salesprovider.exception.UserNotFoundException;
import br.com.siecola.salesprovider.model.User;
import br.com.siecola.salesprovider.repository.UserRepository;
import br.com.siecola.salesprovider.util.CheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/api/users")
public class UserController {
    private static final Logger log = Logger.getLogger("UserController");

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        try {
            user.setRole(CheckRole.ROLE_USER);
            User userCreated = userRepository.saveUser(user);

            return new ResponseEntity<User>(userCreated,
                    HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PreAuthorize("hasAnyAuthority('" + CheckRole.ROLE_USER + "','" + CheckRole.ROLE_ADMIN + "')")
    @PutMapping(path = "/byemail")
    public ResponseEntity<User> updateUser(@RequestBody User user,
                                           @RequestParam("email") String email,
                                           Authentication authentication) {
        if (email.equals("matilde@siecola.com.br")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if ((user.getId() != null) && user.getId() != 0) {
            try {
                boolean hasRoleAdmin = CheckRole.hasRoleAdmin(authentication);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                if (hasRoleAdmin || userDetails.getUsername().equals(email)) {
                    if (!hasRoleAdmin) {
                        user.setRole(CheckRole.ROLE_USER);
                    }
                    return new ResponseEntity<User>(userRepository.updateUser(user,
                            email), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } catch (UserNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (UserAlreadyExistsException e) {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
        } else {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('" + CheckRole.ROLE_USER + "','" + CheckRole.ROLE_ADMIN + "')")
    @GetMapping("/byemail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email,
                                               Authentication authentication) {

        boolean hasRoleAdmin = CheckRole.hasRoleAdmin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (hasRoleAdmin || userDetails.getUsername().equals(email)) {
            Optional<User> optUser = userRepository.getByEmail(email);
            if (optUser.isPresent()) {
                return new ResponseEntity<User>(optUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAnyAuthority('" + CheckRole.ROLE_USER + "','" + CheckRole.ROLE_ADMIN + "')")
    @DeleteMapping(path = "/byemail")
    public ResponseEntity<User> deleteUser(
            @RequestParam("email") String email, Authentication authentication) {
        if (email.equals("matilde@siecola.com.br")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            boolean hasRoleAdmin = CheckRole.hasRoleAdmin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (hasRoleAdmin || userDetails.getUsername().equals(email)) {
                return new ResponseEntity<User>(userRepository.deleteUser(email),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}








