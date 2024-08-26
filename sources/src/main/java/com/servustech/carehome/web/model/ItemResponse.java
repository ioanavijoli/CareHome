/**
 *
 */
package com.servustech.carehome.web.model;

/**
 * @author Andrei Groza
 *
 */
public final class ItemResponse<T> {
	@SuppressWarnings("rawtypes")
	public static final ItemResponse	EMPTY	= new ItemResponse<>(null);

	private T							item;

	/**
	 * Default constructor
	 */
	public ItemResponse() {
	}

	/**
	 * Parameterized constructor
	 *
	 * @param item
	 */
	public ItemResponse(final T item) {
		this.item = item;
	}

	/**
	 * @return the item
	 */
	public T getItem() {
		return this.item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(
			final T item) {
		this.item = item;
	}

}
