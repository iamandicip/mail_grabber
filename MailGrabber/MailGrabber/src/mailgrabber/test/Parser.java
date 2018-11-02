/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ciprian
 */
public class Parser {

    private String emailsFile = "emails.txt";
    private String phonesFile = "telefoane.txt";
    public static final String PHONE_REG_EXP = "(07)[0-9]{2}[\\.\\s]?[0-9]{3}[\\.\\s]?[0-9]{3}";
    public static final String EMAIL_REG_EXP = "([a-zA-Z0-9_\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+";

    public void parse(String folder) {
        List<String> emails = new ArrayList<String>();
        List<String> phones = new ArrayList<String>();

        File[] files = getFileList(folder);
        if (files == null) {
            return;
        }
        Pattern emailsP = Pattern.compile(EMAIL_REG_EXP);
        Pattern phonesP = Pattern.compile(PHONE_REG_EXP);

        for (File file : files) {
            if (file.isFile()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = br.readLine();
                    while(line != null){
                        Matcher emailsM = emailsP.matcher(line);
                        while (emailsM.find()) {
                            String newEmail = emailsM.group();
                            //nu adaugam duplicate
                            if(!emails.contains(newEmail)){
                                emails.add(newEmail);
                            }
                        }

                        Matcher phonesM = phonesP.matcher(line);
                        while (phonesM.find()) {
                            String newPhone = phonesM.group();
                            //nu adaugam duplicate
                            if(!phones.contains(newPhone)){
                                phones.add(phonesM.group());
                            }
                        }
                        line = br.readLine();
                    }
                    br.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        writeListToFile(emails, emailsFile, true);
        writeListToFile(cleanUpPhones(phones), phonesFile, true);
    }

    private List<String> cleanUpPhones(List<String> phones){
        List<String> newPhones = new ArrayList<String>();
        for(String s : phones){
            newPhones.add(s.replaceAll("\\.","").replaceAll("\\s", ""));
        }
        return newPhones;
    }

    private void writeListToFile(List<String> list, String filename, boolean withEnter) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (String s : list) {
                bw.write(s);
                bw.write("; ");
                if(withEnter){
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
            System.out.println("Am scris " + list.size() + " inregistrari in fisierul " + filename);
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File[] getFileList(String folder) {
        File parent = new File(folder);
        System.out.println("Caut in directorul : " + parent.getAbsolutePath());
        if (!parent.exists() || !parent.isDirectory()) {
            System.out.println("Directorul " + folder + " nu exista!");
            return null;
        }

        return parent.listFiles();
    }
}