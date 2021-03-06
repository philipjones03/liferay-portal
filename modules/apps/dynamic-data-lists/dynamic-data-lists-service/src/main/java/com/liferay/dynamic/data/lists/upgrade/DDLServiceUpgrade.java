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

package com.liferay.dynamic.data.lists.upgrade;

import com.liferay.dynamic.data.lists.upgrade.v1_0_0.UpgradeClassNames;
import com.liferay.dynamic.data.lists.upgrade.v1_0_0.UpgradeDynamicDataLists;
import com.liferay.dynamic.data.lists.upgrade.v1_0_0.UpgradeLastPublishDate;
import com.liferay.dynamic.data.lists.upgrade.v1_0_0.UpgradeSchema;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portlet.expando.service.ExpandoRowLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class DDLServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.dynamic.data.lists.service", "0.0.1", "1.0.0",
			new UpgradeSchema(), new UpgradeClassNames(),
			new UpgradeDynamicDataLists(
				_expandoRowLocalService, _expandoTableLocalService,
				_expandoValueLocalService),
			new UpgradeLastPublishDate());
	}

	@Reference(unbind = "-")
	public void setExpandoRowLocalService(
		ExpandoRowLocalService expandoRowLocalService) {

		_expandoRowLocalService = expandoRowLocalService;
	}

	@Reference(unbind = "-")
	public void setExpandoTableLocalService(
		ExpandoTableLocalService expandoTableLocalService) {

		_expandoTableLocalService = expandoTableLocalService;
	}

	@Reference(unbind = "-")
	public void setExpandoValueLocalService(
		ExpandoValueLocalService expandoValueLocalService) {

		_expandoValueLocalService = expandoValueLocalService;
	}

	@Reference(unbind = "-")
	protected void setDDMStructureLocalService(
		DDMStructureLocalService ddmStructureLocalService) {
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	private ExpandoRowLocalService _expandoRowLocalService;
	private ExpandoTableLocalService _expandoTableLocalService;
	private ExpandoValueLocalService _expandoValueLocalService;

}