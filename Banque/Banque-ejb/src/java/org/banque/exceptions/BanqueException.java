package org.banque.exceptions;

/**
 * Class with a unique identifier so that the front end can show a localized 
 * message accordingly
 * @author wasser
 */
public class BanqueException extends Exception {

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
        INVALID_EMAIL
    }
    private ErrorType id;

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
        return "Error ID: " + id + "\n Original Error message: " + super.getMessage();
    }
}
