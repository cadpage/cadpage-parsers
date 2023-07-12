package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class CAAlamedaCountyParser extends DispatchH05Parser {

  public CAAlamedaCountyParser() {
    super("ALAMEDA COUNTY", "CA",
          "CALL_TYPE:CALL! ADDR:ADDRCITY! COMMON_NAME:PLACE! X-STREETS:X! CALLER_NAME:NAME! CALLER_PHONE:PHONE! INCIDENT_#:ID! STATUS_TIMES:EMPTY! TIMES+ UNITS_DISPATCHED:UNIT! NARRATIVE:EMPTY! INFO_BLK+ ALERTS:ALERT! END");
  }

  @Override
  public String getFilter() {
    return "@piedmont.ca.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(" APT:");
      if (pt >= 0) {
        apt = field.substring(pt+5).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
}
