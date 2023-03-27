import java.io.IOException;
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
            HtmlRequester requester = new HtmlRequester();
            String html = requester.request(args[0]);
            String deepestTag = findDeepestTag(html);
            System.out.println(deepestTag);
        } catch (IOException e) {
            System.out.println("URL connection error");
        } catch (MalformedHtmlException e) {
            System.out.println("malformed HTML");
        }
    }

    private static String findDeepestTag(String html) {
        DeepestTagFinder finder = new DeepestTagFinder(html);
        return finder.findDeepest();
    }
}