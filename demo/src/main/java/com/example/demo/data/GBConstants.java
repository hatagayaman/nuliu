package com.example.demo.data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GBConstants {

	/**
	 * 注文ステータス
	 */
	public static final int ORDER_STATUS_ACCEPTED = 0; // 受付済み
	public static final int ORDER_STATUS_SETTLED = 1; // 決済済み
	public static final int ORDER_STATUS_CANCELED = 9; // キャンセル済み

	/**
	 * 消費税率
	 */
	public static final BigDecimal TAX_RATE = new BigDecimal(0.08f);
	public static final BigDecimal GB_TAX_RATE = new BigDecimal(0.20f);

	public static final int NUM_PER_PAGE = 20;

	public static final Map<Integer, String> DEL_FLG_MAP;
    static {
        Map<Integer, String> del_flg_map = new HashMap<>();
        del_flg_map.put(0, "公開");
        del_flg_map.put(1, "非公開");
        DEL_FLG_MAP = Collections.unmodifiableMap(del_flg_map);
    }
}
