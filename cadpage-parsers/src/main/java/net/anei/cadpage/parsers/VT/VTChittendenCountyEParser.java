package net.anei.cadpage.parsers.VT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VTChittendenCountyEParser extends DispatchH05Parser {

  public VTChittendenCountyEParser() {
    super("CHITTENDEN COUNTY", "VT",
          "Call_Address:SKIP? " +
            "( Cross_Streets:X! Google_Map_Link:EMPTY! SKIP! Call_Type:CALL! Call_Address:ADDRCITY! Units:UNIT! Status_Times:EMPTY! TIMES+? Call_Date/Time:DATETIME! Incident_#:ID! LAT/LONG:GPS! TAG " +
            "| CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! CROSS_STREETS:X! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY INFO_BLK+? INCIDENT:ID! APT:INFOX! LAT:GPS1! LON:GPS2! Times:EMPTY TIMES+ ) END");
  }

  @Override
  public String getFilter() {
    return "@burlingtonvt.gov,@bpdvt.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }


  @Override
  protected boolean parseFields(String[] flds, Data data) {
    for (int ii = 0; ii < flds.length; ii++) {
      flds[ii] = stripFieldEnd(flds[ii].trim(), ";");;
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TAG")) return new SkipField("\\*{2}.*\\*{2}", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFOX")) return new MyInfoXField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}|");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (STATE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private class MyInfoXField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }
}
