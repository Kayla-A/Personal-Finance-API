package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.repository.UserRepo;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    } // UserRepo
    
    public User createUser(String name, String email, String password) {

        // validate name
        if(!validateName(name)) {
            throw new IllegalArgumentException("Name must be valid");
        } // if
        
        // validate email
        if(!validateEmail(email)) {
            throw new IllegalArgumentException("Email must be valid");
        } // if
    
        // validate and hash password
        if(!validatePassword(password)) {
            throw new IllegalArgumentException("Password must be valid");
        } // if
        
        String hashedPass = hashPassword(password); // do checks for if null
        // save with UserRepo
        User user = new User(name, email, hashedPass);
        return userRepo.save(user);
    } // createUser

    /*
        ******** Helper methods ********
    */

    /**
     * 
     * @param name
     * @return
     */
    private boolean validateName(String name) {
        // check there is a name
        if(name == null) { return false; }

        // check name isn't taken
        if( userRepo.findByName(name) != null ) { return false; }
        // check name is valid
        String regex = "^\\w[\\w\\-''.]{2,31}$"; // support unicode?
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);

        return m.matches();
    } // validateName

    /**
     * description
     * does not validate eamil domain or tld
     * @param email
     * @return
     */
    private boolean validateEmail(String email) {
        // check there is an email
        if(email == null) { return false; }

        // check email isn't taken
        if(userRepo.findByEmail(email) != null) { return false; }
        // check name is valid structure
        //CHECK REGEX
        String regex = "^[\\w\\-][\\w\\-(?!.*\\.\\.)].+@[valid domain].+\\.[a-zA-z]{2,}.+$"; // support unicode?
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    } // validateEmail

    /**
     * 
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        // check there is an password
        if(password == null) { return false; }

        // check password is valid
        String regex = "[\\w\\W(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*\\W)].+{8,64}$"; // support unicode?
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if(!m.matches()) { return false; }

        // check password isn't taken 
        if(userRepo.findByHashed(password) != null) { return false; }
        
        return true;
    } // validatePassword

    /**
     * 
     * @param password
     * @return
     */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hashed = encoder.encode(password);
        return encoder.matches(password, hashed) ? hashed : null;
    } // hashPassword

} // UserService
