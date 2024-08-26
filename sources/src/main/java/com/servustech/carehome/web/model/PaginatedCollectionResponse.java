/**
 *
 */
package com.servustech.carehome.web.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Andrei Groza
 *
 */
@JsonPropertyOrder({ "page", "pageSize", "itemsCount", "items" })
public class PaginatedCollectionResponse<T> extends CollectionResponse<T> {
	private int	page;
	private int	pageSize;

	/**
	 * Default constructor
	 */
	public PaginatedCollectionResponse() {
	}

	/**
	 * Parameterized constructor
	 *
	 * @param items
	 */
	public PaginatedCollectionResponse(final Collection<T> items, final int page, final int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
		setItems(items);
		setItemsCount(items.size());
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return this.page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(
			final int page) {
		this.page = page;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(
			final int pageSize) {
		this.pageSize = pageSize;
	}

}
