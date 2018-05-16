package org.tect.platform.jsan.token;

public class UnexpectedCharacter extends RuntimeException {
    public UnexpectedCharacter(int index, Character aChar) {
        super(String.format("Unexpected character '%c' at index %d", aChar, index));
    }
}
