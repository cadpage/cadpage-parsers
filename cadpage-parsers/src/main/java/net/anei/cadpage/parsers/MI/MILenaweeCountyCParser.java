package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MILenaweeCountyCParser extends DispatchH05Parser {

  public MILenaweeCountyCParser() {
    super("LENAWEE COUNTY", "MI",
          "Call_Type:CALL! Call_Date/Time:DATETIME! Common_Name:PLACE! ( Call_Address:ADDRCITY! | Address:ADDRCITY ) Additional_Location:PLACE/SDS! " +
             "Cross_Streets:X Narrative:EMPTY! INFO_BLK+ ( Units_Assigned:UNIT! | Units:UNIT! ) Alerts:ALERT! INFO/N+ " +
              "Caller:NAME! Caller's_TX:PHONE! Incident_#:ID! Status_Times:EMPTY! TIMES+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "Lenawee@lenawee.mi.us,hccd@co.hillsdale.mi.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.lastIndexOf("*This is an automated RNR*");
    if (pt >=  0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf("Apt/Lot:");
      if (pt >= 0) {
        apt = field.substring(pt+8).trim();
        field = field.substring(0,pt).trim();
      }
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "4462 N ROGERS HWY",                    "+41.954871,-83.926631"
  });
}
