/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber.parsers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mailgrabber.Constants;
import mailgrabber.utils.FileUtils;
import mailgrabber.utils.UrlUtils;

/**
 *
 * @author Ciprian
 */
public class UrlParser implements DataParser {

    private String regexp;
    private String url;
    private int maxLinks = 0;
    private List visitedLinks;
    private int intermediarySaveLimit;
    private static final String OUTPUT_FILE = "rezultate.txt";
    private int totalHits;

    public UrlParser(String regexp, String url, int maxLinks) {
        this.url = url;
        this.regexp = regexp;
        this.maxLinks = maxLinks;
        this.visitedLinks = new ArrayList();
        this.intermediarySaveLimit = 10;
        this.totalHits = 0;
    }

    public void parseAndSave() {
        List output = new ArrayList();

        Pattern patt = Pattern.compile(this.regexp);
        Pattern urlPatt = Pattern.compile(Constants.URL_REG_EXP);

        parseRecursive(this.url, patt, urlPatt, output);

        System.out.println("\nAm cautat prin " + visitedLinks.size() + " pagini web, si am gasit "
                + totalHits + " rezultate.");
    }

    private void parseRecursive(String url, Pattern patt, Pattern urlPatt, List output) {
        List<String> urls = new ArrayList();

        System.out.println(url);

        if (visitedLinks.size() >= maxLinks || visitedLinks.contains(url)) {
            return;
        } else {
            visitedLinks.add(url);
        }

        try {
            URL u = new URL(url);
            DataInputStream theHTML = new DataInputStream(u.openStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(theHTML));

            String line = br.readLine();
            while (line != null) {
                line = line.toLowerCase();
                Matcher dataM = patt.matcher(line);
                while (dataM.find()) {
                    String stuff = dataM.group();

                    if (!output.contains(stuff)) {
                        output.add(stuff);

                        //salvare intermediara a rezultatelor
                        if (output.size() >= intermediarySaveLimit) {
                            if (this.regexp.equals(Constants.PHONE_REG_EXP)) {
                                output = FileUtils.cleanUpPhones(output);
                            }
                            
                            FileUtils.writeListToFile(output, OUTPUT_FILE);
                            totalHits += output.size();
                            output.clear();
                        }
                    }
                }

                Matcher urlM = urlPatt.matcher(line);
                while (urlM.find()) {
                    String foundUrl = urlM.group(2).trim();

                    if (UrlUtils.validUrl(foundUrl)
                            && UrlUtils.urlsOnSameDomain(this.url, foundUrl)
                            && !urls.contains(foundUrl)
                            && !visitedLinks.contains(foundUrl)
                            && visitedLinks.size() < maxLinks) {

                        urls.add(foundUrl);

                    }
                }

                line = br.readLine();
            }
        } catch (MalformedURLException ex) {
//            Logger.getLogger(UrlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(UrlParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String u : urls) {
            if(visitedLinks.size() < maxLinks){
                parseRecursive(u, patt, urlPatt, output);
            }else{
                return;
            }
        }
    }


}
