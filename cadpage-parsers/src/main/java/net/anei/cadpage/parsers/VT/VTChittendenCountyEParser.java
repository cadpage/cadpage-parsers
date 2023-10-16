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
  private static final Pattern CITY_PTN = Pattern.compile("[ A-Z]{3,}");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = null;
      while (true) {
        int pt = field.lastIndexOf(',');
        if (pt < 0) break;
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (field.isEmpty()) continue;
        if (STATE_PTN.matcher(city).matches()) {
          data.strState = city;
          continue;
        }
        if (apt == null && !CITY_PTN.matcher(city).matches()) {
          apt = city;
          continue;
        }

        data.strCity = city;
        break;
      }
      if (apt != null) {
        field = stripFieldEnd(field, ' ' + apt);
      }
      parseAddress(field, data);
      if (apt != null) data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST APT";
    }
  }

  private class MyInfoXField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }
}
