package com.hiccup.kidpainting.utilities;

/**
 * Created by ${binhpd} on 3/27/2016.
 */
public class Assert {
    public static void assertTrue(boolean value, String message) throws AssertionError {
        if (!value) {
            throw new AssertionError(message);
        }
    }
}
