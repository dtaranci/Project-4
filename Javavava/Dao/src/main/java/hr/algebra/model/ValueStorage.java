/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.model;

import hr.algebra.dal.GlobalValueStore;

/**
 *
 * @author 
 */
public interface ValueStorage {

    Role.RoleType getCurrentUserRole();

    void setCurrentUserRole(Role.RoleType currentUserRole);

    User getCurrentUser();

    void setCurrentUser(User currentUser);

    int getUSER_ROLE_ID();

    int getADMIN_ROLE_ID();
    
    GlobalValueStore.ThreadStatus getThreadWorkStatus();
    
    void changeThreadWorkStatus();

}
