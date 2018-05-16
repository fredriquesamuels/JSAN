package org.tect.platform.jsan;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class JsonProcessingError extends RuntimeException {
    JsonProcessingError(JsonProcessingException ex) {
        super(ex);
    }
}