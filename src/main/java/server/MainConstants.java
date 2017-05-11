package server;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 08.05.2017.
 */
public class MainConstants {
    public static final Path INDEX_PATH = Paths.get("C:\\luceneIndex");

    /*public static final String URI_FIELD = "uri";
    public static final String TITLE_FIELD = "title";
    public static final String CONTENT_FIELD = "content";

    public static final String RUS_TITLE_FIELD = "rustitle";
    public static final String RUS_CONTENT_FIELD = "ruscontent";

    public static final String ENG_TITLE_FIELD = "engtitle";
    public static final String ENG_CONTENT_FIELD = "engcontent";
*/
    public static Analyzer getAnalyzer() {
        final Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>();
/*        analyzers.put(RUS_TITLE_FIELD, new RussianAnalyzer());
        analyzers.put(RUS_CONTENT_FIELD, new RussianAnalyzer());
        analyzers.put(ENG_TITLE_FIELD, new EnglishAnalyzer());
        analyzers.put(ENG_CONTENT_FIELD, new EnglishAnalyzer());*/
        return new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzers);
    }
}
