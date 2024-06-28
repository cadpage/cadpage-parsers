package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class INClarkCountyParser extends DispatchH05Parser {

  public INClarkCountyParser() {
    super("CLARK COUNTY", "IN",
          "( DATETIME CALL SRC PLACE ADDRCITY X UNIT ( ID! | PHONE ID! | NAME ID! | NAME PHONE ID/Y! ) https:SKIP! " +
          "| UNIT ADDRCITY CALL ID X! ( ST_INFO_BLK | PHONE ST_INFO_BLK | NAME ST_INFO_BLK | NAME PHONE ST_INFO_BLK ) INFO_BLK+ https:SKIP! GPS1! GPS2! PLACE " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "alert@clarkcounty911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
