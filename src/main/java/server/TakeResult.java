package server;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public final class TakeResult<T> {
	public final int totalHits;
	private final Collection<T> items;

	protected TakeResult(final int totalHits, final Collection<T> items) {
		this.totalHits = totalHits;
		this.items = items;
	}

	public Collection<T> getItems() {
		if (this.items != null) {
			return Collections.unmodifiableCollection(this.items);
		}
		return null;
	}

	interface ITakeble<T> {
		TakeResult<T> Take(int count, int start)
				throws ParseException, IOException, InstantiationException, IllegalAccessException;
	}
}