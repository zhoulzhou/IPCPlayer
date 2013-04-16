package com.example.ipcplayer.utils;

import java.util.Formatter;
import java.util.Locale;

import com.example.ipcplayer.R;

import android.content.Context;

public class ConvertUtil {
	
	private static StringBuilder sFormatBuilder = new StringBuilder();
	private static Formatter sFormatter = new Formatter(sFormatBuilder,
			Locale.getDefault());
	private static final Object[] sTimeArgs = new Object[5];
	
	public static String makeTimeString(Context context, long secs) {
		if (secs == 0)
			return "0:00";

		String durationformat = context
				.getString(secs < 3600 ? R.string.durationformatshort
						: R.string.durationformatlong);

		/*
		 * Provide multiple arguments so the format can be changed easily by
		 * modifying the xml.
		 */
		sFormatBuilder.setLength(0);

		final Object[] timeArgs = sTimeArgs;
		timeArgs[0] = secs / 3600;
		timeArgs[1] = secs / 60;
		timeArgs[2] = (secs / 60) % 60;
		timeArgs[3] = secs;
		timeArgs[4] = secs % 60;

		return sFormatter.format(durationformat, timeArgs).toString();
	}
	
}