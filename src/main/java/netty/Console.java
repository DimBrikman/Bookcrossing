package netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine() throws IOException {
        return reader.readLine();
    }

    public static void print(String s) {
        System.out.print(s);
        System.out.flush();
    }

    public static void println(String s) {
        System.out.println(s);
        System.out.flush();
    }

    public static void flush() {
        System.out.flush();
    }

    public static void close() {
        try {
            reader.close();
        } catch (IOException ignored) {
        }
    }
}
