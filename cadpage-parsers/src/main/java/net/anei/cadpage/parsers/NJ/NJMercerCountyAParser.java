
























package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;


/**
 * Mercer County, NJ
 */
public class NJMercerCountyAParser extends DispatchA24Parser {

  public NJMercerCountyAParser() {
    super("MERCER COUNTY", "NJ");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "noreply_lifecomm@verizon.net,central@mercercounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf("FOOTER:*");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Some odd variants
    if (body.endsWith(" (Sent by Central 609-799-0110)")) {
      setFieldList("CALL ADDR APT CITY");
      body = body.substring(0, body.length()-30).trim();
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, body, data);
      return data.strAddress.length() > 0;
    }

    if (subject.equals("Sta. 52/Sq. 152")) {
      setFieldList("CALL PLACE ADDR APT CITY");
      String[] flds = body.split("  ");
      if (flds.length == 3) {
        data.strCall = flds[0].trim();
        data.strPlace = flds[1].trim();
        parseAddress(flds[2].trim(), data);
        return true;
      }
      if (flds.length == 2) {
        data.strCall = flds[0].trim();
        parseAddress(flds[1].trim(), data);
        return true;
      }
      return false;
    }

    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("^Longitude: ([+-]\\d+\\.\\d+),Latitude: ([+-]\\d+\\.\\d+),");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1) + ',' + match.group(2), data);
        field = field.substring(match.end()).trim();
      }

      if (field.startsWith("RADIO:")) {
        field = field.substring(6).trim();
        int pt = field.indexOf("REMARKS:");
        if (pt >= 0) {
          data.strSupp = append(data.strSupp, "\n", field.substring(pt+8).trim());
          field = field.substring(0,pt).trim();
        }
        field = stripFieldEnd(field, ":");
        data.strChannel = field;
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH GPS INFO";
    }
  }

  private static final String[] CITY_LIST = new String[]{
    "EAST WINDSOR",
    "EWING",
    "HAMILTON",
    "HIGHTSTOWN",
    "HOPEWELL",
    "LAWRENCE",
    "LPENNINGTON",
    "PRINCETON",
    "ROBBINSVILLE",
    "TRENTON",
    "WEST WINDSOR"
  };
}
