package org.tect.platform.jsan;

public final class JSONParseError extends RuntimeException {
    JSONParseError(Exception error) {
        super(error);
    }
}