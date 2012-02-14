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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ListIterator" %>

<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>

<%@ page import="com.liferay.portlet.PortletURLUtil"%>

<%@ page import="com.google.gdata.data.docs.DocumentListEntry" %>
<%@ page import="com.google.gdata.data.docs.DocumentListFeed" %>

<%@ page import="org.gnenc.googledocs.portlet.GoogleDocs" %>
<%@ page import="org.gnenc.googledocs.portlet.ActionUtil" %>

<%@ page import="javax.portlet.PortletURL" %>

<portlet:defineObjects />