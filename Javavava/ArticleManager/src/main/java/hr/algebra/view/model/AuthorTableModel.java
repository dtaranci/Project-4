/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.view.model;

import hr.algebra.model.Author;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Derin
 */
public class AuthorTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {
        "Id", 
        "Name"
    };
    
    private List<Author> authors;

    public AuthorTableModel(List<Author> authors) {
        this.authors = authors;
    }

    public void setArticles(List<Author> authors) {
        this.authors = authors;
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return authors.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }
    
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return authors.get(rowIndex).getId();
            case 1:
                return authors.get(rowIndex).getName();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
}
