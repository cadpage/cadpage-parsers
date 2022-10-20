package net.anei.cadpage.parsers.MD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MDCarolineCountyBParser extends DispatchH05Parser {

  public MDCarolineCountyBParser() {
    super(MDCarolineCountyParser.CITY_LIST, "CAROLINE COUNTY", "MD",
          "Call_Type:CALL! Call_Date/Time:DATETIME! Call_Location:ADDRCITY! ( Cross_Streets:X! | X_Streets:X! ) Common_Name:PLACE! ( Latitude:GPS1! Longitude:GPS2! | ) Box_Area:BOX! Radio_Channel:CH! ( Map:MAP! https:SKIP! Incident_Number:ID! Units:UNIT! | Lat:GPS1! Long:GPS2! Incident_Number:ID! Units:UNIT! Times:EMPTY! TIMES+ CFS_#:SKIP | Units:UNIT! CFS_#:ID! ) Notes:EMPTY INFO_BLK+ Call_Narrative:INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "carolinemfp@carolinemd.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) {
      return parseFields(body.split("\n"), data);
    } else {
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("^(?:APT|RM|ROOM|LOT|SUITE) *", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (data.strCity.isEmpty()) {
          data.strCity = city;
        } else {
          apt = getLeft();
          apt = APT_PTN.matcher(apt).replaceFirst("");
        }
      }
      field = field.replace('@', '&');
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_NO_CITY | FLAG_ANCHOR_END, field, data);
      if (!data.strApt.equals(apt)) data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
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
