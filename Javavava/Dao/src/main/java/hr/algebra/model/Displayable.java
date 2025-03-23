/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.model;

/**
 *
 * @author Derin
 */
public interface Displayable {
    default String displayInfo() {
        return getClass().getSimpleName();
    }
}
