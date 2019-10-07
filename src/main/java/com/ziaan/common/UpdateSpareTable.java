
package com.ziaan.common;

import com.ziaan.library.*;

public class UpdateSpareTable
{
  public static String setSpareSql() throws Exception {
    String sql = "";
    sql= " update tu_sparetable set updateval = 1, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
    sql+= " where updatekey = ?";
    return sql;
  }

  public static void setSparePbox(PreparedBox pbox) throws Exception {
    pbox.setInt(1, 1); 
  }
}