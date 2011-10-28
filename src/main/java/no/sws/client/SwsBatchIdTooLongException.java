package no.sws.client;

/**
 * Created by user: orby
 * Date: 28.10.11
 * Time: 08.51
 */
public class SwsBatchIdTooLongException extends Exception {

    public SwsBatchIdTooLongException() {
        super("Attribute batchId can't be longer than 256 characters");
    }
}

