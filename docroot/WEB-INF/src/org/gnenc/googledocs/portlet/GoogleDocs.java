/**
*  Copyright (c) 2010-2011 Educational Service Unit 10. 
*
*  This file is part of the Google Docs portlet.
*  
*  Google Docs portlet is free software: you can redistribute it and/or modify
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
* along with the Google Docs portlet.  If not, see <http://www.gnu.org/licenses/>.
**/

package org.gnenc.googledocs.portlet;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.liferay.util.portlet.PortletProps;

public class GoogleDocs extends MVCPortlet {
	public static DocumentListFeed getDocumentListFeed(
			RenderRequest request) throws IOException, 
			ServiceException, OAuthException, 
			ParseException 
	{
		DocumentListFeed feed = new DocumentListFeed();
		ThemeDisplay themeDisplay = 
				(ThemeDisplay) request.getAttribute(
						WebKeys.THEME_DISPLAY);
		String userEmail = themeDisplay.getUser().getEmailAddress();
		Boolean isDefaultUser = themeDisplay.getUser().isDefaultUser();
		
		if (!isDefaultUser) {
			String consumerKey = GetterUtil.getString(
				PortletProps.get(
					"google.docs.oauth.consumer.key"));
			String consumerSecret = GetterUtil.getString(
				PortletProps.get(
					"google.docs.oauth.consumer.secret"));
			String scope = "https://docs.google.com/feeds/";
			String feedUrl =  URLEncoder.encode(
				userEmail,"UTF-8") + "/private/full";
			String requestor = URLEncoder.encode(
				GetterUtil.getString(
					PortletProps.get(
						"google.docs.oauth.requestor")),"UTF-8");
			
			GoogleOAuthParameters oauthParams = new GoogleOAuthParameters();
			oauthParams.setOAuthConsumerKey(consumerKey);
			oauthParams.setOAuthConsumerSecret(consumerSecret);
			oauthParams.setScope(scope);
			
			OAuthSigner signer = new OAuthHmacSha1Signer();
			
			URL docListFeedUrl = new URL(
				scope + feedUrl	+ "?xoauth_requestor_id=" + requestor);
			System.out.println(docListFeedUrl);
			DocsService service = new DocsService("Document List Demo");
			service.setOAuthCredentials(oauthParams, signer);
		
			try {
				feed = service.getFeed(docListFeedUrl,DocumentListFeed.class);
			} catch (ResourceNotFoundException e) {
				feed = null;
				SessionErrors.add(request, "this-is-not-a-valid-google-account");
			}
		} else {
			feed = null;
			SessionErrors.add(request, "you-must-be-logged-in-to-view-google-docs");
		}
		return feed;
		
	}
	
	public static List<DocumentListEntry> getDocumentList(DocumentListFeed feed) {
		List<DocumentListEntry> docList = new ArrayList<DocumentListEntry>();
		
		for (DocumentListEntry entry : feed.getEntries()) {
			docList.add(entry);
			
		}
		return docList;
		
	}

}
