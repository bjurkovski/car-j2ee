package org.banque.exceptions;

import java.io.Serializable;

/**
 * Class with a unique identifier so that the front end can show a localized 
 * message accordingly
 * @author wasser
 */
public class BanqueException extends Exception implements Serializable {

    /**
     * Possible Errors reported by the Exception
     */
    public static enum ErrorType {
        DATABASE_ERROR,
        ERROR_HASHING_PASSWORD,
        CLIENT_NOT_FOUND,
        ACCOUNT_NOT_FOUND,
        TRANSACTION_NOT_FOUND,
        CLIENT_NULL_NAME,
        CLIENT_NULL_LAST_NAME,
        CLIENT_NULL_PASSWORD,
        CLIENT_NULL_ADDRESS,
        CLIENT_NULL_DATE_OF_BIRTH,
        CLIENT_NULL_EMAIL,
        CLIENT_ID_ALREADY_EXISTS,
        INVALID_EMAIL,
        INVALID_SEARCH_CRITERIA,
        TRANSACTION_AMOUNT_INVALID,
        TRANSACTION_ACCOUNTS_EQUAL,
        UNKNOWN_ERROR
    }
    private ErrorType id;
    
    public BanqueException() {
        super();
        this.id = ErrorType.UNKNOWN_ERROR;
    }

    /**
     * Constructs an instance of <code>BanqueException</code> with the specified id
     * that identifies the error.
     * @param id the Error identifier according to the documentation.
     */
    public BanqueException(ErrorType id) {
        super();
        this.id = id;
    }

    public ErrorType getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return "Error ID: " + id;
    }
}
