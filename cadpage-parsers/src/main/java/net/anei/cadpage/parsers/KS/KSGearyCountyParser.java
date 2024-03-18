package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KSGearyCountyParser extends DispatchH05Parser {

  public KSGearyCountyParser() {
    super("GEARY COUNTY", "KS",
          "Create_Date/Time:DATETIME! Fire/EMS_Call_Type:CALL! Caller_Name:NAME! Address:ADDRCITY! X-Streets:X! " +
              "Caller_Phone:PHONE! Units:UNIT! Status_Times:EMPTY! TIMES+ Narrative:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "@JCKS.COM";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(" Apt/Lot/Ste:");
      if (pt >= 0) {
        apt = field.substring(pt+13).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
      if (!apt.isEmpty()) {
        data.strAddress = stripFieldEnd(data.strAddress, " " + apt);
        if (!apt.equals(data.strApt)) {
          data.strApt = append(data.strApt, "-", apt);
        }
      }
    }
  }
}
