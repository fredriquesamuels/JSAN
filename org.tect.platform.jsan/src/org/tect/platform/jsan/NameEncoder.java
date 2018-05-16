package org.tect.platform.jsan;

import java.util.ArrayList;
import java.util.List;

public class NameEncoder {
    private NameEncoder() {
    }

    public static String encode(String name) {
        if(name==null) {
            return "";
        }
        return name.replace("\\" , "\\\\")
                .replace("[" , "\\[")
                .replace("]" , "\\]")
                .replace("{" , "\\{")
                .replace("}" , "\\}")
                .replace("." , "\\.");
    }

    public static String decode(String encode) {
        List<Integer> indicesToRemove = getEscapeIndicesToRemove(encode);

        StringBuilder sb = new StringBuilder(encode);
        indicesToRemove.forEach( i -> sb.deleteCharAt(i));
        return sb.toString();
    }

    private static List<Integer> getEscapeIndicesToRemove(String encode) {
        List<Integer> integers = new ArrayList<>();
        int length = encode.length();
        for(int index=length-1;index>=0;index--) {
            if(isEncodedChar(encode, index)) {
                integers.add(index-1);
                index--;
            }
        }
        return integers;
    }

    private static boolean isEncodedChar(String encode, int index) {
        boolean isEncoded = encode.charAt(index) == '{'
                || encode.charAt(index) == '}'
                || encode.charAt(index) == '['
                || encode.charAt(index) == ']'
                || encode.charAt(index) == '.'
                || encode.charAt(index) == '\\';

        if(isEncoded && ( index==0 || encode.charAt(index-1)!='\\')) {
            throw new EncodedCharacterNotPrecededByEscape(encode, index);
        }
        return isEncoded;
    }

    public static class EncodedCharacterNotPrecededByEscape extends RuntimeException {

        public EncodedCharacterNotPrecededByEscape(String encode, int index) {
            super(String.format("Encoded character not preceded by escape. index=%d char=%c full=%s", index, encode.charAt(index), encode));
        }
    }
}
