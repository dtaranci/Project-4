/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Role;
import hr.algebra.model.User;
import hr.algebra.model.ValueStorage;

/**
 *
 * @author Derin
 */

//for storing values easily accessible by any part of the program
public class GlobalValueStore implements ValueStorage {

    private Role.RoleType currentUserRole;
    private User currentUser;
    private final int USER_ROLE_ID = 1;
    private final int ADMIN_ROLE_ID = 2;
    
    private Boolean threadWorkStatus = false;
    
    //default modifier constructor (no modifier specified) restricts access to same package only
    //so the singleton class can use it
    GlobalValueStore() {
    }
    
    @Override
    public Role.RoleType getCurrentUserRole() {
        return currentUserRole;
    }

    @Override
    public void setCurrentUserRole(Role.RoleType currentUserRole) {
        this.currentUserRole = currentUserRole;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    @Override
    public int getUSER_ROLE_ID() {
        return USER_ROLE_ID;
    }

    @Override
    public int getADMIN_ROLE_ID() {
        return ADMIN_ROLE_ID;
    }

    @Override
    public ThreadStatus getThreadWorkStatus() {
        if (threadWorkStatus) {
            return ThreadStatus.WORKING;
        }
        else {
            return ThreadStatus.STANDBY;
        }
    }

    @Override
    public void changeThreadWorkStatus() {
        this.threadWorkStatus = !threadWorkStatus;
    }
    
    public enum ThreadStatus {
        
        WORKING(1),
        STANDBY(2);

        private final int value;
        
        ThreadStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}
