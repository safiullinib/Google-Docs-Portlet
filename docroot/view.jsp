<%
/**
 *  Copyright (c) 2010-2011 Educational Service Unit 10. 
*
*  This file is part of the My Courses portlet.
*  
*  My Courses portlet is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
* 
*  My Courses is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with the My Courses portlet.  If not, see <http://www.gnu.org/licenses/>.
**/
%>

<%@include file="/init.jsp" %>

<%
DocumentListFeed feed = GoogleDocs.getDocumentListFeed(renderRequest);

if (feed == null) {
%>
	<liferay-ui:error key="this-is-not-a-valid-google-account" message="this-is-not-a-valid-google-account" />
	<liferay-ui:error key="you-must-be-logged-in-to-view-google-docs" message="you-must-be-logged-in-to-view-google-docs" />
<%
} else {
%> 
<%@include file="/view_docs.jsp" %>
<%
}
%>