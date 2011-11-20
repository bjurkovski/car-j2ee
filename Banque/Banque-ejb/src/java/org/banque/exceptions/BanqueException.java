package org.banque.exceptions;

/**
 * Class with a unique identifier so that the front end can show a localized 
 * message accordingly
 * Read Doc for error listings.
 * @author wasser
 */
public class BanqueException extends Exception {

    private int id;

    /**
     * Constructs an instance of <code>BanqueException</code> with the specified id
     * that identifies the error.
     * @param id the Error identifier according to the documentation.
     */
    public BanqueException(int id) {
        super();
        this.id = id;
    }
}
