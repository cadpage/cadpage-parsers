package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Yolo County, CA (B)
 */
public class CAYoloCountyBParser extends MsgParser {

  public CAYoloCountyBParser() {
    super("YOLO COUNTY", "CA");
    setFieldList("DATE TIME ID CODE CALL PLACE ADDR APT CITY UNIT MAP GPS INFO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "YECA_CAD@yeca911.org,YECA_CAD@yolo911.org,auto@incidentdashboard.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[\\d+\\]");
  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)(?:;(\\d+))? *;");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ALERT")) return false;

    body = stripFieldEnd(body, " [Shared],");

    String[] flds = INFO_BRK_PTN.split(body);
    body = stripFieldEnd(flds[0].trim(), "_");
    for (int jj = 1; jj<flds.length; jj++) {
      String fld = flds[jj].trim();
      data.strSupp = append(data.strSupp, "\n", fld);
    }

    Matcher match = DATE_TIME_ID_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCallId = getOptGroup(match.group(3));
      body = body.substring(match.end());
    }

    FParser p = new FParser(body);
    String call = p.get(30);
    int pt = call.indexOf(' ');
    if (pt >= 0) {
      data.strCode = stripFieldStart(call.substring(0, pt).trim(), "*");
      data.strCall = call.substring(pt+1).trim();
    } else {
      data.strCode = data.strCall = call;
    }

    if (p.check(";")) {

      if (p.checkAhead(30, ";")) {
        data.strPlace = p.get(30);
        if (!p.check(";")) return false;
      }

      parseAddress(p.get(50), data);

      if (!p.check(";")) return false;
      String apt = p.getOptional(";", 5, 6);
      if (apt != null) {
        data.strApt = append(data.strApt, "-", apt);
      }

      p.setOptional();
      data.strCity = p.get(35);

      if (!p.check(";")) return false;
      data.strUnit = p.get(30);

      p.setOptional();
      if (!p.check(";")) return false;
      data.strMap = p.get(5);

      if (!p.check(";")) return false;
      String gps1 = p.get(10);
      if (!p.check(";")) return false;
      String gps2 = p .get(10);
      if (!p.check(";")) return false;
      setGPSLoc(cvtGPS(gps1) + ',' + cvtGPS(gps2), data);

      data.strSupp = append(p.get(), "\n", data.strSupp);
      return true;
    }

    if (p.check(" ")) return false;
    parseAddress(p.get(50), data);

    if (!p.check(" UNITS:")) return false;
    data.strUnit = p.get(30);

    if (!p.check(" Map:")) return false;
    data.strMap = p.get(5);

    if (!p.check(" LAT:")) return false;
    String gps1 = p.get(10);
    if (!p.check(" LONG:")) return false;
    String gps2 = p.get(10);
    setGPSLoc(cvtGPS(gps1) + ',' + cvtGPS(gps2), data);

    data.strSupp = append(p.get(), "\n", data.strSupp);
    return true;
  }

  private String cvtGPS(String field) {
    int pt = field.length()-6;
    if (pt >= 0) field = field.substring(0, pt) + '.' + field.substring(pt);
    return field;
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "36530 CR 21",                          "+38.685510,-121.834450"
  });
}
