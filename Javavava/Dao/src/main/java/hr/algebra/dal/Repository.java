/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Article;
import hr.algebra.model.Author;
import hr.algebra.model.Category;
import hr.algebra.model.Role;
import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lecturerc10
 */
public interface Repository {
    
    int createArticle(Article article) throws Exception;
    void createArticles(List<Article> articles) throws Exception;
    void updateArticle(int id, Article article) throws Exception;
    void deleteArticle(int id) throws Exception;
    Optional<Article> selectArticle(int id) throws Exception;
    List<Article> selectArticles() throws Exception;
    
    int createCategory(Category category) throws Exception;
    void createCategories(List<Category> categories) throws Exception;
    void updateCategory(int id, Category category) throws Exception;
    void deleteCategory(int id) throws Exception;
    Optional<Category> selectCategory(int id) throws Exception;
    List<Category> selectCategories() throws Exception;
    
    int createAuthor(Author author) throws Exception;
    void createAuthors(List<Author> authors) throws Exception;
    void updateAuthor(int id, Author author) throws Exception;
    void deleteAuthor(int id) throws Exception;
    Optional<Author> selectAuthor(int id) throws Exception;
    List<Author> selectAuthors() throws Exception;
    
    int addArticleCategory(int articleId, int categoryId) throws Exception;
    void addArticleCategories(int articleId, List<Category> categories) throws Exception;
    void removeArticleCategory(int articleId, int categoryId) throws Exception;
    void removeArticleCategories(int articleId, List<Category> categories) throws Exception;
    List<Category> selectArticleCategories(int articleId) throws Exception;
    
    int addArticleAuthor(int articleId, int authorId) throws Exception;
    void addArticleAuthors(int articleId, List<Author> authors) throws Exception;
    void removeArticleAuthor(int articleId, int authorId) throws Exception;
    void removeArticleAuthors(int articleId, List<Author> authors) throws Exception;
    List<Author> selectArticleAuthors(int articleId) throws Exception;
    
    int selectAuthorArticleCount(int authorId) throws Exception;
    int selectCategoryArticleCount(int categoryId) throws Exception;
    
    int createUser(User user) throws Exception;
    void updateUser(int id, User user) throws Exception;
    void deleteUser(int id) throws Exception;
    Optional<User> selectUser(int id) throws Exception;
    List<User> selectUsers() throws Exception;
    
    Optional<Role> selectRole(int id) throws Exception;
    List<Role> selectRoles() throws Exception;
    
    
    void deleteAll() throws Exception;
    
    

}
