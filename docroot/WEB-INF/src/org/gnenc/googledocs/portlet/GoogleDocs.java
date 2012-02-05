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

import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.DrawingEntry;
import com.google.gdata.data.docs.PresentationEntry;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.liferay.util.portlet.PortletProps;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
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
		DocsService service = getService(userEmail);
		URL feedUrl = getFeedUrl(userEmail);
		//URL docListFeedUrl = new URL(scope + feedUrl);
		service.insert(feedUrl, newEntry);

		//response.setRenderParameter("newDocUrl", newEntryUrl);
		
	}

	public static List<DocumentListEntry> getDocumentList(
			DocumentListFeed feed) {
		List<DocumentListEntry> docList = new ArrayList<DocumentListEntry>();

		for (DocumentListEntry entry : feed.getEntries()) {
			docList.add(entry);

		}
		return docList;

	}

	public static DocumentListFeed getDocumentListFeed(RenderRequest request)
			throws IOException, ServiceException, OAuthException,
			ParseException {
		DocumentListFeed feed = new DocumentListFeed();
		User user = getUser(request);
		String userEmail = user.getEmailAddress();
		URL feedUrl = getFeedUrl(userEmail);
		Boolean isDefaultUser = user.isDefaultUser();

		if (!isDefaultUser) {
			DocsService service = getService(userEmail);
			try {
				feed = service.getFeed(feedUrl, DocumentListFeed.class);
				
			} catch (ResourceNotFoundException e) {
				feed = null;
				SessionErrors
					.add(request, "this-is-not-a-valid-google-account");
				
			} catch (AuthenticationException e) {
				feed = null;
				SessionErrors
					.add(request, "google-docs-authentication-failed");
				
			}
		} else {
			feed = null;
			SessionErrors.add(request,
					"you-must-be-logged-in-to-view-google-docs");
			
		}
		return feed;

	}

	public static URL getFeedUrl(String userEmail)
			throws UnsupportedEncodingException, MalformedURLException {
		String scope = getScope();
		String feedUrl = URLEncoder.encode(userEmail, "UTF-8")
				+ "/private/full";
		String requestor = URLEncoder.encode(
				GetterUtil.getString(PortletProps
					.get("google.docs.oauth.requestor")), "UTF-8");
			URL docListFeedUrl = new URL(scope + feedUrl
					+ "?xoauth_requestor_id=" + requestor);
		return docListFeedUrl;

	}

	public static DocsService getService(String userEmail)
			throws UnsupportedEncodingException, MalformedURLException,
				OAuthException {
		String consumerKey = GetterUtil.getString(PortletProps
				.get("google.docs.oauth.consumer.key"));
		String consumerSecret = GetterUtil.getString(PortletProps
				.get("google.docs.oauth.consumer.secret"));
		String scope = getScope();

		GoogleOAuthParameters oauthParams = new GoogleOAuthParameters();
		oauthParams.setOAuthConsumerKey(consumerKey);
		oauthParams.setOAuthConsumerSecret(consumerSecret);
		oauthParams.setScope(scope);
		
		OAuthSigner signer = new OAuthHmacSha1Signer();
		DocsService service = new DocsService("Document List");
		service.setOAuthCredentials(oauthParams, signer);
		return service;
	}

	public static User getUser(RenderRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		return user;

	}

	protected static String getOAuthConsumerKey() {
		return _OAUTH_CONSUMER_KEY;

	}

	protected static String getOAuthConsumerSecret() {
		return _OAUTH_CONSUMER_SECRET;

	}

	protected static String getOAuthRequestor() {
		return _OAUTH_REQUESTOR;

	}

	protected static String getScope() {
		return _SCOPE;

	}

	private static final String _OAUTH_CONSUMER_KEY =
			GetterUtil.getString(PortletProps
				.get("google.docs.oauth.consumer.key"));
	private static final String _OAUTH_REQUESTOR =
			GetterUtil.getString(PortletProps
				.get("google.docs.oauth.consumer.requestor"));
	private static final String _OAUTH_CONSUMER_SECRET =
			GetterUtil.getString(PortletProps
				.get("google.docs.oauth.consumer.secret"));
	private static final String _SCOPE =
			GetterUtil.getString(PortletProps
				.get("google.docs.oauth.scope"));

}