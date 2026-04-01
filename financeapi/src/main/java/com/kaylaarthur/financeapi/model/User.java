package com.kaylaarthur.financeapi.model;


/**
 * Represents a basic User. A user is an individual that interacts with
 * the system to add information to the system and access information from the system.
 * 
 * This class provides methods for getting and setting user information.
 */
public class User {
    
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
    User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    } // User

    /**
     * Returns the name of the user.
     * @return The user's name.
     */
    public String getName() { return name; } // getName

   /**
    * Sets the name of the user.
    * @param name The user's new name.
    */
    public void setName(String name) { this.name = name; } // setName

    /**
     * Returns the user's email.
     * @return The user's email.
     */
    public String getEmail() { return email; } // getEmail

    /**
     * Sets the user's email.
     * @param email The user's new email.
     */
    public void setEmail(String email) { this.email = email; } // setEmail

    /**
     * Returns the user's password.
     * @return The users password
     */
    public String getPassword() { return password; } // getPassword

    /**
     * Sets the user's password.
     * @param password The user's new password
     */
    public void setPassword(String password) { this.password = password; } // setPassword

} // User 
