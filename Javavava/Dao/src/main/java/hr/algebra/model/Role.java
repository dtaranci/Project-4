/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Derin
 */
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.FIELD)
public class Role implements Comparable<Role> {
    
    @XmlAttribute
    private int id;
    
    @XmlElement(name = "name")
    private String Name;

    public Role(String Name) {
        this.Name = Name;
    }
    
    //for JAXB
    public Role() {
    }
    
    public Role(int id, String Name) {
        this.id = id;
        this.Name = Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    
    public enum RoleType {
        USER(1),
        ADMIN(2);

        private final int value;

        RoleType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
        
        //provide int, convert to corresponding enum value
        public static RoleType fromValue(int value) {
            for (RoleType role : RoleType.values()) {
                if (role.getValue() == value) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        return this.id == other.id;
    }
    
    @Override
    public int compareTo(Role o) {
        return this.getName().compareTo(o.getName());
    }
}
