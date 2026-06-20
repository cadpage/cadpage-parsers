package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAWayneCountyBParser extends DispatchH05Parser {

  public PAWayneCountyBParser() {
    super("WAYNE COUNTY", "PA",
          "( Call_Date/Time:DATETIME! INFO/N+ Call_Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! EMS_Call_Type/Area:CALL! Fire_Call_Type:CALL! Narrative:EMPTY! INFO_BLK+ " +
              "( Unit_Times:EMPTY! TIMES+ Call/Incident_#:ID! https:SKIP! Lat/Lon:GPS! " +
              "| Call/Incident_#:ID! Map_link:EMPTY! https:SKIP! Lat/Lon:GPS! Unit_Times:EMPTY! TIMES+ " +
              ") " +
          "| ADDRCITY! CALL! X! INFO_BLK+? TIMES+? DATETIME SKIP ID ID2 https:GPS2 " +
          ") END");
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "Dispatch@wcpa911.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID2")) return new MyId2Field();
    if (name.equals("GPS2")) return new GPSField("//www.google.com/.*&query=(.*)", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, " BOROUGH");
      data.strCity = stripFieldEnd(data.strCity, " BORO");
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty() || field.equals(data.strCall)) return;
      data.strCall = append(data.strCall, " / ", field);
    }
  }

  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(field, "/", data.strCallId);
    }
  }
}
