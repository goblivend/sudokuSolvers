package fr.ivan.sudoku.util;

public class Utils {
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
}
