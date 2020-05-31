package bot.hostkeeper.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MetaTagInspector {
    public boolean checkMetaTag(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Elements listHead = doc.select("meta[name~=hostkeeper-?\\d+]");

            for (Element element : listHead) {
                if (element.toString().matches("<meta name=\"hostkeeper-?\\d+\">"))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
