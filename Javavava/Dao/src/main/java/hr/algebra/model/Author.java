/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

/**
 *
 * @author Derin
 */
public class Author extends Person implements Comparable<Author> {
    
    private int id;
    
    public Author(String name) {
        super(name);
    }
    
    public Author(int id,String name) {
        this(name);
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Author author = (Author) obj;
        return id == author.id;
    }

    @Override
    public int hashCode() {
        return super.getName() != null ? super.getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getId() + " - " + super.getName();
    }

    @Override
    public int compareTo(Author o) {
        return this.getName().compareTo(o.getName());
    }
    
    
    
    
}
