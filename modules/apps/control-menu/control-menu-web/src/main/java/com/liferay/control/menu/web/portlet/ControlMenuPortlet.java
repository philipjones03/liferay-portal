/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.control.menu.web.portlet;

import com.liferay.control.menu.web.constants.ControlMenuPortletKeys;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.MultiSessionMessages;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.sites.util.SitesUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-control-menu",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.instanceable=false",
		"com.liferay.portlet.use-default-template=false",
		"javax.portlet.name=" + ControlMenuPortletKeys.CONTROL_MENU,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class ControlMenuPortlet extends MVCPortlet {

	public void resetCustomizationView(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (!LayoutPermissionUtil.contains(
				themeDisplay.getPermissionChecker(), themeDisplay.getLayout(),
				ActionKeys.CUSTOMIZE)) {

			throw new PrincipalException();
		}

		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		if ((layoutTypePortlet != null) && layoutTypePortlet.isCustomizable() &&
			layoutTypePortlet.isCustomizedView()) {

			layoutTypePortlet.resetUserPreferences();
		}

		MultiSessionMessages.add(
			actionRequest,
			PortalUtil.getPortletId(actionRequest) + "requestProcessed");
	}

	public void resetPrototype(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		SitesUtil.resetPrototype(themeDisplay.getLayout());

		MultiSessionMessages.add(
			actionRequest,
			PortalUtil.getPortletId(actionRequest) + "requestProcessed");
	}

	@Override
	protected boolean isSessionErrorException(Throwable cause) {
		if (cause instanceof SystemException ||
			super.isSessionErrorException(cause)) {

			return true;
		}

		return false;
	}

}