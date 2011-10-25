package no.sws.client;

/**
 * Created by User: orby
 * Date: 25.10.11
 * Time: 08.12
 */
public class SwsRequiredBatchIdException extends Exception {
    public SwsRequiredBatchIdException() {
        super("Parameter batchId can't be null or an empty String when issuing invoices. This to prevent a double submit.");
    }
}
