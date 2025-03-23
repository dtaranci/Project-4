/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.model;

import hr.algebra.dal.GlobalValueStore;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @author Derin
 */


public class ThreadStatic {

    public ThreadStatic() {
    }

    //Function
    public static <T extends ValueStorage> Function<T, Optional<String>> getThreadStatusString() {
        return x -> {

            GlobalValueStore.ThreadStatus status = x.getThreadWorkStatus();

            if (status != null) {
                //Upper case first letter, lower case for the rest + ... at the end
                return Optional.of(status.name().substring(0, 1).toUpperCase() + status.name().substring(1).toLowerCase() + "...");
            } else {
                return Optional.empty();
            }
        };
    }
}

