package ru.club.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        // abc123.abc123.abc
        String regex = "[a-z]+(.?[-a-z0-9_]+)*@[a-z]+[a-z0-9]*(.[a-z]+)+";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher("Ayoa");

        String[] hexs = {
                "cross-@mail.ru",
                "jamie.randall@outlook.com.ua",
                "G.G.G",
                ".gf.fdsg2",
                "f5T..gfdg",
                "5GL..G.G"
        };

        for (String hex : hexs) {
            System.out.print(hex + " : ");
            System.out.println(pattern.matcher(hex).matches());
        }
    }
}
