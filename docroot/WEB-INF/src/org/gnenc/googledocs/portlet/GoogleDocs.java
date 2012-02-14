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

package org.gnenc.googledocs.portlet;

import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.DrawingEntry;
import com.google.gdata.data.docs.PresentationEntry;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
public class GoogleDocs extends MVCPortlet {
	public void createDoc(ActionRequest request, ActionResponse response)
			throws OAuthException, IOException, ServiceException {
		String type = ParamUtil.getString(request, "createType");
		String title = ParamUtil.getString(request, "createTitle");

		DocumentListEntry newEntry = null;
		if (type.equals("document")) {
			newEntry = new DocumentEntry();
		} else if (type.equals("presentation")) {
			newEntry = new PresentationEntry();
		} else if (type.equals("spreadsheet")) {
			newEntry = new SpreadsheetEntry();
		} else if (type.equals("drawing")) {
			newEntry = new DrawingEntry();
		} 
		newEntry.setTitle(new PlainTextConstruct(title));

		//String newEntryUrl = newEntry.getDocumentLink().getHref();
		ThemeDisplay themeDisplay = (ThemeDisplay) request
			.getAttribute(WebKeys.THEME_DISPLAY);
		String userEmail = themeDisplay.getUser().getEmailAddress();
		DocsService service = ActionUtil.getService(userEmail);
		URL feedUrl = ActionUtil.getFeedUrl(userEmail,true);
		//URL docListFeedUrl = new URL(scope + feedUrl);
		service.insert(feedUrl, newEntry);

		//response.setRenderParameter("newDocUrl", newEntryUrl);
		response.setRenderParameter("jspPage", viewJSP);
	}
	
	public void searchDocs(ActionRequest request, ActionResponse response) 
			throws IOException, ServiceException, OAuthException, ParseException {
		String searchString = ParamUtil.getString(request, "searchString");
		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		
		DocumentListFeed feed = (DocumentListFeed)ActionUtil.getDocumentListFeedFromTitleQuery(user,searchString);
		
		request.setAttribute("feed", feed);
		response.setRenderParameter("jspPage", searchJSP);
		
	}
	
	public void getContentJSP(ActionRequest request, ActionResponse response) {
		response.setRenderParameter("jspPage", contentJSP);
		
	}
	

	protected String contentJSP = "/display_content.jsp";
	protected String searchJSP = "/display_searchlist.jsp";
	protected String viewJSP = "/view.jsp";

	

}