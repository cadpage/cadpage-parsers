package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


public class OHPortageCountyDParser extends DispatchH05Parser {

  public OHPortageCountyDParser() {
    this("PORTAGE COUNTY", "OH");
  }

  public OHPortageCountyDParser(String defCity, String defState) {
    super(defCity, defState,
          "( U_DATETIME U_ADDRESS U_NAME U_PHONE U_CFS_NUMBER U_PLACE U_X U_CALL U_ID! " +
          "| Date/time:DATETIME! Address:ADDRCITY! Latitude:GPS1? Longitude:GPS2? Caller_Name:NAME! Caller_phone:PHONE! CFS_Number:SKIP! Common_Name:PLACE! Cross_Streets:X! Call_Type:CALL! Incident_Number:ID! " +
          ") ( Narrative%EMPTY! | Narrative:EMPTY! ) INFO_BLK/Z+? ( Status_Times%EMPTY! | Status_Times:EMPTY! ) TIMES/Z+? U_UNIT! U_ALERT? INFO2/N+");
  }

  public String getAliasCode() {
    return "OHPortageCountyD";
  }

  @Override
  public String getFilter() {
    return "@kent.edu.ahenterly@stow.oh.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("U_DATETIME")) return new DateTimeField("Date/time *(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d)", true);
    if (name.equals("U_ADDRESS")) return new AddressCityField("Address *(.*)", true);
    if (name.equals("U_NAME")) return new NameField("Caller Name *(.*)", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("U_PHONE")) return new MyPhoneField("Caller phone *(.*)", true);
    if (name.equals("U_CFS_NUMBER")) return new SkipField("CFS Number.*", true);
    if (name.equals("U_PLACE")) return new PlaceField("Common Name *(.*)", true);
    if (name.equals("U_X")) return new CrossField("Cross Streets *(.*)", true);
    if (name.equals("U_CALL")) return new CallField("Call Type *(.*)", true);
    if (name.equals("U_ID")) return new IdField("Incident Number *(.*)", true);
    if (name.equals("U_UNIT")) return new UnitField("Units Assigned:? *(.+)", true);
    if (name.equals("U_ALERT")) return new AlertField("Alerts:? *(.*)", true);
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private class MyPhoneField extends PhoneField {

    public MyPhoneField() {
      super();
    }

    public MyPhoneField(String pattern, boolean hard) {
      super(pattern, hard);
    }

    public void parse(String field, Data data) {
      if (field.equals("() -")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("- \\d+ -");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
