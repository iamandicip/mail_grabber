/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber.test;

import java.io.Console;
import java.util.List;
import mailgrabber.Constants;
import mailgrabber.parsers.DataParser;
import mailgrabber.parsers.FileParser;
import mailgrabber.parsers.UrlParser;
import mailgrabber.utils.FileUtils;

/**
 *
 * @author Ciprian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        DataParser p = null;
        String searchData = "";
        String searchType = "";
        String uri = "";
        String searchRegex = "";
        int maxDepth = -1;

        Console cons = System.console();

        if (cons != null) {

            cons.printf("\n****************************************************");
            cons.printf("\n*               Mail Grabber                       *");
            cons.printf("\n*                  v 0.3                           *");
            cons.printf("\n*                                                  *");
            cons.printf("\n*         ciprian.iamandi@gmail.com                *");
            cons.printf("\n*               +40724318195                       *");
            cons.printf("\n****************************************************");
            cons.printf("\n");
            cons.printf("\n1. Cautare adrese email");
            cons.printf("\n2. Cautare nr de telefon");

            do {
                searchData = cons.readLine("\nIntroduceti optiunea dorita : ");
            } while (!(searchData.equals("1") || searchData.equals("2")));

            searchRegex = searchData.equals("1") ? Constants.EMAIL_REG_EXP : Constants.PHONE_REG_EXP;

            cons.printf("----------------------------------------------------");
            cons.printf("\n1. Cautare intr-un folder");
            cons.printf("\n2. Cautare intr-un url");

            do {
                searchType = cons.readLine("\nIntroduceti optiunea dorita : ");
            } while (!(searchType.equals("1") || searchType.equals("2")));

            cons.printf("----------------------------------------------------");
            if (searchType.equals("1")) {
                do {
                    uri = cons.readLine("\nIntroduceti folderul : ");
                } while (uri.equals(""));

                p = new FileParser(searchRegex, uri);

            } else {

                do {
                    uri = cons.readLine("\nIntroduceti url-ul : ");
                } while (!uri.startsWith("http://"));

                cons.printf("----------------------------------------------------");

                do {
                    try {
                        maxDepth = Integer.parseInt(cons.readLine("\nIntroduceti numarul maxim de pagini prin care se va face cautarea [ < 20000 ] : "));
                    } catch (NumberFormatException nfe) {
                        cons.printf("Adancimea maxima trebuie sa fie un numar!");
                    }
                } while (maxDepth < 0 || maxDepth > 20000);

                p = new UrlParser(searchRegex, uri, maxDepth);
            }


        } else {
            if (args.length < 4) {
                System.out.println("Programul trebuie apelat cu 4 argumente : tipul de date cautat, tipul de cautare, adresa si adancimea maxima!");
                System.exit(0);
            } else {
                searchRegex = args[0].equals("1") ? Constants.EMAIL_REG_EXP : Constants.PHONE_REG_EXP;
                searchType = args[1];
                uri = args[2];
                try {
                    maxDepth = Integer.parseInt(args[3]);
                } catch (NumberFormatException nfe) {
                    System.out.println("Adancimea maxima trebuie sa fie un numar!");
                    System.exit(0);
                }

                if (searchType.equals("1")) {
                    p = new UrlParser(searchRegex, uri, maxDepth);

                } else if (searchType.equals("2")) {
                    p = new FileParser(searchRegex, uri);

                } else {
                    System.out.println("Tipul de cautare trebuie sa fie '1' sau '2'.");
                    System.exit(0);
                }
            }
        }

        if (p != null) {
            System.out.println("\nVa rugam asteptati...\n");
            p.parseAndSave();
        } else {
            System.out.println("\nNu am gasit niciun rezultat.");
        }

        System.out.println("\nProgramul s-a terminat.");

//        p = new UrlParser(Constants.EMAIL_REG_EXP,
//                        "http://roportal.bestjobs.ro/categorie/cercetare/55", 2000);
//        "http://www.anuntul.ro", 3000);
//        "http://www.locuri-de-munca.net/cereri-locuri-de-munca/cereri-locuri-de-munca-page-2.html", 4);
//                p = new FileParser(Constants.EMAIL_REG_EXP, "D:/cv");
//
//        p.parseAndSave();
//
//                String url = "http://www.bestjobs.ro/locuri-de-munca-site-general-manager-romania/153222/2";


    }
}
