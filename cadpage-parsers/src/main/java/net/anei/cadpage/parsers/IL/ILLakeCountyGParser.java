package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ILLakeCountyGParser extends DispatchH05Parser {

  public ILLakeCountyGParser() {
    super("LAKE COUNTY", "IL",
          "( Address:ADDRCITY/S6! Units:UNIT! Cross_Streets:X! Details:EMPTY INFO_BLK+ " +
          "| CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CROSS:X? ID:ID! PRI:PRI! DATE:DATETIME! UNIT:UNIT! INFO:EMPTY! INFO_BLK+ GPS:GPS? " +
          "| DATETIME! Address:ADDRCITY/S6! X! Caller_Phone:PHONE! District:MAP! https:G_GPS! UNIT! " +
          "| ADDRCITY/S6! MAP! UNIT! https:G_GPS! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "administrator@lakecounty911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d|", true);
    if (name.equals("G_GPS")) return new GPSField(".*&query=(.*)", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

}
