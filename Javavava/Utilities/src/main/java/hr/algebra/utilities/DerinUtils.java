/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.utilities;

import java.util.Optional;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Derin
 */
public class DerinUtils {

    public DerinUtils() {
    }

    public static <T extends JComponent> Optional<JFrame> getJFrameFromComponent(T component) {
        //nicer
        return Optional.ofNullable((JFrame) SwingUtilities.getWindowAncestor(component));
    }
    
    public static <T extends JMenu> void setStatusMenuBar(T menu, String text) {

        if (!text.isBlank()) {
            menu.setText(text);
        }

    }

    
}
