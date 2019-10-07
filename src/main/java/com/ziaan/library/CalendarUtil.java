
package com.ziaan.library;

import java.util.Calendar;

public class CalendarUtil {
	public static Object[][] getTable(int year, int month)
	{
	    Calendar cal = Calendar.getInstance();
	   int date = cal.get(Calendar.DATE);
	
	    cal.set(year, month - 1, 1);
	
	    int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    int firstDay = cal.get(Calendar.DAY_OF_WEEK);
	    Object temp[][] = new Object[6][7];
	    int daycount = 1;
	    for (int i = 0; i < 6; i++)
	    {
	        for (int j = 0; j < 7; j++)
	        {
	            if (firstDay - 1 > 0 || daycount > lastDay)
	            {
	                temp[i][j] = "";
	                firstDay--;
	                continue;
	            }
	            else
	            { temp[i][j] = String.valueOf( daycount ); }
	            daycount++;
	        }
	    }
	
	    return temp;
	}
}