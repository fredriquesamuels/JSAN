package org.tect.platform.jsan;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class NameEncoderTest {

    @Test
    public void encode() {
        //when
        String encode = NameEncoder.encode("[]{}.");

        //then
        Assert.assertEquals("\\[\\]\\{\\}\\.", encode);
    }

    @Test
    public void decode() {
        //when
        String decode = NameEncoder.decode(NameEncoder.encode("[]{}."));

        //then
        Assert.assertEquals("[]{}.", decode);
    }

    @Test
    public void decode_error() {
        try {
            NameEncoder.decode("[");
            fail();
        } catch (NameEncoder.EncodedCharacterNotPrecededByEscape e) {

        } catch (Exception e) {
            fail();
        }
    }
}