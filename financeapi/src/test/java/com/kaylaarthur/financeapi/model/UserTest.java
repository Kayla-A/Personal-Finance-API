package com.kaylaarthur.financeapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertEquals("Kayla", user.getName());
        assertEquals("kayla@email.com", user.getEmail());
        assertEquals("password0612", user.getPassword());
    } // testName

    @Test
    void testUserSetters() {
        User user = new User("test", "test", "test");
        
        user.setName("Kayla");
        user.setEmail("kayla@email.com");
        user.setPassword("password0612");

        assertEquals("Kayla", user.getName());
        assertEquals("kayla@email.com", user.getEmail());
        assertEquals("password0612", user.getPassword());
    } // testSetters 

    @Test
    void testEmptyConstructorArguments() {
        Exception e = assertThrows(IllegalArgumentException.class, () ->{
            new User("", "", "");
        }); 
        assertEquals("Name, email, and/or password cannot be null or empty", e.getMessage());
    } // testEmptyConstructorArguments

    @Test
    void testEmptyConstructorArgumentName() {
        Exception e = assertThrows(IllegalArgumentException.class, () ->{
            new User("", "kayla@email.com", "password0612");
        }); 
        assertEquals("Name, email, and/or password cannot be null or empty", e.getMessage());
    } // testEmptyConstructorArguments

    @Test
    void testNullConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () ->{
            new User(null, null, null);
        }); 
    } // testNullConstructorArguments

    @Test
    void testEmptyNameSetter() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertThrows(IllegalArgumentException.class, () ->{
            user.setName("");;
        }); 
    } // testEmptyNameSetter

    @Test
    void testNullNameSetter() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertThrows(IllegalArgumentException.class, () ->{
            user.setName(null);;
        }); 
    } // testNullNameSetter

    @Test
    void testEmptyEmailSetter() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertThrows(IllegalArgumentException.class, () ->{
            user.setEmail("");;
        }); 
    } // testEmptyEmailSetter

    @Test
    void testNullEmailSetter() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertThrows(IllegalArgumentException.class, () ->{
            user.setEmail(null);;
        }); 
    } // testNullEmailSetter

    @Test
    void testEmptyPasswordSetter() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertThrows(IllegalArgumentException.class, () ->{
            user.setPassword("");;
        }); 
    } // testEmptyPasswordSetter

    @Test
    void testNullPasswordSetter() {
        User user = new User("Kayla", "kayla@email.com", "password0612");
        assertThrows(IllegalArgumentException.class, () ->{
            user.setPassword(null);;
        }); 
    } // testNullPasswordSetter

} // UserTest
