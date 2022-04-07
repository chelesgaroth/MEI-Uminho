package Exceptions;

public class NoRouteException extends Exception { 
    public NoRouteException(String dest) {
        super("No route available to " + dest + "!");
    }
}