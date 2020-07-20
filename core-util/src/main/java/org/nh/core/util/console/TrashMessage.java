package org.nh.core.util.console;

/**
 * Created by root on 3/12/18.
 */
public final class TrashMessage {

    static boolean isTrashMessage(String buf, String command){
        return isEmptyMessage(buf, command) || isOtherMessage(buf)
                || isDownloadOrUploadMessage(buf) || isSshMessage(buf);
    }

    private static boolean isEmptyMessage(String buf, String command){
        return "".equals(buf) || buf.length() == 0 || buf.contains(command);
    }

    private static boolean isOtherMessage(String buf){
        return buf.startsWith("pom.xml") || buf.startsWith("src/")
                || buf.startsWith("A ");
    }

    private static boolean isDownloadOrUploadMessage(String buf) {
        if(buf.startsWith("Download") || buf.startsWith("Upload") || buf.endsWith(".tar.gz") || buf.contains("%"))
            return true;
        for (int c = 0; c < buf.length(); c++) {
            char x = buf.charAt(c);
            if (Character.isDigit(x))
                continue;
            switch (x){
                case 'K':
                case 'B':
                case '/':
                case ' ':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private static boolean isSshMessage(String buf){
        return buf.contains("Last login:") || buf.contains("exit") || buf.contains("logout");
    }

}
