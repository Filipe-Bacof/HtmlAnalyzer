import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class DeepestTagFinder {
    private List<String> htmlLines;
    private int maxDepth;
    private String deepestTag;
    private Deque<String> stack;

    public DeepestTagFinder(String html) {
        htmlLines = Arrays.asList(html.split("\n"));
        maxDepth = -1;
        deepestTag = "";
        stack = new ArrayDeque<>();
    }

    public String findDeepest() {
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

    private boolean isClosingTag(String tag) {
        return tag.trim().startsWith("</");
    }

    private boolean tagMatches(String openTag, String closeTag) {
        return openTag.substring(1).replaceAll("[^a-zA-Z0-9]+", "")
                .equals(closeTag.substring(2, closeTag.length() - 1).replaceAll("[^a-zA-Z0-9]+", ""));
    }

}
