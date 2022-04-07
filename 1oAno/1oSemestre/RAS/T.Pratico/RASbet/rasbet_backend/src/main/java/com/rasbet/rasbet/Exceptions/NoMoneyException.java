package com.rasbet.rasbet.Exceptions;

public class NoMoneyException extends Exception {
    public NoMoneyException() {
        super("Dinheiro insuficiente");
    }
}