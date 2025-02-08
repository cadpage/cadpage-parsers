package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PADauphinCountyBParser extends DispatchH05Parser {

  public PADauphinCountyBParser() {
    this("DAUPHIN COUNTY", "PA");
  }

  public PADauphinCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "Report_Date:SKIP! Call_Date:DATETIME! Call_Address:ADDRCITY! " +
                "( Latitude:GPS1! Longitude:GPS2! Common_Name:PLACE! " +
                "| Common_Name:PLACE! ( Latitude:GPS1! ( Longitude:GPS2! | Logitude:GPS2! ) | ) " +
                ") Cross_Streets:X! Fire_Call_Type:SKIP! Fire_Box:BOX! EMS_Box:BOX! EMS_Call_Type:SKIP Nature_Of_Call:CALL? " +
                      "Unit_Incident_Number:ID! Unit_Times:EMPTY! TIMES+? Units_Assigned:UNIT");
    setAccumulateUnits(true);
  }

  @Override
  public String getAliasCode() {
    return "PADauphinCountyB";
  }

  @Override
  public String getFilter() {
    return "@lcdes.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " Borough");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strBox)) return;
      data.strBox = append(data.strBox, "/", field);

    }
  }
}
