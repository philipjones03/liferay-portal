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

package com.liferay.portal.security.sso.facebook.connect.portal.settings.web.portlet.resource.bundle;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.settings.web.constants.PortalSettingsPortletKeys;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Stian Sigvartsen
 */
@Component(immediate = true)
public class PortalSettingsPortletResourceBundlePublisher {

	@Activate
	protected void activated(BundleContext bundleContext) throws IOException {
		registerResourceBundle(
			bundleContext, "content.Language",
			PortalSettingsPortletKeys.PORTAL_SETTINGS);
	}

	@Deactivate
	protected void deactivated() {
		for (ServiceRegistration serviceRegistration : _serviceRegistrations) {
			serviceRegistration.unregister();
		}

		_serviceRegistrations.clear();
	}

	@Modified
	protected void modified(BundleContext bundleContext) throws IOException {
		deactivated();

		activated(bundleContext);
	}

	protected void registerResourceBundle(
		BundleContext bundleContext, String bundleName, String portletName) {

		for (Locale locale : _languageUtil.getAvailableLocales()) {
			ResourceBundle resourceBundle = null;

			try {
				resourceBundle = ResourceBundleUtil.getBundle(
					bundleName, locale, getClass().getClassLoader());
			}
			catch (MissingResourceException e) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Unable to create ResourceBundle for locale " + locale,
						e);
				}

				continue;
			}

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			properties.put("javax.portlet.name", portletName);
			properties.put("language.id", LocaleUtil.toLanguageId(locale));

			ServiceRegistration<ResourceBundle> serviceRegistration =
				bundleContext.registerService(
					ResourceBundle.class, resourceBundle, properties);

			_serviceRegistrations.add(serviceRegistration);
		}
	}

	@Reference(unbind = "-")
	protected void setLanguageUtil(LanguageUtil languageUtil) {
		_languageUtil = languageUtil;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalSettingsPortletResourceBundlePublisher.class);

	private LanguageUtil _languageUtil;
	private final List<ServiceRegistration<ResourceBundle>>
		_serviceRegistrations = new ArrayList<>();

}