/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.model;

import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Derin
 */
public interface JComponentInvisibleSettable {
    
    default <T extends JComponent> void hideErrors(List<T> components) {
        components.forEach(x -> x.setVisible(false));
    }
    
}
