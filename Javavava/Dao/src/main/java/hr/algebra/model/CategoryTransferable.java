/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Derin
 */
public class CategoryTransferable implements Transferable {
    
    //public so class can ask for it
    public static final DataFlavor CATEGORY_FLAVOR = new DataFlavor(Author.class, "Category");
    
    private static final DataFlavor[] SUPPORTED_FLAVORS = {CATEGORY_FLAVOR};
    private final Category category;


    public CategoryTransferable(Category category) {
        this.category = category;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        //foreach and find is better but only one is used here
        return CATEGORY_FLAVOR.equals(flavor); 
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return category;
        }
        throw new UnsupportedFlavorException(flavor);
    }

}
