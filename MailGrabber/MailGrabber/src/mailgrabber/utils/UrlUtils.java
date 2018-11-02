/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mailgrabber.Constants;

/**
 *
 * @author Ciprian
 */
public class UrlUtils {

    public static boolean validUrl(String url) {
        if (url.endsWith(".js")
                || url.endsWith(".css")
                || url.endsWith(".png")
                || url.endsWith(".jpg")
                || url.endsWith(".gif")
                || url.endsWith(".pdf")
                || url.endsWith(".doc")
                || url.endsWith(".xls")
                || url.endsWith(".ppt")) {
            return false;
        }
        return true;
    }

    public static boolean urlsOnSameDomain(String url1, String url2) {
        String domain1 = url1;
        String domain2 = url2;

        Pattern p = Pattern.compile(Constants.DOMAIN_REG_EXP);
        Matcher m = p.matcher(url1);
        if (m.find()) {
            domain1 = m.group();
        }

        m = p.matcher(url2);
        if (m.find()) {
            domain2 = m.group();
        }

        return domain1.equals(domain2);
    }

    public static String getUpperLevel(String url){
        if(url == null){
            return "";
        }

        int index = url.lastIndexOf('/', url.length() - 1);
        return url.substring(0, index);
    }
}
