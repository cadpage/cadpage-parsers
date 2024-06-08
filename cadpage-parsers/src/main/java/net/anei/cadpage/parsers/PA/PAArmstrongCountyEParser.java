package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAArmstrongCountyEParser extends DispatchH05Parser {

  public PAArmstrongCountyEParser() {
    super("ARMSTRONG COUNTY", "PA",
          "EMS_CFS_Type:SKIP! Fire_CFS_Type:SKIP! Nature_of_Call:CALL! Narrative:EMPTY! INFO_BLK+ Location:ADDRCITY! " +
              "Cross_Streets:X? Common_Name:PLACE? Additional_Info:INFO/N? Info:INFO/N? Call_Date/Time:DATETIME! Call_Number:ID! " +
              "Fire_Quandrant:MAP! Ems_District:MAP/L! Status_Times:EMPTY TIMES+ Caller_Name:NAME Caller_Phone:PHONE");
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "noreply@co.armstrong.pa.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    int pt = data.strAddress.indexOf('=');
    if (pt >= 0) {
      data.strCity = data.strAddress.substring(pt+1).trim();
      data.strAddress = data.strAddress.substring(0,pt).trim();
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
    }
  }
}
