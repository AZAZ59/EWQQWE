package server;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

abstract class Aggregator<T> {
	protected Query query;
	protected IndexSearcher indexSearcher;

	abstract T aggregate(ScoreDoc sd) throws IOException, CorruptIndexException;
}

class LuceneSearcher<T, A extends Aggregator<T>> implements TakeResult.ITakeble<T> {

	private final String story;
	private final Class<A> classA;

	/* IndexReader is thread safe - one instance for all requests */

	private static volatile IndexReader indexReader;

	LuceneSearcher(final Class<A> classA, final Path indexPath, final String story) throws IOException {

		this.story = story;
		this.classA = classA;

		if (LuceneSearcher.indexReader == null) {
			synchronized (LuceneSearcher.class) {
				if (LuceneSearcher.indexReader == null) {
					final Directory dir = FSDirectory.open(indexPath);
					LuceneSearcher.indexReader = DirectoryReader.open(dir);
				}
			}
		}
	}

	@Override
	public TakeResult<T> Take(final int count, final int start)
			throws ParseException, IOException, InstantiationException, IllegalAccessException {

		final int nDocs = start + count;

		final Query query = QueryHelper.generate(this.story);
		final IndexSearcher indexSearcher = new IndexSearcher(LuceneSearcher.indexReader);
		final TopScoreDocCollector collector = TopScoreDocCollector.create(Math.max(nDocs, 1));
		indexSearcher.search(query, collector);
		final TopDocs topDocs = collector.topDocs();

		if (nDocs <= 0) {
			return new TakeResult<T>(topDocs.totalHits, null);
		}

		final ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		final int length = scoreDocs.length - start;

		if (length <= 0) {
			return new TakeResult<T>(topDocs.totalHits, null);
		}

		final List<T> items = new ArrayList<T>(length);
		final A aggregator = this.classA.newInstance();
		aggregator.query = query;
		aggregator.indexSearcher = indexSearcher;

		for (int i = start; i < scoreDocs.length; i++) {
			items.add(i - start, aggregator.aggregate(scoreDocs[i]));
		}

		return new TakeResult<T>(topDocs.totalHits, items);
	}
}