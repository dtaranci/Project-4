/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lecturerc10
 */
public class Article implements Comparable<Article> {
    
    public static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private int id;
    private String title;
    private String link;
    private Set<Author> authors = new HashSet<Author>();
    private Set<Category> categories = new HashSet<Category>();
    private String description;
    private String picturePath;
    private LocalDateTime publishedDate;

    public Article() {
    }

    public Article(int id, String title, String link, String description, String picturePath, LocalDateTime publishedDate) {
        this(title, link, description, picturePath, publishedDate);
        setId(id);
    }

    public Article(String title, String link, String description, String picturePath, LocalDateTime publishedDate) {
        setTitle(title);
        setDescription(description);
        setLink(link);
        setPicturePath(picturePath);
        setPublishedDate(publishedDate);
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public Set<Author> getAuthorsList() {
        return new HashSet<>(authors);
    }

    public void addAuthorsToList(Set<Author> authorsSet) {
        if (authorsSet != null) {
            authors.addAll(authorsSet);    
        }
    }
    
    public void addAuthorToList(Author author) {
        if (author != null) {
            authors.add(author);
        }
    }
    
    public Set<Category> getCategoriesList() {
        return new HashSet<>(categories);
    }

    public void addCategoriesToList(Set<Category> categoriesSet) {
        if (categoriesSet != null) {
            categories.addAll(categoriesSet);
        }
    }

    public void addCategoryToList(Category category) {
        if (category != null) {
            categories.add(category);
        }
    }
    
    @Override
    public String toString() {
        return id + " - " + title;
    }

    @Override
    public int compareTo(Article o) {
        return this.getTitle().compareTo(o.getTitle());
    }
    
    
    
    
    
}
