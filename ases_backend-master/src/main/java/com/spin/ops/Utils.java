package com.spin.ops;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.security.SecureRandom;

public class Utils {

    public static final int second = 1000;
    public static final int minute = 1000 * 60;
    public static final int hour = minute * 60;
    public static final int day = hour * 24;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Transactional
    public static void atualizaComplPessoaFisicaResidencial(EntityManager manager, String dsCampo, String dsValor, String dsCd_pessoa_fisica) {

        StringBuilder dsQuery = new StringBuilder();

        dsQuery.append("update tasy.compl_pessoa_fisica p ");
        dsQuery.append("set p." + dsCampo.trim() + " = '" + dsValor.trim().toLowerCase() + "' ");
        dsQuery.append("where p.cd_pessoa_fisica = :cd_pessoa_fisica ");
        dsQuery.append("and p.ie_tipo_complemento = 1");

        Query query = manager.createNativeQuery(dsQuery.toString());

        query.setParameter("cd_pessoa_fisica", dsCd_pessoa_fisica);

        query.executeUpdate();
    }

    public static String trimLowerCase(String value) {
        return value.trim().toLowerCase();
    }

    public static String removeAcentos(String string) {
        string = string.replaceAll("[ÂÀÁÄÃ]", "A");
        string = string.replaceAll("[âãàáä]", "a");
        string = string.replaceAll("[ÊÈÉË]", "E");
        string = string.replaceAll("[êèéë]", "e");
        string = string.replaceAll("ÎÍÌÏ", "I");
        string = string.replaceAll("îíìï", "i");
        string = string.replaceAll("[ÔÕÒÓÖ]", "O");
        string = string.replaceAll("[ôõòóö]", "o");
        string = string.replaceAll("[ÛÙÚÜ]", "U");
        string = string.replaceAll("[ûúùü]", "u");
        string = string.replaceAll("Ç", "C");
        string = string.replaceAll("ç", "c");
        string = string.replaceAll("[ýÿ]", "y");
        string = string.replaceAll("Ý", "Y");
        string = string.replaceAll("ñ", "n");
        string = string.replaceAll("Ñ", "N");
        return string;
    }

    public static String humanReadablePassword(int alphabetSize, int numberSize) {
        final String VOWELS = "aeiou";
        final String CONSONANTS = "bcdfghjklmnpqrstvwxyz";
        final String DIGITS = "0123456789";

        // sanity check
        if (alphabetSize < 1) throw new IllegalArgumentException();
        if (numberSize < 1) throw new IllegalArgumentException();

        // initialize
        char randomCharacter = 'a';
        int character = 0;

        int prefixSize = alphabetSize / 2;
        if (alphabetSize % 2 != 0) {
            prefixSize = ((int) (alphabetSize / 2)) + 1;
        }

        StringBuilder prefixPart = new StringBuilder(prefixSize);

        for (int i = 0; i < prefixSize - 1; i++) {
            if (i % 2 == 0) {
                // use consonants
                character = (int) (Math.random() * CONSONANTS.length());
                randomCharacter = CONSONANTS.charAt(character);
                prefixPart.append(randomCharacter);
            } else {
                // use vowels
                character = (int) (Math.random() * VOWELS.length());
                randomCharacter = VOWELS.charAt(character);
                prefixPart.append(randomCharacter);
            }
        }

        StringBuilder middlePart = new StringBuilder(numberSize);

        for (int i = 0; i < numberSize; i++) {
            // use digits
            character = (int) (Math.random() * DIGITS.length());
            randomCharacter = DIGITS.charAt(character);
            middlePart.append(randomCharacter);
        }


        int suffixSize = alphabetSize - prefixSize;
        StringBuilder suffixPart = new StringBuilder(suffixSize);

        for (int i = 0; i < suffixSize - 1; i++) {
            if (i % 2 == 0) {
                // use consonants
                character = (int) (Math.random() * CONSONANTS.length());
                randomCharacter = CONSONANTS.charAt(character);
                suffixPart.append(randomCharacter);
            } else {
                // use vowels
                character = (int) (Math.random() * VOWELS.length());
                randomCharacter = VOWELS.charAt(character);
                suffixPart.append(randomCharacter);
            }
        }

        prefixPart.append(middlePart).append(suffixPart);

        return prefixPart.toString();
    }

    public static String pathToStorage() {
        String path;
        String operationalSystem = System.getProperty("os.name");

        if (operationalSystem.toLowerCase().contains("windows")) {
            path = "storage\\";
        } else {
            path = "/home/srvboot/spinops/storage/";
        }

        return path;
    }

    public static String randomString( int size ){

         StringBuilder sb = new StringBuilder( size );

         for( int i = 0; i < size; i++ )
              sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

         return sb.toString();
     }
}
