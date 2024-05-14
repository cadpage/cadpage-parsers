package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHRichlandCountyAParser extends DispatchH05Parser {

  public OHRichlandCountyAParser() {
    super("RICHLAND COUNTY", "OH",
          "Call_Number:SKIP! Call_Date/Time:DATETIME! Address:ADDRCITY! Units:UNIT! " +
              "Status_Times:EMPTY! TIMES+ Narrative:EMPTY! INFO_BLK+ Incident_Number:ID! Latitude/Longitude:GPS!");
  }

  @Override
  public String getFilter() {
    return "richlandcounty911@richlandcountyoh.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
}
