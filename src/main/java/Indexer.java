import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import server.MainConstants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Alexander on 08.05.2017.
 */
public class Indexer {

    private static IndexWriter indexWriter = null;

    public static void main(String[] args) throws IOException {

        new_index();

        pdfParse(new File("C:\\textData\\1.pdf"));
        add("C:\\textData\\1.txt");
        add("C:\\textData\\2.txt");
        add("C:\\textData\\3.txt");
        add("C:\\textData\\4.txt");

        if (indexWriter != null) {
            indexWriter.close();
            indexWriter = null;
        } else {
            throw new IOException("Index already closed");
        }

    }

    private static HashMap<String,String> extractTxt(final String name) {
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("key","text");//TODO write it

        /*String result = "";
        try {
            FileReader reader = new FileReader(name);
            int c;
            while ((c = reader.read()) != -1) {
                result += (char) c;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }*/
        return data;
    }

    private static void add(final String name) {

        final HashMap<String,String> text = extractTxt(name);
        final Document doc = new Document();

        final FieldType tokenType = new FieldType();
        tokenType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        tokenType.setStored(true);
        tokenType.setTokenized(true);
        tokenType.setStoreTermVectorOffsets(true);
        tokenType.setStoreTermVectorPayloads(true);
        tokenType.setStoreTermVectorPositions(true);
        tokenType.setStoreTermVectors(true);
        for (HashMap.Entry<String,String> item:text.entrySet()){
            doc.add(new Field(item.getKey(), item.getValue(), tokenType));
        }


        try {
            if (indexWriter != null) {
                indexWriter.addDocument(doc);
            }
        } catch (final IOException ex) {

        }
    }

    public static void new_index() throws IOException { // Инициализация индекса
        final Directory directory = FSDirectory.open(MainConstants.INDEX_PATH); // Путь, куда запишутся проиндексированные файлы
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(MainConstants.getAnalyzer());
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // OpenMode.CREATE - перезапись индекса заново, если он уже есть
        indexWriter = new IndexWriter(directory, indexWriterConfig);
    }

    public static void pdfParse(File file) throws IOException {
        PDDocument doc = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        FileWriter writer = new FileWriter("C:\\textData\\testParsing.txt", false);
        writer.write(text);
        writer.flush();
        doc.close();
    }

}
