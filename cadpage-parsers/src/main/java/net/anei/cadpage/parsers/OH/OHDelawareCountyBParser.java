package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHDelawareCountyBParser extends DispatchH05Parser {

  public OHDelawareCountyBParser() {
    super("DELAWARE COUNTY", "OH",
          "( Call_For_Service:SEQ! Call_Date/Time:DATETIME! Units:UNIT! Common_Name:PLACE! Location:ADDRCITY! Cross_Street:X! Alerts:ALERT! Radio_Channel:CH! Narrative:EMPTY! INFO_BLK+? Status_Times:EMPTY! TIMES+ Incident_Numbers:EMPTY! ID Maps:EMPTY! " +
          "| SEQ DATETIME UNIT PLACE ADDRCITY X INFO_BLK+? TIMES+? ID! " +
          "| DATETIME ADDRCITY X UNIT INFO_BLK+? TIMES+? https:SKIP! )");
  }

  @Override
  public String getFilter() {
    return "del-911@co.delaware.oh.us,maleshire@co.delaware.oh.us,nwsysadmin@westerville.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SEQ")) return new SkipField("\\d+", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("911") || field.equals("<UNKNOWN>")) return;
      super.parse(field, data);
    }
  }

  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("[") || !field.endsWith("]")) return false;
      field = field.substring(1, field.length()-1).trim().replace("] [", ",");
      if (field.startsWith("Incident not yet created")) return true;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
