package org.example.exceptions;

public class BrandNotFoundException extends RuntimeException {
  public BrandNotFoundException(String message) {
    super(message);
  }
}
