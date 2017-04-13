package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAFranklinCountyBParser extends MsgParser {
  
  public PAFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "cadpaging@franklincountypa.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Paging")) return false;
    
    FParser fp = new FParser(body);
    String call = fp.get(30);
    String addr = fp.get(30);
    if (addr.length() == 0) {
      setFieldList("ADDR CITY X APT CALL MAP DATE TIME GPS INFO");
      parseAddress(call, data);
      if (!fp.checkBlanks(360)) return false;
      data.strCity = fixCity(fp.get(35));
      data.strCross = fp.get(30);
      if (!fp.checkBlanks(370)) return false;
      data.strApt = fp.get(10);
      if (fp.check(" ")) return false;
      data.strCall = fp.get(30);
      data.strMap = fp.get(30);
      if (!parseDateTime(fp, data)) return false;
      if (!parseGPS(fp, data)) return false;
      data.strSupp = fp.get();
      return true;
    }

    setFieldList("CALL ADDR APT CITY X GPS MAP UNIT DATE TIME INFO");
    data.strCall = call;
    parseAddress(addr, data);
    if (!fp.checkBlanks(370)) return false;
    data.strApt = append(fp.get(10), "-", fp.get(10));
    data.strCity = fixCity(fp.get(35));
    
    String zip = fp.lookahead(0, 10);
    if (ZIP_PTN.matcher(zip).matches()) {
      if (data.strCity.length() == 0) data.strCity = zip;
      fp.skip(10);
    } else {
      zip = null;
    }
    data.strCross = fp.get(30);
    if (!fp.checkBlanks(370)) return false;
    if (!parseGPS(fp, data)) return false;
    if (!fp.check("  ")) return false;
    fp.checkBlanks(10);
    data.strMap = fp.get(30);

    if (!parseDateTime(fp, data)) data.strUnit = fp.get(10);
    data.strSupp = fp.get();
    return true;
  }
  
  private String fixCity(String city) {
    city = stripFieldEnd(city, " BORO");
    city = stripFieldEnd(city, " Boro");
    return city;
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");
  private boolean parseDateTime(FParser fp, Data data) {
    String dateTime = fp.lookahead(0, 19);
    Matcher match = DATE_TIME_PTN.matcher(dateTime);
    if (!match.matches()) return false;
    fp.skip(19);
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    return true;
  }
  
  private boolean parseGPS(FParser fp, Data data) {
    String gps1 = fp.get(8);
    if (!fp.check("  ")) return false;
    String gps2 = fp.get(8);
    if (gps1.length() == 0 && gps2.length() == 0) return true;
    gps1 = cvtGps(gps1);
    gps2 = cvtGps(gps2);
    if (gps1 == null || gps2 == null) return false;
    setGPSLoc(gps1+','+gps2, data);
    return true;
  }
  
  private static final Pattern GPS_COORD_PTN = Pattern.compile("(\\d\\d)(\\d{6})");
  private String cvtGps(String coord) {
    Matcher match = GPS_COORD_PTN.matcher(coord);
    if (!match.matches()) return null;
    return match.group(1)+'.'+match.group(2);
  }
}
