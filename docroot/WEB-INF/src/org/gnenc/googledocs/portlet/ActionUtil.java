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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.Query.CustomParameter;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.portlet.PortletProps;
public class ActionUtil {

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
		ThemeDisplay themeDisplay = (ThemeDisplay) request
			.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		String userEmail = user.getEmailAddress();
		URL feedUrl = getFeedUrl(userEmail,true);

		if (!user.isDefaultUser()) {
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
	
	public static DocumentListFeed getDocumentListFeedFromTitleQuery(User user, String searchString)
			throws IOException, ServiceException, OAuthException,
			ParseException {
		DocumentListFeed feed = new DocumentListFeed();
		String userEmail = user.getEmailAddress();

		DocumentQuery query = new DocumentQuery(getFeedUrl(userEmail,false));
		query.addCustomParameter(new CustomParameter("xoauth_requestor_id",
			getOAuthRequestor()));
		query.setTitleQuery(searchString);
		query.setTitleExact(false);
		query.setMaxResults(25);
		
		if (!user.isDefaultUser()) {
			DocsService service = getService(userEmail);
			
			try {
				feed = service.getFeed(query, DocumentListFeed.class);
			
			} catch (ResourceNotFoundException e) {
				feed = null;
				System.out.println(e);
				//SessionErrors
					//.add(request, "this-is-not-a-valid-google-account");
				
			} catch (AuthenticationException e) {
				feed = null;
				System.out.println(e);
//				SessionErrors
//					.add(request, "google-docs-authentication-failed");
				
			}
		} else {
			feed = null;
			System.out.println("Is Default User");
//			SessionErrors.add(request,
//					"you-must-be-logged-in-to-view-google-docs");
			
		}
		return feed;

	}

	public static URL getFeedUrl(String userEmail,boolean includeRequestor)
			throws UnsupportedEncodingException, MalformedURLException {
		String scope = getScope();
		String feedUrl = userEmail + "/private/full";
		URL fullFeedUrl;
		
		if (includeRequestor) {
			String requestor = getOAuthRequestor();
			fullFeedUrl = new URL(scope + feedUrl
				+ "?xoauth_requestor_id=" + requestor);
			
		} else {
			
			fullFeedUrl = new URL(scope + feedUrl);
		}
		return fullFeedUrl;

	}

	public static DocsService getService(String userEmail)
			throws UnsupportedEncodingException, MalformedURLException,
				OAuthException {
		GoogleOAuthParameters oauthParams = new GoogleOAuthParameters();
		oauthParams.setOAuthConsumerKey(getOAuthConsumerKey());
		oauthParams.setOAuthConsumerSecret(getOAuthConsumerSecret());
		oauthParams.setScope(getScope());
		
		OAuthSigner signer = new OAuthHmacSha1Signer();
		DocsService service = new DocsService("gnenc-liferay-integration");
		service.useSsl();
		service.setOAuthCredentials(oauthParams, signer);
		
		return service;
		
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
				.get("google.docs.oauth.requestor"));
	private static final String _OAUTH_CONSUMER_SECRET =
			GetterUtil.getString(PortletProps
				.get("google.docs.oauth.consumer.secret"));
	private static final String _SCOPE =
			GetterUtil.getString(PortletProps
				.get("google.docs.oauth.scope"));
	
}
