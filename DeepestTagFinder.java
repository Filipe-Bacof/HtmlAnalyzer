import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class DeepestTagFinder implements DeepestTagFinderInterface {
    private int maxDepth;
    private String deepestTag;
    private Deque<String> stack;

    public DeepestTagFinder() {
        maxDepth = -1;
        deepestTag = "";
        stack = new ArrayDeque<>();
    }

    public String findDeepest(String html) {
        List<String> htmlLines = Arrays.asList(html.split("\n"));
        for (String htmlLine : htmlLines) {
            analizeLine(htmlLine);
        }
        return analyzeResults();
    }

    private void analizeLine(String htmlLine) {
        if (isATag(htmlLine)) {
            analyzeTag(htmlLine);
        } else {
            analyzeSnippetOfText(htmlLine);
        }
    }

    private boolean isATag(String htmlLine) {
        return htmlLine.trim().startsWith("<");
    }

    private void analyzeTag(String htmlLine) {
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
    }

    private void analyzeSnippetOfText(String htmlLine) {
        String text = htmlLine;
        if (text != null && !stack.isEmpty()) {
            int depth = stack.size();
            if (depth > maxDepth) {
                deepestTag = text;
                maxDepth = depth;
            }
        }
    }

    private boolean isClosingTag(String tag) {
        return tag.trim().startsWith("</");
    }

    private boolean tagMatches(String openTag, String closeTag) {
        return openTag.substring(1).replaceAll("[^a-zA-Z0-9]+", "")
                .equals(closeTag.substring(2, closeTag.length() - 1).replaceAll("[^a-zA-Z0-9]+", ""));
    }

    private String analyzeResults() {
        if (!stack.isEmpty()) {
            throw new MalformedHtmlException();
        }
        if (maxDepth == -1) {
            throw new MalformedHtmlException();
        }
        return deepestTag.trim();
    }
}
