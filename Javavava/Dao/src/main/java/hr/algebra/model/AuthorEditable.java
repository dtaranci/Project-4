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
public interface AuthorEditable {

    int createAuthor(Author author);

    Set<Author> getAllAuthors();

    void updateAuthor(Author author);

    void deleteAuthor(Author author);

}
