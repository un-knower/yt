package com.yt.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebUtils {
	private static final Log LOG = LogFactory.getLog(WebUtils.class);
	public static final String LOGIN_USERNAME = "login.username";

	public static String getCurrentLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String value = (String) session.getAttribute(LOGIN_USERNAME);
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"Get session[%s] data success, '%s' = '%s'.",
					session.getId(), LOGIN_USERNAME, value));
		}
		return value;
	}

	public static void setCurrentLoginUser(HttpServletRequest request,
			String username) {
		HttpSession session = request.getSession(true);
		if (username != null) {
			session.setAttribute(LOGIN_USERNAME, username);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(
						"Set session[%s] data success, '%s' = '%s'.",
						session.getId(), LOGIN_USERNAME, username));
			}
		} else {
			session.removeAttribute(LOGIN_USERNAME);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(
						"Remove session[%s] data success, key: %s.",
						session.getId(), LOGIN_USERNAME));
			}
		}
	}
}