package hu.pricecheck.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class FileLogger {

    private static final String PATH = "src\\main\\resources\\" ;

    private final String fileName ;

    public FileLogger(final String fileName) {
        this.fileName = PATH + fileName;
    }

    public void println(final String line) {
        try (PrintWriter log = new PrintWriter(new FileWriter(fileName))){
            log.println(line);
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
