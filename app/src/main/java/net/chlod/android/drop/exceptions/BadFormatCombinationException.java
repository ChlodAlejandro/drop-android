package net.chlod.android.drop.exceptions;

public class BadFormatCombinationException extends Exception {

    public BadFormatCombinationException(String message) {
        super(message);
    }

    public BadFormatCombinationException(String message, Throwable e) {
        super(message, e);
    }

    public BadFormatCombinationException(Throwable e) {
        super(e);
    }

}
