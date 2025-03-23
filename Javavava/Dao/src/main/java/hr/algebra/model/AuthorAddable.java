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
public interface AuthorAddable {
    
    void addAuthors(Set<Author> authors);

    void removeAuthors(Set<Author> authors);

    Set<Author> getAllAuthors();

    Set<Author> getArticleAuthors();
}
