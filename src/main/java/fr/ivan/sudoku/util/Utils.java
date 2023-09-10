package fr.ivan.sudoku.util;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    // alphabet semantic : al<linesize><blank char><range (num : has 1-...., otherwise a-...)>
    public static final String al4DotNum = ".1234";
    public static final String al9DotNum = ".123456789";
    public static final String al9Dot    = ".ABCDEFGHI";
    public static final String al16DotNum = ".123456789ABCDEFG";
    public static final String al16Dot    = ".ABCDEFGHIJKLMNOP";
    public static final String al25DotNum = ".123456789ABCDEFGHIJKLMNOP";
    public static final String al25Dot    = ".ABCDEFGHIJKLMNOPQRSTUVWXY";

    public static String getAlphabet(int size) {
        Map<Integer, String> alphabets = new HashMap<> ();
        alphabets.put(4, Utils.al4DotNum);
        alphabets.put(9, Utils.al9DotNum);
        alphabets.put(16, Utils.al16DotNum);
        alphabets.put(25, Utils.al25DotNum);
        return alphabets.get(size);
    }
    public static Integer HexToInt(char c) {
        if ('0' <= c && c <= '9')
            return c - '0';

        if ('a' <= c && c <= 'z')
            return 10 + c - 'a';
        if ('A' <= c && c <= 'Z')
            return 10 + c - 'A';


        return null;
    }

    public static char IntToChar(Integer i) {
        if (i == null)
            return '.';

        if (0 <= i && i <= 9)
            return (char)('0' + i);
        if (10 <= i && i <= 36)
            return (char)('A' + i - 10);


        return '.';
    }

    public static Integer CharToInt(String alphabet, char c) {
        if ( alphabet == null)
            throw new RuntimeException("Utils.CharToInt(): alphabet null");


        return c == alphabet.charAt(0) ? null : alphabet.indexOf(c);
    }

    public static char IntToChar(String alphabet, Integer i) {
        if ( alphabet == null)
            throw new RuntimeException("Utils.CharToInt(): alphabet null");
        if (i == null)
            return alphabet.charAt(0);

        return alphabet.charAt(i);
    }
}
