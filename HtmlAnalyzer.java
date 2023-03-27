import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HtmlAnalyzer <url>");
            return;
        }

        try {
            String html = getHtml(args[0]);
            System.out.println(html);
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
            sb.append(line.trim());
        }
        reader.close();
        return sb.toString();
    }

    private static String findDeepestTag(String html) throws MalformedHtmlException {
        int maxDepth = -1;
        String deepestTag = "";
        Deque<String> stack = new ArrayDeque<>();
        Matcher matcher = Pattern.compile("<([a-zA-Z]+)>.*?</\\1>|<([a-zA-Z]+)>[^<]*|[^<>]+").matcher(html);
        String currentTag = "";
        while (matcher.find()) {
            String match = matcher.group();
            if (match.startsWith("<")) {
                if (isClosingTag(match)) {
                    if (stack.isEmpty()) {
                        throw new MalformedHtmlException();
                    }
                    Deque<String> stackCopy = new ArrayDeque<>(stack);
                    String openTag = stackCopy.pop();
                    if (!tagMatches(openTag, match)) {
                        throw new MalformedHtmlException();
                    }
                    currentTag = openTag;
                } else {
                    stack.push(match);
                    int depth = stack.size();
                    if (depth > maxDepth) {
                        deepestTag = match;
                        maxDepth = depth;
                    }
                    currentTag = match;
                }
            } else {
                String text = extractText(match);
                if (text != null && !stack.isEmpty()) {
                    int depth = stack.size();
                    if (depth > maxDepth) {
                        deepestTag = currentTag;
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
        return deepestTag;
    }

    private static boolean isClosingTag(String tag) {
        return tag.startsWith("</");
    }

    private static boolean tagMatches(String openTag, String closeTag) {
        System.out.println(openTag);
        System.out.println(closeTag);
        return openTag.substring(1).replaceAll("[^a-zA-Z0-9]+", "")
                .equals(closeTag.substring(2, closeTag.length() - 1).replaceAll("[^a-zA-Z0-9]+", ""));
    }

    private static String extractText(String line) {
        String text = line.trim();
        if (text.isEmpty()) {
            return null;
        }
        return text;
    }

    private static class MalformedHtmlException extends Exception {
    }
}