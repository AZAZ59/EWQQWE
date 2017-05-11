<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/>

<jsp:directive.page import="server.TakeResult"/>
<jsp:directive.page import="server.SearchServlet"/>
<jsp:directive.page import="server.DefaultSearchItem"/>


<%!
    // Основные методы
    // Замена стандартных символов на HTML символы для адекватного вывода на страницу.
    public String escapeHTML(String s) {
        if (s == null) return null;
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("\"", "&quot;");
        s = s.replaceAll("'", "&apos;");
        return s;
    }

    public String ellipsize(String s, int max) {
        if (s.length() > max) {
            final String end = " ...";
            s = s.substring(0, max - end.length()).trim() + end;
        }
        return s;
    }
%>

<%
    // Константы
    final TakeResult<DefaultSearchItem> model = (TakeResult<DefaultSearchItem>) request.getAttribute("searchmodel");
    final String queryDefaultValue = escapeHTML(request.getParameter(SearchServlet.QUERY_INPUT));
    final int resultsPerPage = request.getParameter(SearchServlet.RESULTS_PER_PAGE) == null ? 5 : Integer.parseInt(request.getParameter(SearchServlet.RESULTS_PER_PAGE));
    final int currentPage = request.getParameter(SearchServlet.CURRENT_PAGE) == null ? 1 : Integer.parseInt(request.getParameter(SearchServlet.CURRENT_PAGE));
%>

<html>
<head>

    <title>Document Searcher</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <%--Стили--%>
    <link rel="stylesheet" type="text/css" href="stylesheets/main.css">
    <link rel="stylesheet" type="text/css" href="stylesheets/table.css">

    <%--Скрипты--%>

</head>

<body class="body">

<div id="header">
    <img id="logo" src="images/ssi_logo.jpg">
</div>

<form class="form" name="search" action="/search" accept-charset="UTF-8">

    <h1 id="title">Document Searcher</h1>
    <div class="container">
        <div class="query">

            <label>Parameters: </label>

            <textarea style="margin-top: 1vw;" class="queryField" name="query"></textarea>

            <label>Results Per Page: </label>

            <input name="resperpage" value="5" size="3">

            <input class="button" type="submit" value="Search">
        </div>

        <div class="query" style="display: block;">
            <label>Results: </label>
            <table style="margin-top: 1vw;">
                <tbody>
                <tr>
                    <th>Score</th>
                    <th>Name</th>
                    <th>First basic parameter</th>
                    <th>Second basic parameter</th>
                    <th>Country</th>
                    <th>Download specification</th>
                </tr>

                <% if (model != null) { %>
                <% if (model.getItems() != null) { %>
                <% for (DefaultSearchItem item : model.getItems()) { %>

                <tr>
                    <td><%= item.score %></td>
                    <td><%= escapeHTML(item.title) %></td>
                    <td><%= escapeHTML(ellipsize(item.content, 100))%></td>
                    <td>Something</td>
                    <td>Finland</td>
                    <td><a href="<%= item.uri %>"><%= item.uri %></a></td>
                </tr>

                <% } %>
                <% } else { %>
                <p>I'm sorry I couldn't find what you were looking for.</p>
                <% } %>
                <% } %>

                </tbody>
            </table>
        </div>
    </div>

</form>


<%--Страница <текущая> из <общего количества>--%>
<% if (model != null && model.getItems() != null) { %>
<p style="float:right;">
    Page <b><%= currentPage %></b>
    from <b><%= (int) Math.ceil(((float) model.totalHits) / resultsPerPage)%></b>
</p>
<% } %>

<%--Если нашли индекс и он содержит какие то записи--%>
<%--<% if (model != null && model.getItems() != null) { %>--%>

<%--<% if (currentPage > 1) { %>--%>

<%--<form name="search" action="/search" accept-charset="UTF-8" style="float:left;">--%>
<%--<% if (queryDefaultValue != null) { %>--%>
<%--<input type="hidden" name="<%= SearchServlet.QUERY_INPUT %>" value="<%= queryDefaultValue %>"/>--%>
<%--<% } %>--%>

<%--<input type="hidden" name="<%= SearchServlet.RESULTS_PER_PAGE %>" value="<%= resultsPerPage %>"/>--%>
<%--<input type="hidden" name="<%= SearchServlet.CURRENT_PAGE %>" value="<%= currentPage - 1 %>"/>--%>
<%--<input type="submit" value="<%= escapeHTML("<") %>"/>--%>

<%--</form>--%>
<%--<% } %>--%>

<%--<% if (model.totalHits > currentPage * resultsPerPage) { %>--%>
<%--<form name="search" action="/search" accept-charset="UTF-8">--%>

<%--<% if (queryDefaultValue != null) { %>--%>
<%--<input type="hidden" name="<%= SearchServlet.QUERY_INPUT %>" value="<%= queryDefaultValue %>"/>--%>
<%--<% } %>--%>

<%--<input type="hidden" name="<%= SearchServlet.RESULTS_PER_PAGE %>" value="<%= resultsPerPage %>"/>--%>
<%--<input type="hidden" name="<%= SearchServlet.CURRENT_PAGE %>" value="<%= currentPage + 1 %>"/>--%>
<%--<input type="submit" value="<%= escapeHTML(">") %>"/>--%>
<%--</form>--%>
<%--<% } %>--%>
<%--<% } %>--%>

</body>
</html>