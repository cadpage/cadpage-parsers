package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class TXEllisCountyDParser extends DispatchH05Parser {

  public TXEllisCountyDParser() {
    super("ELLIS COUNTY", "TX",
          "Units_Assigned:UNIT! Call_Type:CALL! Business_Name:PLACE! Location:ADDRCITY! Cross_Streets:X! " +
              "CAD_Comments:INFO! INFO/N+ Fire_Radio_Channel:CH! Incident_Number(s):ID! Call_Date_and_Time:DATETIME!");
  }

  @Override
  public String getFilter() {
    return "cadalert@ennistx.gov";
  }

  @Override
  public boolean parseFields(String[] flds, Data data) {
    for (int ndx = 0; ndx < flds.length; ndx++) {
      flds[ndx] = stripFieldEnd(flds[ndx], ";");
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr = p.get(',');
      data.strCity = p.get(',');
      String apt = p.get();
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      if (!apt.equals(data.strApt)) data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
