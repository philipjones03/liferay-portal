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

package com.liferay.social.networking.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.persistence.CompanyProvider;
import com.liferay.portal.service.persistence.CompanyProviderWrapper;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.spring.extender.service.ServiceReference;

import com.liferay.social.networking.exception.NoSuchMeetupsEntryException;
import com.liferay.social.networking.model.MeetupsEntry;
import com.liferay.social.networking.model.impl.MeetupsEntryImpl;
import com.liferay.social.networking.model.impl.MeetupsEntryModelImpl;
import com.liferay.social.networking.service.persistence.MeetupsEntryPersistence;

import java.io.Serializable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the meetups entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MeetupsEntryPersistence
 * @see com.liferay.social.networking.service.persistence.MeetupsEntryUtil
 * @generated
 */
@ProviderType
public class MeetupsEntryPersistenceImpl extends BasePersistenceImpl<MeetupsEntry>
	implements MeetupsEntryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link MeetupsEntryUtil} to access the meetups entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = MeetupsEntryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, MeetupsEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, MeetupsEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, MeetupsEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, MeetupsEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] { Long.class.getName() },
			MeetupsEntryModelImpl.COMPANYID_COLUMN_BITMASK |
			MeetupsEntryModelImpl.STARTDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the meetups entries where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByCompanyId(long companyId) {
		return findByCompanyId(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the meetups entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @return the range of matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByCompanyId(long companyId, int start, int end) {
		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the meetups entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByCompanyId(long companyId, int start,
		int end, OrderByComparator<MeetupsEntry> orderByComparator) {
		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the meetups entries where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByCompanyId(long companyId, int start,
		int end, OrderByComparator<MeetupsEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId, start, end, orderByComparator };
		}

		List<MeetupsEntry> list = null;

		if (retrieveFromCache) {
			list = (List<MeetupsEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (MeetupsEntry meetupsEntry : list) {
					if ((companyId != meetupsEntry.getCompanyId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_MEETUPSENTRY_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(MeetupsEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (!pagination) {
					list = (List<MeetupsEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<MeetupsEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first meetups entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry findByCompanyId_First(long companyId,
		OrderByComparator<MeetupsEntry> orderByComparator)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = fetchByCompanyId_First(companyId,
				orderByComparator);

		if (meetupsEntry != null) {
			return meetupsEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchMeetupsEntryException(msg.toString());
	}

	/**
	 * Returns the first meetups entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching meetups entry, or <code>null</code> if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry fetchByCompanyId_First(long companyId,
		OrderByComparator<MeetupsEntry> orderByComparator) {
		List<MeetupsEntry> list = findByCompanyId(companyId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last meetups entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry findByCompanyId_Last(long companyId,
		OrderByComparator<MeetupsEntry> orderByComparator)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = fetchByCompanyId_Last(companyId,
				orderByComparator);

		if (meetupsEntry != null) {
			return meetupsEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchMeetupsEntryException(msg.toString());
	}

	/**
	 * Returns the last meetups entry in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching meetups entry, or <code>null</code> if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry fetchByCompanyId_Last(long companyId,
		OrderByComparator<MeetupsEntry> orderByComparator) {
		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<MeetupsEntry> list = findByCompanyId(companyId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the meetups entries before and after the current meetups entry in the ordered set where companyId = &#63;.
	 *
	 * @param meetupsEntryId the primary key of the current meetups entry
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry[] findByCompanyId_PrevAndNext(long meetupsEntryId,
		long companyId, OrderByComparator<MeetupsEntry> orderByComparator)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = findByPrimaryKey(meetupsEntryId);

		Session session = null;

		try {
			session = openSession();

			MeetupsEntry[] array = new MeetupsEntryImpl[3];

			array[0] = getByCompanyId_PrevAndNext(session, meetupsEntry,
					companyId, orderByComparator, true);

			array[1] = meetupsEntry;

			array[2] = getByCompanyId_PrevAndNext(session, meetupsEntry,
					companyId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected MeetupsEntry getByCompanyId_PrevAndNext(Session session,
		MeetupsEntry meetupsEntry, long companyId,
		OrderByComparator<MeetupsEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_MEETUPSENTRY_WHERE);

		query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(MeetupsEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(meetupsEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<MeetupsEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the meetups entries where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (MeetupsEntry meetupsEntry : findByCompanyId(companyId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(meetupsEntry);
		}
	}

	/**
	 * Returns the number of meetups entries where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching meetups entries
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_COMPANYID;

		Object[] finderArgs = new Object[] { companyId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_MEETUPSENTRY_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 = "meetupsEntry.companyId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, MeetupsEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, MeetupsEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] { Long.class.getName() },
			MeetupsEntryModelImpl.USERID_COLUMN_BITMASK |
			MeetupsEntryModelImpl.STARTDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the meetups entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByUserId(long userId) {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the meetups entries where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @return the range of matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByUserId(long userId, int start, int end) {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the meetups entries where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByUserId(long userId, int start, int end,
		OrderByComparator<MeetupsEntry> orderByComparator) {
		return findByUserId(userId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the meetups entries where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching meetups entries
	 */
	@Override
	public List<MeetupsEntry> findByUserId(long userId, int start, int end,
		OrderByComparator<MeetupsEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId, start, end, orderByComparator };
		}

		List<MeetupsEntry> list = null;

		if (retrieveFromCache) {
			list = (List<MeetupsEntry>)finderCache.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (MeetupsEntry meetupsEntry : list) {
					if ((userId != meetupsEntry.getUserId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_MEETUPSENTRY_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(MeetupsEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (!pagination) {
					list = (List<MeetupsEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<MeetupsEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first meetups entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry findByUserId_First(long userId,
		OrderByComparator<MeetupsEntry> orderByComparator)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = fetchByUserId_First(userId,
				orderByComparator);

		if (meetupsEntry != null) {
			return meetupsEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchMeetupsEntryException(msg.toString());
	}

	/**
	 * Returns the first meetups entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching meetups entry, or <code>null</code> if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry fetchByUserId_First(long userId,
		OrderByComparator<MeetupsEntry> orderByComparator) {
		List<MeetupsEntry> list = findByUserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last meetups entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry findByUserId_Last(long userId,
		OrderByComparator<MeetupsEntry> orderByComparator)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = fetchByUserId_Last(userId, orderByComparator);

		if (meetupsEntry != null) {
			return meetupsEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchMeetupsEntryException(msg.toString());
	}

	/**
	 * Returns the last meetups entry in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching meetups entry, or <code>null</code> if a matching meetups entry could not be found
	 */
	@Override
	public MeetupsEntry fetchByUserId_Last(long userId,
		OrderByComparator<MeetupsEntry> orderByComparator) {
		int count = countByUserId(userId);

		if (count == 0) {
			return null;
		}

		List<MeetupsEntry> list = findByUserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the meetups entries before and after the current meetups entry in the ordered set where userId = &#63;.
	 *
	 * @param meetupsEntryId the primary key of the current meetups entry
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry[] findByUserId_PrevAndNext(long meetupsEntryId,
		long userId, OrderByComparator<MeetupsEntry> orderByComparator)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = findByPrimaryKey(meetupsEntryId);

		Session session = null;

		try {
			session = openSession();

			MeetupsEntry[] array = new MeetupsEntryImpl[3];

			array[0] = getByUserId_PrevAndNext(session, meetupsEntry, userId,
					orderByComparator, true);

			array[1] = meetupsEntry;

			array[2] = getByUserId_PrevAndNext(session, meetupsEntry, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected MeetupsEntry getByUserId_PrevAndNext(Session session,
		MeetupsEntry meetupsEntry, long userId,
		OrderByComparator<MeetupsEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_MEETUPSENTRY_WHERE);

		query.append(_FINDER_COLUMN_USERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(MeetupsEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(meetupsEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<MeetupsEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the meetups entries where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 */
	@Override
	public void removeByUserId(long userId) {
		for (MeetupsEntry meetupsEntry : findByUserId(userId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(meetupsEntry);
		}
	}

	/**
	 * Returns the number of meetups entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching meetups entries
	 */
	@Override
	public int countByUserId(long userId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID;

		Object[] finderArgs = new Object[] { userId };

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_MEETUPSENTRY_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_USERID_USERID_2 = "meetupsEntry.userId = ?";

	public MeetupsEntryPersistenceImpl() {
		setModelClass(MeetupsEntry.class);
	}

	/**
	 * Caches the meetups entry in the entity cache if it is enabled.
	 *
	 * @param meetupsEntry the meetups entry
	 */
	@Override
	public void cacheResult(MeetupsEntry meetupsEntry) {
		entityCache.putResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryImpl.class, meetupsEntry.getPrimaryKey(), meetupsEntry);

		meetupsEntry.resetOriginalValues();
	}

	/**
	 * Caches the meetups entries in the entity cache if it is enabled.
	 *
	 * @param meetupsEntries the meetups entries
	 */
	@Override
	public void cacheResult(List<MeetupsEntry> meetupsEntries) {
		for (MeetupsEntry meetupsEntry : meetupsEntries) {
			if (entityCache.getResult(
						MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
						MeetupsEntryImpl.class, meetupsEntry.getPrimaryKey()) == null) {
				cacheResult(meetupsEntry);
			}
			else {
				meetupsEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all meetups entries.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(MeetupsEntryImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the meetups entry.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(MeetupsEntry meetupsEntry) {
		entityCache.removeResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryImpl.class, meetupsEntry.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<MeetupsEntry> meetupsEntries) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (MeetupsEntry meetupsEntry : meetupsEntries) {
			entityCache.removeResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
				MeetupsEntryImpl.class, meetupsEntry.getPrimaryKey());
		}
	}

	/**
	 * Creates a new meetups entry with the primary key. Does not add the meetups entry to the database.
	 *
	 * @param meetupsEntryId the primary key for the new meetups entry
	 * @return the new meetups entry
	 */
	@Override
	public MeetupsEntry create(long meetupsEntryId) {
		MeetupsEntry meetupsEntry = new MeetupsEntryImpl();

		meetupsEntry.setNew(true);
		meetupsEntry.setPrimaryKey(meetupsEntryId);

		meetupsEntry.setCompanyId(companyProvider.getCompanyId());

		return meetupsEntry;
	}

	/**
	 * Removes the meetups entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param meetupsEntryId the primary key of the meetups entry
	 * @return the meetups entry that was removed
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry remove(long meetupsEntryId)
		throws NoSuchMeetupsEntryException {
		return remove((Serializable)meetupsEntryId);
	}

	/**
	 * Removes the meetups entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the meetups entry
	 * @return the meetups entry that was removed
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry remove(Serializable primaryKey)
		throws NoSuchMeetupsEntryException {
		Session session = null;

		try {
			session = openSession();

			MeetupsEntry meetupsEntry = (MeetupsEntry)session.get(MeetupsEntryImpl.class,
					primaryKey);

			if (meetupsEntry == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchMeetupsEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(meetupsEntry);
		}
		catch (NoSuchMeetupsEntryException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected MeetupsEntry removeImpl(MeetupsEntry meetupsEntry) {
		meetupsEntry = toUnwrappedModel(meetupsEntry);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(meetupsEntry)) {
				meetupsEntry = (MeetupsEntry)session.get(MeetupsEntryImpl.class,
						meetupsEntry.getPrimaryKeyObj());
			}

			if (meetupsEntry != null) {
				session.delete(meetupsEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (meetupsEntry != null) {
			clearCache(meetupsEntry);
		}

		return meetupsEntry;
	}

	@Override
	public MeetupsEntry updateImpl(MeetupsEntry meetupsEntry) {
		meetupsEntry = toUnwrappedModel(meetupsEntry);

		boolean isNew = meetupsEntry.isNew();

		MeetupsEntryModelImpl meetupsEntryModelImpl = (MeetupsEntryModelImpl)meetupsEntry;

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (meetupsEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				meetupsEntry.setCreateDate(now);
			}
			else {
				meetupsEntry.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!meetupsEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				meetupsEntry.setModifiedDate(now);
			}
			else {
				meetupsEntry.setModifiedDate(serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (meetupsEntry.isNew()) {
				session.save(meetupsEntry);

				meetupsEntry.setNew(false);
			}
			else {
				meetupsEntry = (MeetupsEntry)session.merge(meetupsEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !MeetupsEntryModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((meetupsEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						meetupsEntryModelImpl.getOriginalCompanyId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_COMPANYID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);

				args = new Object[] { meetupsEntryModelImpl.getCompanyId() };

				finderCache.removeResult(FINDER_PATH_COUNT_BY_COMPANYID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);
			}

			if ((meetupsEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						meetupsEntryModelImpl.getOriginalUserId()
					};

				finderCache.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] { meetupsEntryModelImpl.getUserId() };

				finderCache.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}
		}

		entityCache.putResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
			MeetupsEntryImpl.class, meetupsEntry.getPrimaryKey(), meetupsEntry,
			false);

		meetupsEntry.resetOriginalValues();

		return meetupsEntry;
	}

	protected MeetupsEntry toUnwrappedModel(MeetupsEntry meetupsEntry) {
		if (meetupsEntry instanceof MeetupsEntryImpl) {
			return meetupsEntry;
		}

		MeetupsEntryImpl meetupsEntryImpl = new MeetupsEntryImpl();

		meetupsEntryImpl.setNew(meetupsEntry.isNew());
		meetupsEntryImpl.setPrimaryKey(meetupsEntry.getPrimaryKey());

		meetupsEntryImpl.setMeetupsEntryId(meetupsEntry.getMeetupsEntryId());
		meetupsEntryImpl.setCompanyId(meetupsEntry.getCompanyId());
		meetupsEntryImpl.setUserId(meetupsEntry.getUserId());
		meetupsEntryImpl.setUserName(meetupsEntry.getUserName());
		meetupsEntryImpl.setCreateDate(meetupsEntry.getCreateDate());
		meetupsEntryImpl.setModifiedDate(meetupsEntry.getModifiedDate());
		meetupsEntryImpl.setTitle(meetupsEntry.getTitle());
		meetupsEntryImpl.setDescription(meetupsEntry.getDescription());
		meetupsEntryImpl.setStartDate(meetupsEntry.getStartDate());
		meetupsEntryImpl.setEndDate(meetupsEntry.getEndDate());
		meetupsEntryImpl.setTotalAttendees(meetupsEntry.getTotalAttendees());
		meetupsEntryImpl.setMaxAttendees(meetupsEntry.getMaxAttendees());
		meetupsEntryImpl.setPrice(meetupsEntry.getPrice());
		meetupsEntryImpl.setThumbnailId(meetupsEntry.getThumbnailId());

		return meetupsEntryImpl;
	}

	/**
	 * Returns the meetups entry with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the meetups entry
	 * @return the meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchMeetupsEntryException {
		MeetupsEntry meetupsEntry = fetchByPrimaryKey(primaryKey);

		if (meetupsEntry == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchMeetupsEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return meetupsEntry;
	}

	/**
	 * Returns the meetups entry with the primary key or throws a {@link com.liferay.social.networking.NoSuchMeetupsEntryException} if it could not be found.
	 *
	 * @param meetupsEntryId the primary key of the meetups entry
	 * @return the meetups entry
	 * @throws com.liferay.social.networking.NoSuchMeetupsEntryException if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry findByPrimaryKey(long meetupsEntryId)
		throws NoSuchMeetupsEntryException {
		return findByPrimaryKey((Serializable)meetupsEntryId);
	}

	/**
	 * Returns the meetups entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the meetups entry
	 * @return the meetups entry, or <code>null</code> if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry fetchByPrimaryKey(Serializable primaryKey) {
		MeetupsEntry meetupsEntry = (MeetupsEntry)entityCache.getResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
				MeetupsEntryImpl.class, primaryKey);

		if (meetupsEntry == _nullMeetupsEntry) {
			return null;
		}

		if (meetupsEntry == null) {
			Session session = null;

			try {
				session = openSession();

				meetupsEntry = (MeetupsEntry)session.get(MeetupsEntryImpl.class,
						primaryKey);

				if (meetupsEntry != null) {
					cacheResult(meetupsEntry);
				}
				else {
					entityCache.putResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
						MeetupsEntryImpl.class, primaryKey, _nullMeetupsEntry);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
					MeetupsEntryImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return meetupsEntry;
	}

	/**
	 * Returns the meetups entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param meetupsEntryId the primary key of the meetups entry
	 * @return the meetups entry, or <code>null</code> if a meetups entry with the primary key could not be found
	 */
	@Override
	public MeetupsEntry fetchByPrimaryKey(long meetupsEntryId) {
		return fetchByPrimaryKey((Serializable)meetupsEntryId);
	}

	@Override
	public Map<Serializable, MeetupsEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, MeetupsEntry> map = new HashMap<Serializable, MeetupsEntry>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			MeetupsEntry meetupsEntry = fetchByPrimaryKey(primaryKey);

			if (meetupsEntry != null) {
				map.put(primaryKey, meetupsEntry);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			MeetupsEntry meetupsEntry = (MeetupsEntry)entityCache.getResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
					MeetupsEntryImpl.class, primaryKey);

			if (meetupsEntry == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, meetupsEntry);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_MEETUPSENTRY_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (MeetupsEntry meetupsEntry : (List<MeetupsEntry>)q.list()) {
				map.put(meetupsEntry.getPrimaryKeyObj(), meetupsEntry);

				cacheResult(meetupsEntry);

				uncachedPrimaryKeys.remove(meetupsEntry.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(MeetupsEntryModelImpl.ENTITY_CACHE_ENABLED,
					MeetupsEntryImpl.class, primaryKey, _nullMeetupsEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the meetups entries.
	 *
	 * @return the meetups entries
	 */
	@Override
	public List<MeetupsEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the meetups entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @return the range of meetups entries
	 */
	@Override
	public List<MeetupsEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the meetups entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of meetups entries
	 */
	@Override
	public List<MeetupsEntry> findAll(int start, int end,
		OrderByComparator<MeetupsEntry> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the meetups entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link MeetupsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of meetups entries
	 * @param end the upper bound of the range of meetups entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of meetups entries
	 */
	@Override
	public List<MeetupsEntry> findAll(int start, int end,
		OrderByComparator<MeetupsEntry> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<MeetupsEntry> list = null;

		if (retrieveFromCache) {
			list = (List<MeetupsEntry>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_MEETUPSENTRY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_MEETUPSENTRY;

				if (pagination) {
					sql = sql.concat(MeetupsEntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<MeetupsEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<MeetupsEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the meetups entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (MeetupsEntry meetupsEntry : findAll()) {
			remove(meetupsEntry);
		}
	}

	/**
	 * Returns the number of meetups entries.
	 *
	 * @return the number of meetups entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_MEETUPSENTRY);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return MeetupsEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the meetups entry persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(MeetupsEntryImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = CompanyProviderWrapper.class)
	protected CompanyProvider companyProvider;
	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;
	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;
	private static final String _SQL_SELECT_MEETUPSENTRY = "SELECT meetupsEntry FROM MeetupsEntry meetupsEntry";
	private static final String _SQL_SELECT_MEETUPSENTRY_WHERE_PKS_IN = "SELECT meetupsEntry FROM MeetupsEntry meetupsEntry WHERE meetupsEntryId IN (";
	private static final String _SQL_SELECT_MEETUPSENTRY_WHERE = "SELECT meetupsEntry FROM MeetupsEntry meetupsEntry WHERE ";
	private static final String _SQL_COUNT_MEETUPSENTRY = "SELECT COUNT(meetupsEntry) FROM MeetupsEntry meetupsEntry";
	private static final String _SQL_COUNT_MEETUPSENTRY_WHERE = "SELECT COUNT(meetupsEntry) FROM MeetupsEntry meetupsEntry WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "meetupsEntry.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No MeetupsEntry exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No MeetupsEntry exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(MeetupsEntryPersistenceImpl.class);
	private static final MeetupsEntry _nullMeetupsEntry = new MeetupsEntryImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<MeetupsEntry> toCacheModel() {
				return _nullMeetupsEntryCacheModel;
			}
		};

	private static final CacheModel<MeetupsEntry> _nullMeetupsEntryCacheModel = new CacheModel<MeetupsEntry>() {
			@Override
			public MeetupsEntry toEntityModel() {
				return _nullMeetupsEntry;
			}
		};
}