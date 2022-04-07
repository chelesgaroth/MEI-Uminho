package Exceptions;

public class BootstrapperException extends Exception { 
    public BootstrapperException() {
        super("Bootstrapper ended connection!");
    }
}