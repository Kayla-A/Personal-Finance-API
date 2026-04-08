package com.kaylaarthur.financeapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Represents a basic User. A user is an individual that interacts with
 * the system to add information to the system and access information from the system.
 * 
 * This class provides methods for getting and setting user information.
 */
@Entity
public class User {
    @Id
    private long id;
    private String name; 
    private String email;
    private String password;

    /**
     * Creates a new User with the specified name, email and password.
     * 
     * @param name The user's name.
     * @param email The user's email.
     * @param password The user's password.
     */
    public User(String name, String email, String password) {
        if(name == null || email == null || password == null ||
            name == "" || email == "" || password == "") {
            throw new IllegalArgumentException("Name, email, and/or password cannot be null or empty");
        } // if
        this.name = name;
        this.email = email;
        this.password = password;
    } // User

    /**
     * Creates a new User with the specified id, name, email and password.
     * 
     * @param id The user's id.
     * @param name The user's name.
     * @param email The user's email.
     * @param password The user's password.
     */
    public User(long id, String name, String email, String password) {
        this.id = id;
        if(name == null || email == null || password == null ||
            name == "" || email == "" || password == "") {
            throw new IllegalArgumentException("Name, email, and/or password cannot be null or empty");
        } // if
        this.name = name;
        this.email = email;
        this.password = password;
    } // User

    /**
     * Returns the user's id.
     * @return The user's id.
     */
    public long getId() { return id; } // getId

    /**
     * Sets the user's id.
     * @param id The user's id.
     */
    public void setId(long id) { this.id = id; }

    /**
     * Returns the name of the user.
     * @return The user's name.
     */
    public String getName() { return name; } // getName

   /**
    * Sets the name of the user.
    * @param name The user's new name.
    */
    public void setName(String name) { 
        if(name == null || name == "") {
            throw new IllegalArgumentException("Name cannot be null or empty");
        } // if
        this.name = name; 
    } // setName

    /**
     * Returns the user's email.
     * @return The user's email.
     */
    public String getEmail() { return email; } // getEmail

    /**
     * Sets the user's email.
     * @param email The user's new email.
     */
    public void setEmail(String email) { 
        if(email == null || email == "") {
            throw new IllegalArgumentException("Email cannot be null or empty");
        } // if
        this.email = email; 
    } // setEmail

    /**
     * Returns the user's password.
     * @return The users password
     */
    public String getPassword() { return password; } // getPassword

    /**
     * Sets the user's password.
     * @param password The user's new password
     */
    public void setPassword(String password) { 
        if(password == null || password == "") {
            throw new IllegalArgumentException("Password cannot be null or empty");
        } // if
        this.password = password; 
    } // setPassword

} // User 
