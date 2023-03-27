import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HtmlAnalyzer <url>");
            return;
        }

        try {
            String html = getHtml(args[0]);
            String deepestTag = findDeepestTag(html);
            System.out.println(deepestTag);
        } catch (IOException e) {
            System.out.println("URL connection error");
        } catch (MalformedHtmlException e) {
            System.out.println("malformed HTML");
        }
    }

    private static String getHtml(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }

    private static String findDeepestTag(String html) throws MalformedHtmlException {
        List<String> htmlLines = Arrays.asList(html.split("\n"));
        int maxDepth = -1;
        String deepestTag = "";
        Deque<String> stack = new ArrayDeque<>();
        for (String htmlLine : htmlLines) {
            if (htmlLine.trim().startsWith("<")) {
                if (isClosingTag(htmlLine)) {
                    if (stack.isEmpty()) {
                        throw new MalformedHtmlException();
                    }
                    String openTag = stack.pop();
                    if (!tagMatches(openTag, htmlLine)) {
                        throw new MalformedHtmlException();
                    }
                } else {
                    stack.push(htmlLine);
                }
            } else {
                String text = htmlLine;
                if (text != null && !stack.isEmpty()) {
                    int depth = stack.size();
                    if (depth > maxDepth) {

                        deepestTag = text;
                        maxDepth = depth;
                    }
                }
            }
        }
        if (!stack.isEmpty()) {
            throw new MalformedHtmlException();
        }
        if (maxDepth == -1) {
            throw new MalformedHtmlException();
        }
        return deepestTag.trim();
    }

    private static boolean isClosingTag(String tag) {
        return tag.trim().startsWith("</");
    }

    private static boolean tagMatches(String openTag, String closeTag) {
        return openTag.substring(1).replaceAll("[^a-zA-Z0-9]+", "")
                .equals(closeTag.substring(2, closeTag.length() - 1).replaceAll("[^a-zA-Z0-9]+", ""));
    }

    private static class MalformedHtmlException extends Exception {
    }
}