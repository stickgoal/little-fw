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


    public static String underScoreToCamelCase(String src) {
        if(src.startsWith("_")){
            src=src.substring(1,src.length());
        }
        if(src.endsWith("_")){
            src=src.substring(0,src.length()-1);
        }
        final String[] splited = src.split("_");
        StringBuilder strBuilder=new StringBuilder();

        for (int i = 0; i < splited.length; i++) {
            final String current = splited[i];
            if(i!=0){
                strBuilder.append(""+Character.toUpperCase(current.charAt(0)));
                strBuilder.append(current, 1, current.length());
            }else{
                strBuilder.append(current);
            }
        }
        return strBuilder.toString();
    }
}
