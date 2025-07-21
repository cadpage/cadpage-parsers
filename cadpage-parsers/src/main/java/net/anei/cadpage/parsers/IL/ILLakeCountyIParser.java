package net.anei.cadpage.parsers.IL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ILLakeCountyIParser extends DispatchH05Parser {

  public ILLakeCountyIParser() {
    super(ILLakeCountyParser.CITY_LIST, "LAKE COUNTY", "IL",
          "Call_Date:DATETIME! Incident_Number:ID! Units:UNIT! Fire_Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE! Fire_Area:MAP! Narrative:EMPTY! INFO_BLK+ MAP:SKIP");
    setBreakChar('|');
  }

  @Override
  public String getFilter() {
    return "administrator@lakecounty911.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("ADDRCITY")) return new MyAddressCityPlaceField();
    return super.getField(name);
  }

  // This class was cloned from ILLakeCountyC

  private static final Pattern APT_PTN = Pattern.compile("\\d+[A-Z]?|[A-Z]");
  private class MyAddressCityPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&').replace('=', ',');
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field.substring(0,pt).trim(), data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field.substring(pt+1).trim(), data);
        if (data.strCity.isEmpty()) {
          data.strCity = getLeft();
        } else {
          data.strPlace = getLeft();
        }
      }
      else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, field, data);
        data.strPlace = getLeft();
      }
      if (!data.strPlace.isEmpty()) {
        if (data.strPlace.equals(data.strApt)) {
          data.strPlace = "";
        }
        else if (APT_PTN.matcher(data.strPlace).matches()) {
          if (!data.strPlace.equals(data.strApt)) {
            data.strApt = append(data.strApt, "-", data.strPlace);
          }
          data.strPlace = "";
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
}
