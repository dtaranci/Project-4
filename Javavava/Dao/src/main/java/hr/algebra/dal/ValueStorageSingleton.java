/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.ValueStorage;

/**
 *
 * @author Derin
 */
public class ValueStorageSingleton {
    
    private static ValueStorage instance;
    
    private ValueStorageSingleton() {
    }
    
    public static ValueStorage getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    private static ValueStorage createInstance() {

        if (instance == null) {
            instance = new GlobalValueStore();
        }
        return instance;
    }
}
