package br.ufpb.dcx.lima.albiere.exceptions;

public class IdExistsException extends RuntimeException {
    public IdExistsException(String message) {
        super(message);
    }
}
