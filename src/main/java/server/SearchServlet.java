package server;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

	public final static String QUERY_INPUT = "query";
	public final static String RESULTS_PER_PAGE = "resperpage";
	public final static String CURRENT_PAGE = "currentpage";

	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse res)
			throws ServletException, IOException {

		final String query = req.getParameter(SearchServlet.QUERY_INPUT);
		final String itemsPerPage = req.getParameter(SearchServlet.RESULTS_PER_PAGE);
		final String currentPage = req.getParameter(SearchServlet.CURRENT_PAGE);

		if (query != null && !query.isEmpty()) {
			int currentPageInt = 1, itemsPerPageInt = 10;

			try {
				currentPageInt = Integer.parseInt(currentPage);
			} catch (final NumberFormatException e) {
			}
			try {
				itemsPerPageInt = Integer.parseInt(itemsPerPage);
			} catch (final NumberFormatException e) {
			}

			try {
				req.setAttribute("searchmodel",
						new LuceneSearcher<DefaultSearchItem, DefaultAgregator>
								(DefaultAgregator.class,
								MainConstants.INDEX_PATH, query.trim()
								).
								Take(itemsPerPageInt,
										(currentPageInt - 1) * itemsPerPageInt));
			} catch (final Exception e) {
				throw new ServletException(e);
			}
		}

		this.getServletContext().getRequestDispatcher("/index.jsp").forward(req, res);
	}
}