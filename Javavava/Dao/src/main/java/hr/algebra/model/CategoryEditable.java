/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.model;

import java.util.Set;

/**
 *
 * @author Derin
 */
public interface CategoryEditable {
    
    int createCategory(Category category);

    Set<Category> getAllCategories();

    void updateCategory(Category category);

    void deleteCategory(Category category);
}
