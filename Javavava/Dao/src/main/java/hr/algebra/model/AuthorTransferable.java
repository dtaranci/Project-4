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
public class AuthorTransferable implements Transferable {
    
    //public so class can ask for it
    public static final DataFlavor AUTHOR_FLAVOR = new DataFlavor(Author.class, "Author");
    
    private static final DataFlavor[] SUPPORTED_FLAVORS = {AUTHOR_FLAVOR};
    private final Author author;


    public AuthorTransferable(Author author) {
        this.author = author;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        //foreach and find is better but only one is used here
        return AUTHOR_FLAVOR.equals(flavor); 
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return author;
        }
        throw new UnsupportedFlavorException(flavor);
    }

}
