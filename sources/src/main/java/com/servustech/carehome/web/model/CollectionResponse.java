/**
 *
 */
package com.servustech.carehome.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Andrei Groza
 *
 */
public class CollectionResponse<T> {
	private int				itemsCount;
	private Collection<T>	items;

	/**
	 * Default constructor
	 */
	public CollectionResponse() {
		this(Collections.emptyList());
	}

	/**
	 * Parameterized constructor
	 *
	 * @param items
	 */
	public CollectionResponse(final Collection<T> items) {
		this.items = new ArrayList<>(items);
		this.itemsCount = items.size();
	}

	/**
	 * @return the itemsCount
	 */
	public int getItemsCount() {
		return this.itemsCount;
	}

	/**
	 * @param itemsCount
	 *            the itemsCount to set
	 */
	public void setItemsCount(
			final int itemsCount) {
		this.itemsCount = itemsCount;
	}

	/**
	 * @return the items
	 */
	public Collection<T> getItems() {
		return this.items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(
			final Collection<T> items) {
		this.items = items;
	}

}
