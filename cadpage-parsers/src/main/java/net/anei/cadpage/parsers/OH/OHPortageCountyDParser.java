package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


public class OHPortageCountyDParser extends DispatchH05Parser {

  public OHPortageCountyDParser() {
    this("PORTAGE COUNTY", "OH");
  }

  public OHPortageCountyDParser(String defCity, String defState) {
    super(defCity, defState,
          "Date/time:DATETIME! Address:ADDRCITY! Latitude:GPS1? Longitude:GPS2? Caller_Name:NAME! Caller_phone:PHONE! CFS_Number:SKIP! Common_Name:PLACE! " +
              "Cross_Streets:X! Call_Type:CALL! Incident_Number:ID! ( Alerts:EMPTY! SKIP+ Fire_Radio_Channel:CH! | ) " +
          "( Narrative:EMPTY! | Narrative%EMPTY ) INFO_BLK/Z+? ( Status_Times:EMPTY! | Status_Times%EMPTY! ) TIMES/Z+? ( Units_Assigned:UNIT! | U_UNIT! ) END");
  }

  public String getAliasCode() {
    return "OHPortageCountyD";
  }

  @Override
  public String getFilter() {
    return "dispatch@kent.edu";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("U_UNIT")) return new UnitField("Units Assigned(\\S.*)", true);
    return super.getField(name);
  }

  private class MyPhoneField extends PhoneField {

    public MyPhoneField() {
      super();
    }

    public void parse(String field, Data data) {
      if (field.equals("() -")) return;
      super.parse(field, data);
    }
  }
}
