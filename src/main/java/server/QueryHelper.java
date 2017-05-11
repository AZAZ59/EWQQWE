package server;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.Query;

final class QueryHelper {
	static Query generate(final String story) throws ParseException {
		final QueryParser parser = new MultiFieldQueryParser(
				new String[] { 
						/*MainConstants.TITLE_FIELD,
						MainConstants.CONTENT_FIELD,
						/* Russian */
						/*MainConstants.RUS_TITLE_FIELD,
						MainConstants.RUS_CONTENT_FIELD,
						/* English */
						/*MainConstants.ENG_TITLE_FIELD,
						MainConstants.ENG_CONTENT_FIELD */},
				MainConstants.getAnalyzer());
		/* Operator OR is used by default */

		parser.setDefaultOperator(QueryParser.Operator.AND);

		return parser.parse(QueryParserBase.escape(story));
	}
}