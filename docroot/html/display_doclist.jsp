<%
/**
 *  Copyright (c) 2011-2012 Educational Service Unit 10.
 *
 *  This file is part of the Google Docs portlet.
 *
 *  Google Docs portlet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Google Docs portlet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with the Google Docs portlet.  If not, see <http://www.gnu.org/licenses/>.
 **/
%>

<%@include file="/init.jsp" %>

<%
DocumentListFeed feed = (DocumentListFeed)ActionUtil.getDocumentListFeed(renderRequest);
%>
<liferay-ui:error key="google-docs-authentication-failed" message="google-docs-authentication-failed" />
<liferay-ui:error key="this-is-not-a-valid-google-account" message="this-is-not-a-valid-google-account" />
<liferay-ui:error key="you-must-be-logged-in-to-view-google-docs" message="you-must-be-logged-in-to-view-google-docs" />
<%
if (Validator.isNotNull(feed)) {
%>
<%@include file="/html/toolbar.jsp" %>

<div class="doc-list-wrapper" id="<portlet:namespace />docListWrapper">
	<liferay-ui:search-container emptyResultsMessage="there-are-no-google-docs-to-display" delta="20">
    	<liferay-ui:search-container-results>
    	<%
    		List<DocumentListEntry> tempResults = ActionUtil.getDocumentList(feed);

    		results = ListUtil.subList(tempResults, searchContainer.getStart(), searchContainer.getEnd());
		    total = tempResults.size();

		    pageContext.setAttribute("results", results);
		    pageContext.setAttribute("total", total);
	    %>
    	</liferay-ui:search-container-results>

    	<liferay-ui:search-container-row
    		className="com.google.gdata.data.docs.DocumentListEntry"
    		keyProperty="resourceId"
    		modelVar="entry">

    		<liferay-ui:search-container-column-jsp
		        name="doc-type"
		        path="/html/column_doctype.jsp"
		        />

		    <liferay-ui:search-container-column-text
		        name="doc-title"
		        value="<%=entry.getTitle().getPlainText() %>"
		        href="<%=entry.getDocumentLink().getHref() %>"
		        target="_blank"
		        />

		    <liferay-ui:search-container-column-text
		        name="doc-updated"
		        value="<%=entry.getUpdated().toUiString() %>"
		        />

		    <liferay-ui:search-container-column-jsp
       			path="/html/column_docactions.jsp"
       			align="right"
        	/>

	      </liferay-ui:search-container-row>

	      <liferay-ui:search-iterator />

	</liferay-ui:search-container>
</div>
<%
}
%>