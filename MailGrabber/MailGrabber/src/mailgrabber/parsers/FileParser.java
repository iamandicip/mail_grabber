/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mailgrabber.Constants;
import mailgrabber.utils.FileUtils;

/**
 *
 * @author Ciprian
 */
public class FileParser implements DataParser {

    private String regexp;
    private String folder;
    private int filesNo;
    private static String OUTPUT_FILE = "rezultate.txt";

    public FileParser(String regexp, String folder) {
        this.folder = folder;
        this.regexp = regexp;
    }

    private File[] getFileList(File folder) {
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Directorul " + folder + " nu exista!");
            return new File[0];
        }

        return folder.listFiles();
    }

    public void parseAndSave() {
        List output = new ArrayList();

        File f = new File(this.folder);
        Pattern patt = Pattern.compile(this.regexp);

        parseRecursive(f, patt, output);

        if (this.regexp.equals(Constants.PHONE_REG_EXP)) {
            output = FileUtils.cleanUpPhones(output);
        }

        System.out.println("\nAm cautat prin " + filesNo + " fisiere si am gasit "
                + output.size() + " rezultate.");

        FileUtils.writeListToFile(output, OUTPUT_FILE);
    }

    private void parseRecursive(File f, Pattern patt, List output) {
        File[] files = getFileList(f);

        for (File file : files) {

            if (file.isFile()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = br.readLine();
                    while (line != null) {
                        Matcher dataM = patt.matcher(line);
                        while (dataM.find()) {
                            String stuff = dataM.group();
                            //nu adaugam duplicate
                            if (!output.contains(stuff)) {
                                output.add(stuff);
                            }
                        }

                        line = br.readLine();
                    }
                    br.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println(file.getAbsolutePath());

                filesNo++;
            } else {
                parseRecursive(file, patt, output);
            }
        }
    }
}
