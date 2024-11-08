package kh.st.boot.model.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	public static String getLastWeek() {
		LocalDate now = LocalDate.now();
		LocalDate lastWeek = now.minusWeeks(1);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return lastWeek.format(format);
	} // 1주전 구하는 메소드

	public static String getLastMonth() {
		LocalDate now = LocalDate.now();
		LocalDate lastMonth = now.minusMonths(1);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return lastMonth.format(format);
	} // 1달전 날짜 구하는 메소드

	public static String getLast3Month() {
		LocalDate now = LocalDate.now();
		LocalDate last3Month = now.minusMonths(3);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return last3Month.format(format);
	} // 3달전 날짜 구하는 메소드

	public static String getLastYear() {
		LocalDate now = LocalDate.now();
		LocalDate lastYear = now.minusYears(1);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return lastYear.format(format);
	} // 1년전 날짜 구하는 메소드
}
