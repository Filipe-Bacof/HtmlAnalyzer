public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HtmlAnalyzer <url>");
            return;
        }
        try {
            HtmlRequesterInterface requester = new HtmlRequester();
            String html = requester.request(args[0]);
            String deepestTag = findDeepestTag(html);
            System.out.println(deepestTag);
        } catch (ConnectionException e) {
            System.out.println("URL connection error");
        } catch (MalformedHtmlException e) {
            System.out.println("malformed HTML");
        }
    }

    private static String findDeepestTag(String html) {
        DeepestTagFinderInterface finder = new DeepestTagFinder();
        return finder.findDeepest(html);
    }
}