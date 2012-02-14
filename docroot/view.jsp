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

<portlet:actionURL var="contentJSP" name="getContentJSP" 
	windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

<script type="text/javascript">
AUI().use('io-plugin',function(A) {
	var contentNode = A.one("#<portlet:namespace/>googleDocsWrapper");
	//Currently, this does not work. Only gets portal language keys
	var loading = Liferay.Language.get('loading-google-docs');
	
	if (contentNode) {
		contentNode.plug(A.Plugin.IO, { 
			uri: '<%=contentJSP.toString()%>', 
			loadingMask: {
				strings: {loading: loading}
			}
		});
	}
});
</script>

<div id="<portlet:namespace />googleDocsWrapper" class="google-docs-wrapper">
<!-- Wrapper will hold all content retrieved through Alloy -->
</div>