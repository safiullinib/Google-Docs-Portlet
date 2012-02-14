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
	
<%@include file="/html/toolbar.jsp" %>
<%@include file="/html/display_doclist.jsp" %>