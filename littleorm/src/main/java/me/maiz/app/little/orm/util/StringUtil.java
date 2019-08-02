package me.maiz.app.little.orm.util;

public class StringUtil {

    public static String camelCaseToUnderScore(String string) {
        StringBuilder sb = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (string.indexOf(c) == 0) {
                sb.append(Character.toLowerCase(c));
            } else if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
