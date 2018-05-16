package org.tect.platform.jsan;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class JSONProcessingError extends RuntimeException {
    JSONProcessingError(JsonProcessingException ex) {
        super(ex);
    }
}