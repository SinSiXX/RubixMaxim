package com.xetanai.rubix.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MiscUtils {
	public static void updateLastHeard(String usrId)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		SQLUtils.changeUser(usrId, "LastHeard", dateFormat.format(date));
	}
}
