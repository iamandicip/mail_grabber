/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ciprian
 */
public class FileUtils {

    public static void writeListToFile(List<String> list, String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            for (String s : list) {
                bw.write(s);
                bw.write("; ");
                bw.newLine();
            }
            bw.flush();
            bw.close();
            System.out.println("\nAm scris " + list.size() + " inregistrari in fisierul " + filename);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> cleanUpPhones(List<String> phones) {
        List<String> newPhones = new ArrayList<String>();
        for (String s : phones) {
            newPhones.add(s.replaceAll("\\.", "").replaceAll("\\s", ""));
        }
        return newPhones;
    }
}
