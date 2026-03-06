package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ILKendallCountyParser extends DispatchH05Parser {

  public ILKendallCountyParser() {
    super("KENDALL COUNTY", "IL",
          "Address:ADDRCITY/S6! Cross:X! Alerts:ALERT! Nature:CALL! Call_Type:CODE! Description:CALL/SDS! Due:UNIT! Incident_Number:ID! Date_/_Time:DATETIME! GPS! END");
  }

  @Override
  public String getFilter() {
    return "CAD@KenCom911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("https://www.google.com/maps/.*query=(.*)", true);
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
