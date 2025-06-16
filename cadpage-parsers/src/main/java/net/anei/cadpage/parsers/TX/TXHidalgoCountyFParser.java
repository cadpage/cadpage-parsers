package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class TXHidalgoCountyFParser extends DispatchH05Parser {

  public TXHidalgoCountyFParser() {
    super("HIDALGO COUNTY", "TX",
          "Date:DATETIME! Address_Info:EMPTY! ADDRCITY! GPS? https:SKIP! Incident_Number:ID! Call_Type:CALL! Narrative:EMPTY! INFO_BLK+ Units:EMPTY! UNIT/C+");
  }

  @Override
  public String getFilter() {
    return "noreply@cityofedinburg.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DAGETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;
      super.parse(field.replace('/', ','), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
