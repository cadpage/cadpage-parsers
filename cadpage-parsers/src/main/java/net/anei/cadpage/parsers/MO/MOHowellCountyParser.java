package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MOHowellCountyParser extends DispatchH05Parser {
  
  public MOHowellCountyParser() {
    super("HOWELL COUNTY", "MO", 
          "( SELECT/1 CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! X_STREET:X1! LAT/LON:GPS1 NOTE:INFO1% INFO1/N+ " + 
          "| REPORT_DATE/TIME:DATETIME2! CALLER_NAME:NAME! CBN:PHONE! ADDR:ADDRCITY! CALL_DATE/TIME:SKIP! UNIT:UNIT! INFO:EMPTY! INFO_BLK+ INCIDENT_INFORMATION:EMPTY! INC_#:ID2! TIMES:EMPTY! TIMES+ )");
  }
  
  @Override
  public String getFilter() {
    return "cad@howellcounty911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.startsWith("Automatic R&R Notification:")) {
      setSelectValue("2");
      return super.parseHtmlMsg(subject, body, data);
    } else {
      setSelectValue("1");
      return parseFields(body.split("\n"), data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X1")) return new MyCross1Field();
    if (name.equals("GPS1")) return new MyGPS1Field();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ID2")) return new MyId2Field();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCross1Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyGPS1Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
  
  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("PowerPhone CACH:") ||
          field.equals("Call started") ||
          field.equals("Call closed")) return;
      super.parse(field, data);
    }
  }
  
  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      for (String id : field.split(",")) {
        id = id.trim();
        id = stripFieldStart(id, "[");
        id = stripFieldEnd(id, "]");
        if (id.startsWith("Incident not yet created")) continue;
        int pt = id.indexOf(' ');
        if (pt >= 0) id = id.substring(0, pt).trim();
        data.strCallId = append(data.strCallId, ",", id);
      }
    }
  }
}
