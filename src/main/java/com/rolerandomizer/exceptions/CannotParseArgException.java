package com.rolerandomizer.exceptions;

public class CannotParseArgException extends Exception {
    public CannotParseArgException(String arg) {
        super(arg + " is not a valid player name or role");
    }
}
