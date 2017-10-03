/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.pojo;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

/**
 * .
 * 2017/06/01 add file and password hash
 */
public final class PasswordHash {

    /**
     * .
     */
    private PasswordHash() {

    }

    /**
     * .
     */
    public static final int BYTETOBIT = 8;

    /**
     * .
     */
    public static final int RADIXFORBYTE = 8;

    /**
     * .
     */
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * .
     */
    public static final int SALT_BYTES = 24;

    /**
     * .
     */
    public static final int HASH_BYTES = 24;

    /**
     * .
     */
    public static final int PBKDF2_ITERATIONS = 1000;

    /**
     * .
     */
    public static final int ITERATION_INDEX = 0;

    /**
     * .
     */
    public static final int SALT_INDEX = 1;

    /**
     * .
     */
    public static final int PBKDF2_INDEX = 2;

    /**
     * .
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static String createHash(final String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static String createHash(final char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check
     * @param goodHash the hash of the valid password
     * @return true if the password is correct, false if not
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static boolean validatePassword(final String password,
            final String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return validatePassword(password.toCharArray(), goodHash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check
     * @param goodHash the hash of the valid password
     * @return true if the password is correct, false if not
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static boolean validatePassword(final char[] password,
            final String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Decode the hash into its parameters
        String[] params = goodHash.split(":");
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] salt = fromHex(params[SALT_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(final byte[] a, final byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password the password to hash.
     * @param salt the salt
     * @param iterations the iteration count (slowness factor)
     * @param bytes the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    private static byte[] pbkdf2(final char[] password, final byte[] salt,
            final int iterations, final int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations,
                bytes * BYTETOBIT);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    private static byte[] fromHex(final String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(
                    hex.substring(2 * i, 2 * i + 2), RADIXFORBYTE);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private static String toHex(final byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(RADIXFORBYTE);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * .
     * Tests the basic functionality of the PasswordHash class
     *
     * @param args ignored
     */
    public static void main(final String[] args) {
        try {
            // Print out 10 hashes
            for (int i = 0; i < 10; i++) {
                System.out.println(createHash("p\r\nassw0Rd!"));
            }

            // Test password validation
            boolean failure = false;
            System.out.println("Running tests...");
            for (int i = 0; i < 100; i++) {
                String password = "" + i;
                String hash = createHash(password);
                String secondHash = createHash(password);
                if (hash.equals(secondHash)) {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    failure = true;
                }
                String wrongPassword = "" + (i + 1);
                if (validatePassword(wrongPassword, hash)) {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    failure = true;
                }
                if (!validatePassword(password, hash)) {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    failure = true;
                }
            }
            if (failure) {
                System.out.println("TESTS FAILED!");
            } else {
                System.out.println("TESTS PASSED!");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

}
