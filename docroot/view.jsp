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
DocumentListFeed feed = GoogleDocs.getDocumentListFeed(renderRequest);

if (feed == null) {
%>
	<liferay-ui:error key="google-docs-authentication-failed" message="google-docs-authentication-failed" />
	<liferay-ui:error key="this-is-not-a-valid-google-account" message="this-is-not-a-valid-google-account" />
	<liferay-ui:error key="you-must-be-logged-in-to-view-google-docs" message="you-must-be-logged-in-to-view-google-docs" />
<%
} else {
%>
<div id="<portlet:namespace />googleDocsWrapper">
	<div class="toolbar" id="<portlet:namespace />toolbar">
		<div class="search-wrapper" id="<portlet:namespace />searchWrapper">
			<portlet:actionURL name="searchDocs" var="searchUrl" />
			<aui:form action="<%=searchUrl.toString() %>" method="post" name="gdSearch">
				<aui:layout>
					<aui:column>
						<aui:input cssClass="search-input" id="keywords" label="" name="keywords" type="text" />
					</aui:column>
					<aui:column>
						<aui:button cssClass="search-button" name="search" value="search" />
					</aui:column>
				</aui:layout>
			</aui:form>
		</div>
		<div class='table'>
			<portlet:actionURL name="createDoc" var="createDocUrl" />
			<aui:form action="<%=createDocUrl.toString() %>" method="post" name="gdCreate">
			<div class="table_row">
				<div class="table_cell">
					<aui:input cssClass="create-title" id="keywords" label="new-doc-title" name="createTitle" type="text" />
				</div>
				<div class="table_cell">
					<aui:select cssClass="create-type" label="type" name="createType">
						<aui:option value="document"><liferay-ui:message key="document" /></aui:option>
						<aui:option value="spreadsheet"><liferay-ui:message key="spreadsheet" /></aui:option>
						<aui:option value="presentation"><liferay-ui:message key="presentation" /></aui:option>
						<aui:option value="drawing"><liferay-ui:message key="drawing" /></aui:option>
					</aui:select>
				</div>
				<div class="table_cell align-bottom">
					<aui:button type="submit" value="Create" />
				</div>
			</div>
			</aui:form>
		</div>
	</div>

<%@include file="/view_docs.jsp" %>
<%
}
%>
</div>