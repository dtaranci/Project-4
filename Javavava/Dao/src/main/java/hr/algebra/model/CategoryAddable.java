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
public interface CategoryAddable {
    
    void addCategories(Set<Category> categories);

    void removeCategories(Set<Category> categories);

    Set<Category> getAllCategories();

    Set<Category> getArticleCategories();
}
