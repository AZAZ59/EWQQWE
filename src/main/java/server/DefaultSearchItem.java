package server;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.HashMap;

public class DefaultSearchItem {
	public final HashMap<String,String> data ;
	public final float score;

	DefaultSearchItem(HashMap<String,String> data,final float score) {

		this.data=data;
		this.score = score;
	}
}

class DefaultAgregator extends Aggregator<DefaultSearchItem> {

	@Override
    DefaultSearchItem aggregate(final ScoreDoc sd) throws IOException, CorruptIndexException {
		final Document doc = this.indexSearcher.doc(sd.doc);

		HashMap<String,String> data= new HashMap<>();
		for (IndexableField name :doc.getFields()){
			data.put(name.name(),doc.get(name.name()));
		}
		return new DefaultSearchItem(data,sd.score);

		/*return new DefaultSearchItem(doc.get(MainConstants.TITLE_FIELD), doc.get(MainConstants.CONTENT_FIELD),
				doc.get(MainConstants.URI_FIELD), sd.score);*/
	}

}