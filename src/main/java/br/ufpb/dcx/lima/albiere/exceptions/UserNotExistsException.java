package br.ufpb.dcx.lima.albiere.exceptions;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(String message) {
        super(message);
    }
}
