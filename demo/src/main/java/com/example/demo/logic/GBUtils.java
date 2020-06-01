package com.example.demo.logic;

import java.math.BigDecimal;

import com.example.demo.data.GBConstants;

public final class GBUtils {
	public static final String makeMovieUrl(int movieid) {
		return "/movieroom/" + movieid;
	}
	/**
	 * 数値を金額表記に変換
	 * 例）11100→\11,100 or 11,100
	 */
	public static final String formatPriceForDisplay(boolean withYenMark, BigDecimal price) {
		String str = String.valueOf(price);
		int len = str.length();
		StringBuffer sb = new StringBuffer();
		for (int i = len; i > 0; i--) {
			if (i != len && (i - len) % 3 == 0) {
				sb.append(",");
			}
			sb.append(str.charAt(i - 1));
		}
		return (withYenMark ? "\\" : "") + sb.reverse().toString();
	}

	/**
	 * 消費税額計算
	 */
	public static final BigDecimal calcTax(BigDecimal amount) {
		return amount.divide(GBConstants.TAX_RATE);
	}

	/**
	 * 消費税抜き金額計算
	 */
	public static final BigDecimal calcAmountWithoutTax(BigDecimal amount) {
		// TODO
		return null;
	}

	/**
	 * 消費税込み金額計算
	 */
	public static final BigDecimal calcAmountWithTax(BigDecimal amount) {
		// TODO
		return null;
	}
}
