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

<% DocumentListFeed docListFeed = GoogleDocs.getDocumentListFeed(renderRequest); %>

<div id="<portlet:namespace />googleDocsWrapper">
	<div class="toolbar" id="<portlet:namespace />toolbar">
		<div class="search-wrapper" id="<portlet:namespace />searchWrapper">
			<aui:form action="" method="get" name="gdSearch" >
		
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
		<div class="button-wrapper" id="<portlet:namespace />buttonWrapper">
			
		<span class="add-button" id="<portlet:namespace />addButtonContainer">
			<liferay-ui:icon-menu align="left" icon='<%=renderRequest.getContextPath() + "/image/add.png"%>' direction="down" message="create" showExpanded="<%= false %>" showWhenSingleIcon="<%= false %>">
				<liferay-ui:icon src='<%=renderRequest.getContextPath() + "/image/document.png"%>'
					message="document" url="https://docs.google.com/DocAction?action=newdoc" target="_blank" />
				<liferay-ui:icon src='<%=renderRequest.getContextPath() + "/image/spreadsheet.png"%>' 
					message="spreadsheet" url="https://spreadsheets.google.com/ccc?new=true" target="_blank" />
				<liferay-ui:icon src='<%=renderRequest.getContextPath() + "/image/presentation.png"%>' 
					message="presentation" url="https://docs.google.com/?action=new_presentation" target="_blank" />
			</liferay-ui:icon-menu>
		</span>
		
		
		</div>
	</div>
	
	<div class="doc-list-wrapper" id="<portlet:namespace />docListWrapper" >
		<liferay-ui:search-container emptyResultsMessage="there-are-no-google-docs-to-display" delta="20">
	    	<liferay-ui:search-container-results>
	    	<%
	    		List<DocumentListEntry> tempResults = GoogleDocs.getDocumentList(docListFeed);
	    	
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
			        path="/doc_type_column.jsp"
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
        			path="/doc_actions.jsp"
        			align="right"
	        	/>
	
		      </liferay-ui:search-container-row>
	
		      <liferay-ui:search-iterator />
	
		</liferay-ui:search-container>
	</div>
</div>