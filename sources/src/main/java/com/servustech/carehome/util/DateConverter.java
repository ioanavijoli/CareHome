/**
 *
 */
package com.servustech.carehome.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date converter utility class
 *
 * @author Andrei Groza
 *
 */
public class DateConverter {
	private static DateTimeFormatter	DATE_FORMATTER		= DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
	private static DateTimeFormatter	DATE_TIME_FORMATTER	= DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_FORMAT);

	public static LocalDate toLocalDate(
			final String date) {
		return LocalDate.parse(date, DATE_FORMATTER);
	}

	public static LocalDateTime toLocalDateTime(
			final String dateTime) {
		return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
	}

	public static String toString(
			final LocalDate localDate) {
		return DATE_FORMATTER.format(localDate);
	}

	public static String toString(
			final LocalDateTime localDateTime) {
		return DATE_TIME_FORMATTER.format(localDateTime);
	}
}
