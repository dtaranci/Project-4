/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Article;
import hr.algebra.model.Author;
import hr.algebra.model.Category;
import hr.algebra.model.Role;
import hr.algebra.model.User;
import java.security.spec.NamedParameterSpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class SqlRepository implements Repository {

    private static final String ID_ARTICLE = "ID_Article";
    private static final String TITLE = "Title";
    private static final String LINK = "Link";
    private static final String DESCRIPTION = "Description";
    private static final String PICTURE_PATH = "PictureURL";
    private static final String PUBLISHED_DATE = "PublishedDate";

    private static final String ID_CATEGORY = "ID_Category";
    private static final String CATEGORY_NAME = "Name";
    
    private static final String ID_AUTHOR = "ID_Author";
    private static final String ID_CREATORFIX = "ID_Creator";
    private static final String AUTHOR_NAME = "Name";
    
    private static final String ID_ARTICLE_CATEGORY = "ID_ArticleCategory";
    private static final String ID_ARTICLE_AUTHOR = "ID_ArticleAuthor";
    
    private static final String AUTHOR_ARTICLE_COUNT = "Count";
    private static final String CATEGORY_ARTICLE_COUNT = "Count";
    
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String ROLE_ID = "Role_ID";
    private static final String ID_USER = "ID_User";
    private static final String ID_ROLE = "ID_Role";
    private static final String ROLE_NAME = "Role_name";
    
    // PROCEDURES //////////////////////////////////////////////////////////////////////////////////////////
    private static final String CREATE_ARTICLE = "{ CALL createMovieArticle (?,?,?,?,?,?) }";
    private static final String UPDATE_ARTICLE = "{ CALL updateMovieArticle (?,?,?,?,?,?) }";
    private static final String DELETE_ARTICLE = "{ CALL deleteMovieArticle (?) }";
    private static final String SELECT_ARTICLE = "{ CALL selectMovieArticle (?) }";
    private static final String SELECT_ARTICLES = "{ CALL selectMovieArticles }";
    
    private static final String CREATE_CATEGORY = "{ CALL createCategory (?,?)}";
    private static final String UPDATE_CATEGORY = "{ CALL updateCategory (?,?)}";
    private static final String DELETE_CATEGORY = "{ CALL deleteCategory (?)}";
    private static final String SELECT_CATEGORY = "{ CALL selectCategory (?) }";
    private static final String SELECT_CATEGORIES = "{ CALL selectCategories }";
    
    private static final String CREATE_AUTHOR = "{ CALL createAuthor (?,?)}";
    private static final String UPDATE_AUTHOR = "{ CALL updateAuthor (?,?)}";
    private static final String DELETE_AUTHOR = "{ CALL deleteAuthor (?)}";
    private static final String SELECT_AUTHOR = "{ CALL selectAuthor (?) }";
    private static final String SELECT_AUTHORS = "{ CALL selectAuthors }";
    
    private static final String CREATE_ARTICLE_AUTHOR = "{ CALL createArticleAuthor (?,?,?) }";
    private static final String DELETE_ARTICLE_AUTHOR = "{ CALL deleteArticleAuthor (?,?) }";
    private static final String SELECT_ARTICLE_AUTHORS = "{ CALL selectArticleAuthors (?) }";
    
    private static final String CREATE_ARTICLE_CATEGORY = "{ CALL createArticleCategory (?,?,?) }";
    private static final String DELETE_ARTICLE_CATEGORY = "{ CALL deleteArticleCategory (?,?) }";
    private static final String SELECT_ARTICLE_CATEGORIES = "{ CALL selectArticleCategories (?) }";
    
    private static final String SELECT_AUTHOR_ARTICLE_COUNT = "{ CALL selectAuthorArticleCount (?,?) }";
    private static final String SELECT_CATEGORY_ARTICLE_COUNT = "{ CALL selectCategoryArticleCount (?,?) }";
    
    private static final String CREATE_USER = "{ CALL createUser (?,?,?,?)}";
    private static final String UPDATE_USER = "{ CALL updateUser (?,?)}";
    private static final String DELETE_USER = "{ CALL deleteUser (?)}";
    private static final String SELECT_USER = "{ CALL selectUser (?) }";
    private static final String SELECT_USERS = "{ CALL selectUsers }";
    
    private static final String SELECT_ROLE = "{ CALL selectRoles (?) }";
    private static final String SELECT_ROLES = "{ CALL selectRoles }";
    
    private static final String DELETE_ALL = "{ CALL deleteall }";
    

    @Override
    public int createArticle(Article article) throws Exception {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ARTICLE)) {
            
            
            stmt.setString(TITLE, article.getTitle());
            stmt.setString(LINK, article.getLink());
            stmt.setString(DESCRIPTION, article.getDescription());
            stmt.setString(PICTURE_PATH, article.getPicturePath());
            stmt.setString(PUBLISHED_DATE, article.getPublishedDate()
                    .format(Article.DATE_FORMATTER)
            );
            stmt.registerOutParameter(ID_ARTICLE, Types.INTEGER);
            stmt.executeUpdate();
            
            return stmt.getInt(ID_ARTICLE);
            
        }
        
        
    }

    @Override
    public void createArticles(List<Article> articles) throws Exception {
        
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ARTICLE)) {
            
            
            for (Article article : articles) {
                stmt.setString(TITLE, article.getTitle());
                stmt.setString(LINK, article.getLink());
                stmt.setString(DESCRIPTION, article.getDescription());
                stmt.setString(PICTURE_PATH, article.getPicturePath());
                stmt.setString(PUBLISHED_DATE, article.getPublishedDate()
                        .format(Article.DATE_FORMATTER)
                );
                stmt.registerOutParameter(ID_ARTICLE, Types.INTEGER);
                stmt.executeUpdate();
                
                ///Add database article id back to article so it's easier later
                int databaseArticleId = stmt.getInt(ID_ARTICLE);
                article.setId(databaseArticleId);
                
                /////
                
                //check if author exists
                var articleauthors = article.getAuthorsList();
                List<Integer> authorIdToInsert = new LinkedList<Integer>();
                Optional<Author> existingAuthor;
                //check if article has any authors
                if (!articleauthors.isEmpty()) {
                    for (Author author : articleauthors) {
                        
                        //check if author exists
                        var databaseAuthors = selectAuthors();//**
                        
                        if (!databaseAuthors.isEmpty()) {
                            existingAuthor = databaseAuthors.stream()
                                    .filter(x -> x.getName().equals(author.getName()))
                                    .findFirst();
                        }
                        else {
                            //don't crash other code
                            existingAuthor = Optional.empty();
                        }
                        
                        if (!existingAuthor.isPresent()) {
                            //author does not exists so insert
                            //add the returned author id to list for later M-N table relation insertion
                            authorIdToInsert.add(createAuthor(author));
                        }
                        else {
                            //author already exists, retrieve database ID
                            //add to author id list for later insertion
                            authorIdToInsert.add(existingAuthor.get().getId());
                        }
                    }
                    
                    authorIdToInsert.forEach(x -> {
                        try {
                            addArticleAuthor(article.getId(), x);
                        } catch (Exception ex) {
                            Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                
                //check if category exists
                var articlecategories = article.getCategoriesList();
                List<Integer> categoryIdToInsert = new LinkedList<Integer>();
                Optional<Category> existingCategory;
                //check if article has any category
                if (!articlecategories.isEmpty()) {
                    for (Category category : articlecategories) {
                        
                        //check if category exists
                        var databaseCategories = selectCategories();
                        
                        if (databaseCategories.isEmpty()) {
                            existingCategory = databaseCategories.stream()
                                    .filter(x -> x.getName().equals(category.getName()))
                                    .findFirst();
                        }
                        else {
                            //don't crash other code
                            existingCategory = Optional.empty();
                        }
                        
                        if (!existingCategory.isPresent()) {
                            //category does not exists so insert
                            //add the returned category id to list for later M-N table relation insertion
                            categoryIdToInsert.add(createCategory(category));
                        }
                        else {
                            //author already exists, retrieve database ID
                            //add to category id list for later insertion
                            categoryIdToInsert.add(existingCategory.get().getId());
                        }
                    }
                    
                    categoryIdToInsert.forEach(x -> {
                        try {
                            addArticleCategory(article.getId(), x);
                        } catch (Exception ex) {
                            Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    
                }
            }   
        }
    }

    @Override
    public void updateArticle(int id, Article article) throws Exception {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ARTICLE)) {
            
            
            stmt.setString(TITLE, article.getTitle());
            stmt.setString(LINK, article.getLink());
            stmt.setString(DESCRIPTION, article.getDescription());
            stmt.setString(PICTURE_PATH, article.getPicturePath());
            stmt.setString(PUBLISHED_DATE, article.getPublishedDate()
                    .format(Article.DATE_FORMATTER)
            );
            stmt.setInt(ID_ARTICLE, id);
            stmt.executeUpdate();
                        
        }

    }

    @Override
    public void deleteArticle(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ARTICLE)) {
            
            stmt.setInt(ID_ARTICLE, id);
            stmt.executeUpdate();
                        
        }    
    }

    @Override
    public Optional<Article> selectArticle(int id) throws Exception { 
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTICLE)) {
            
            stmt.setInt(ID_ARTICLE, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                //run if exists
                if (rs.next()) {
                    
                    Article articleToReturn = new Article(
                            rs.getInt(ID_ARTICLE),
                            rs.getString(TITLE),
                            rs.getString(LINK),
                            rs.getString(DESCRIPTION),
                            rs.getString(PICTURE_PATH),
                            LocalDateTime.parse(
                                    rs.getString(PUBLISHED_DATE),
                                    Article.DATE_FORMATTER
                            ));
                    
                    //find and add article authors
                    List<Author> articleauthors = selectArticleAuthors(id);
                    
                    //list already initialized in model, if no authors are returned it is an empty set
                    if (!articleauthors.isEmpty()) {
                        articleToReturn.addAuthorsToList(articleauthors.stream().collect(Collectors.toSet()));
                    }
                    
                    //find and add article categories
                    List<Category> articlecategories = selectArticleCategories(id);
                    
                    if (!articlecategories.isEmpty()) {
                        articleToReturn.addCategoriesToList(articlecategories.stream().collect(Collectors.toSet()));
                    }
                    
                    return Optional.of(articleToReturn);
                    
                }
            }
            
                        
        }   
        return Optional.empty();
    }

    @Override
    public List<Article> selectArticles() throws Exception {
        List<Article> articles = new ArrayList<>();
        //articles.add (new Article (1,"2","3","4","5",LocalDateTime.parse("2023-12-12T12:00:00",Article.DATE_FORMATTER)));
                                            
                                            
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTICLES)) {
                        
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    
                    Article articleToAdd = new Article(
                            rs.getInt(ID_ARTICLE),
                            rs.getString(TITLE),
                            rs.getString(LINK),
                            rs.getString(DESCRIPTION),
                            rs.getString(PICTURE_PATH),
                            LocalDateTime.parse(
                                    rs.getString(PUBLISHED_DATE),
                                    Article.DATE_FORMATTER
                            ));
                    
                    //get article authors
                    List<Author> databaseArticleAuthors = selectArticleAuthors(articleToAdd.getId());
                    
                    //add article authors
                    //convert List<Author> to Set<Author> (model uses set) via stream and collectors
                    if (!databaseArticleAuthors.isEmpty()) {
                        Set<Author> databaseSetArticleAuthors = databaseArticleAuthors.stream().collect(Collectors.toSet());
                        articleToAdd.addAuthorsToList(databaseSetArticleAuthors);    
                    }
                    
                    
                    //get article categories
                    List<Category> databaseArticleCategories = selectArticleCategories(articleToAdd.getId());
                    
                    //add article categories
                    if (!databaseArticleCategories.isEmpty()) {
                        Set<Category> databaseSetArticleCategories = databaseArticleCategories.stream().collect(Collectors.toSet());
                        articleToAdd.addCategoriesToList(databaseSetArticleCategories);    
                    }
                    
                    
                    //add article
                    articles.add(articleToAdd);
                }
            }
            
                        
        }   
        
        return articles;
        
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////// CATEGORY CRUD ////////////////////////////
    @Override
    public int createCategory(Category category) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_CATEGORY)) {

            stmt.setString(CATEGORY_NAME, category.getName());
            stmt.registerOutParameter(ID_CATEGORY, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(ID_CATEGORY);
        }
    }

    @Override
    public void createCategories(List<Category> categories) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_CATEGORY)) {

            for (Category category : categories) {
                stmt.setString(CATEGORY_NAME, category.getName());
                stmt.registerOutParameter(ID_CATEGORY, Types.INTEGER);
                stmt.executeUpdate();
            }
            
        }
    }

    @Override
    public void updateCategory(int id, Category category) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_CATEGORY)) {

            stmt.setString(CATEGORY_NAME, category.getName());
            stmt.setInt(ID_CATEGORY, id);
            stmt.executeUpdate();

        }
    }

    @Override
    public void deleteCategory(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_CATEGORY)) {

            stmt.setInt(ID_CATEGORY, id);
            stmt.executeUpdate();

        }
    }

    @Override
    public Optional<Category> selectCategory(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_CATEGORY)) {

            stmt.setInt(ID_CATEGORY, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(
                            new Category( // constructor order important
                                    rs.getInt(ID_CATEGORY),
                                    rs.getString(CATEGORY_NAME))
                    );
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public List<Category> selectCategories() throws Exception {
        List<Category> categories = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_CATEGORIES)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(
                            new Category(
                                    rs.getInt(ID_CATEGORY),
                                    rs.getString(CATEGORY_NAME)
                            )
                    );
                }
            }

        }

        return categories;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////// AUTHOR CRUD ////////////////////////////
    
    @Override
    public int createAuthor(Author author) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_AUTHOR)) {

            stmt.setString(AUTHOR_NAME, author.getName());
            stmt.registerOutParameter(ID_AUTHOR, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(ID_AUTHOR);
        }
    }

    @Override
    public void createAuthors(List<Author> authors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_AUTHOR)) {

            for (Author author : authors) {
                stmt.setString(AUTHOR_NAME, author.getName());
                stmt.registerOutParameter(ID_AUTHOR, Types.INTEGER);
                stmt.executeUpdate();
            }

        }
    }

    @Override
    public void updateAuthor(int id, Author author) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_AUTHOR)) {

            stmt.setString(AUTHOR_NAME, author.getName());
            stmt.setInt(ID_AUTHOR, id);
            stmt.executeUpdate();

        }
    }

    @Override
    public void deleteAuthor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_AUTHOR)) {

            stmt.setInt(ID_AUTHOR, id);
            stmt.executeUpdate();

        }
    }

    @Override
    public Optional<Author> selectAuthor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_AUTHOR)) {

            stmt.setInt(ID_AUTHOR, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(
                            new Author(
                                    rs.getInt(ID_AUTHOR),
                                    rs.getString(AUTHOR_NAME))
                    );
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public List<Author> selectAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_AUTHORS)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(
                            new Author(
                                    rs.getInt(ID_CREATORFIX),
                                    rs.getString(AUTHOR_NAME)
                            )
                    );
                }
            }

        }

        return authors;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////// Author and Category M-N ////////////////////////////
    
    @Override
    public int addArticleCategory(int articleId, int categoryId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_ARTICLE_CATEGORY)) {

            stmt.setInt(ID_ARTICLE, articleId);
            stmt.setInt(ID_CATEGORY, categoryId);
            stmt.registerOutParameter(ID_ARTICLE_CATEGORY, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(ID_ARTICLE_CATEGORY);
        }
    }

    @Override
    public void removeArticleCategory(int articleId, int categoryId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ARTICLE_CATEGORY)) {

            stmt.setInt(ID_ARTICLE, articleId);
            stmt.setInt(ID_CATEGORY, categoryId);
            stmt.executeUpdate();

        }
    }

    @Override
    public List<Category> selectArticleCategories(int articleId) throws Exception {
        List<Category> categories = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ARTICLE_CATEGORIES)) {
            
            stmt.setInt(ID_ARTICLE, articleId); //fixed
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(
                            new Category(
                                    rs.getInt(ID_CATEGORY),
                                    rs.getString(CATEGORY_NAME)
                            )
                    );
                }
            }

        }

        return categories;
    }

    @Override
    public int addArticleAuthor(int articleId, int authorId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_ARTICLE_AUTHOR)) {

            stmt.setInt(ID_ARTICLE, articleId);
            stmt.setInt(ID_AUTHOR, authorId);
            stmt.registerOutParameter(ID_ARTICLE_AUTHOR, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(ID_ARTICLE_AUTHOR);
        }
    }

    @Override
    public void removeArticleAuthor(int articleId, int authorId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ARTICLE_AUTHOR)) {

            stmt.setInt(ID_ARTICLE, articleId);
            stmt.setInt(ID_AUTHOR, authorId);
            stmt.executeUpdate();

        }
    }

    @Override
    public List<Author> selectArticleAuthors(int articleId) throws Exception {
        List<Author> authors = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ARTICLE_AUTHORS)) {
            
            stmt.setInt(ID_ARTICLE, articleId); //fixed
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(
                            new Author(
                                    rs.getInt(ID_AUTHOR),
                                    rs.getString(AUTHOR_NAME)
                            )
                    );
                }
            }

        }

        return authors;
    }

    @Override
    public void addArticleCategories(int articleId, List<Category> categories) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_CATEGORY)) {

            for (Category category : categories) {
                stmt.setInt(ID_CATEGORY, articleId);
                stmt.setInt(ID_ARTICLE, category.getId());

                stmt.executeUpdate();
            }
        }
        
         for (Category category : categories) {
             addArticleCategory(articleId, category.getId());
         }
        
        
        
    }

    @Override
    public void removeArticleCategories(int articleId, List<Category> categories) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addArticleAuthors(int articleId, List<Author> authors) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void removeArticleAuthors(int articleId, List<Author> authors) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ALL)) {
            
            stmt.executeUpdate();
            
        }
    }
    
    //////////////// ARTICLE COUNT FOR M-N ////////////////////////////

    @Override
    public int selectAuthorArticleCount(int authorId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_AUTHOR_ARTICLE_COUNT)) {

            stmt.setInt(ID_AUTHOR, authorId);
            stmt.registerOutParameter(AUTHOR_ARTICLE_COUNT, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(AUTHOR_ARTICLE_COUNT);
        }
    }

    @Override
    public int selectCategoryArticleCount(int categoryId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_CATEGORY_ARTICLE_COUNT)) {

            stmt.setInt(ID_CATEGORY, categoryId);
            stmt.registerOutParameter(CATEGORY_ARTICLE_COUNT, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(CATEGORY_ARTICLE_COUNT);
        }
    }

    
    //////////////// USER and ROLE ////////////////////////////
    
    @Override
    public int createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_USER)) {

            stmt.setString(USERNAME, user.getUsername());
            stmt.setString(PASSWORD, user.getPassword());
            stmt.setInt(ROLE_ID, user.getRole().getId());
            stmt.registerOutParameter(ID_USER, Types.INTEGER);
            stmt.executeUpdate();

            return stmt.getInt(ID_USER);
        }
    }

    @Override
    public void updateUser(int id, User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_USER)) {
            
            stmt.setInt(ID_USER, id);
            stmt.setString(USERNAME, user.getUsername());
            stmt.setString(PASSWORD, user.getPassword());
            stmt.setInt(ROLE_ID, user.getRole().getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_USER)) {

            stmt.setInt(ID_USER, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<User> selectUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_USER)) {

            stmt.setInt(ID_USER, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(
                            new User(
                                    rs.getString(USERNAME),
                                    rs.getString(PASSWORD),
                                    new Role(rs.getInt(ROLE_ID),rs.getString(ROLE_NAME))));
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public List<User> selectUsers() throws Exception {
        List<User> users = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_USERS)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(
                            new User(
                                    rs.getInt(ID_USER),
                                    rs.getString(USERNAME),
                                    rs.getString(PASSWORD),
                                    new Role(rs.getInt(ROLE_ID),rs.getString(ROLE_NAME))));
                }
            }

        }

        return users;
    }

    @Override
    public Optional<Role> selectRole(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ROLE)) {

            stmt.setInt(ID_ROLE, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(
                            new Role(
                                    rs.getInt(ID_ROLE),
                                    rs.getString(ROLE_NAME)));
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public List<Role> selectRoles() throws Exception {
        List<Role> roles = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ROLES)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(
                            new Role(
                                    rs.getInt(ID_ROLE),
                                    rs.getString(ROLE_NAME)));
                }
            }

        }

        return roles;
    }
    
}
