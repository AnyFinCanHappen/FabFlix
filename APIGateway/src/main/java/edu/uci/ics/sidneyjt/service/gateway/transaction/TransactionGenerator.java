package edu.uci.ics.sidneyjt.service.gateway.transaction;

import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;

public class TransactionGenerator {
    private static final int ID_SIZE = 64;

    public static String generate() {
        SecureRandom rngesus = new SecureRandom();
        byte[] id = new byte[ID_SIZE];
        rngesus.nextBytes(id);
        String idString = Hex.encodeHexString(id);
        return idString;
    }
}