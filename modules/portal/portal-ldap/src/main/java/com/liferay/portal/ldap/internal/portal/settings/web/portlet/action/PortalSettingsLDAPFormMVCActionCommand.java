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

package com.liferay.portal.ldap.internal.portal.settings.web.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseFormMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.ldap.configuration.ConfigurationProvider;
import com.liferay.portal.ldap.constants.LDAPConstants;
import com.liferay.portal.ldap.exportimport.configuration.LDAPExportConfiguration;
import com.liferay.portal.ldap.exportimport.configuration.LDAPImportConfiguration;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.settings.web.constants.PortalSettingsPortletKeys;
import com.liferay.portal.theme.ThemeDisplay;

import java.util.Dictionary;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + PortalSettingsPortletKeys.PORTAL_SETTINGS,
		"mvc.command.name=/portal_settings/ldap"
	},
	service = MVCActionCommand.class
)
public class PortalSettingsLDAPFormMVCActionCommand
	extends BaseFormMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (!permissionChecker.isCompanyAdmin(themeDisplay.getCompanyId())) {
			SessionErrors.add(actionRequest, PrincipalException.class);

			actionResponse.setRenderParameter("mvcPath", "/error.jsp");

			return;
		}

		updateBooleanProperties(
			actionRequest, _ldapAuthConfigurationProvider,
			themeDisplay.getCompanyId(), LDAPConstants.AUTH_ENABLED,
			LDAPConstants.AUTH_REQUIRED, LDAPConstants.PASSWORD_POLICY_ENABLED);

		updateBooleanProperties(
			actionRequest, _ldapExportConfigurationProvider,
			themeDisplay.getCompanyId(), LDAPConstants.EXPORT_ENABLED);

		updateBooleanProperties(
			actionRequest, _ldapImportConfigurationProvider,
			themeDisplay.getCompanyId(), LDAPConstants.IMPORT_ENABLED,
			LDAPConstants.IMPORT_ON_STARTUP);
	}

	@Override
	protected void doValidateForm(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		LDAPImportConfiguration ldapImportConfiguration =
			_ldapImportConfigurationProvider.getConfiguration(
				themeDisplay.getCompanyId());

		if (!ldapImportConfiguration.importUserPasswordAutogenerated()) {
			return;
		}

		boolean ldapExportEnabled = ParamUtil.getBoolean(
			actionRequest, "ldap--" + LDAPConstants.EXPORT_ENABLED + "--");
		boolean ldapImportEnabled = ParamUtil.getBoolean(
			actionRequest, "ldap--" + LDAPConstants.IMPORT_ENABLED + "--");

		if (ldapExportEnabled && ldapImportEnabled) {
			SessionErrors.add(
				actionRequest, "ldapExportAndImportOnPasswordAutogeneration");
		}
	}

	@Reference(
		target = "(factoryPid=com.liferay.portal.ldap.authenticator.configuration.LDAPAuthConfiguration)",
		unbind = "-"
	)
	protected void setLDAPAuthConfigurationProvider(
		ConfigurationProvider<LDAPAuthConfiguration>
			ldapAuthConfigurationProvider) {

		_ldapAuthConfigurationProvider = ldapAuthConfigurationProvider;
	}

	@Reference(
		target = "(factoryPid=com.liferay.portal.ldap.exportimport.configuration.LDAPExportConfiguration)",
		unbind = "-"
	)
	protected void setLDAPExportConfigurationProvider(
		ConfigurationProvider<LDAPExportConfiguration>
			ldapExportConfigurationProvider) {

		_ldapExportConfigurationProvider = ldapExportConfigurationProvider;
	}

	@Reference(
		target = "(factoryPid=com.liferay.portal.ldap.exportimport.configuration.LDAPImportConfiguration)",
		unbind = "-"
	)
	protected void setLDAPImportConfigurationProvider(
		ConfigurationProvider<LDAPImportConfiguration>
			ldapImportConfigurationProvider) {

		_ldapImportConfigurationProvider = ldapImportConfigurationProvider;
	}

	protected void updateBooleanProperties(
		ActionRequest actionRequest,
		ConfigurationProvider<?> configurationProvider, long companyId,
		String... propertyNames) {

		Dictionary<String, Object> properties =
			configurationProvider.getConfigurationProperties(companyId, false);

		if (properties == null) {
			properties = new HashMapDictionary<>();
		}

		for (String propertyName : propertyNames) {
			boolean value = ParamUtil.getBoolean(
				actionRequest, "ldap--" + propertyName + "--");

			properties.put(propertyName, value);
		}

		configurationProvider.updateProperties(companyId, properties);
	}

	private ConfigurationProvider<LDAPAuthConfiguration>
		_ldapAuthConfigurationProvider;
	private ConfigurationProvider<LDAPExportConfiguration>
		_ldapExportConfigurationProvider;
	private ConfigurationProvider<LDAPImportConfiguration>
		_ldapImportConfigurationProvider;

}