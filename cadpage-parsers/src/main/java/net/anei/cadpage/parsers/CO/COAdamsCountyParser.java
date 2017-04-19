package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class COAdamsCountyParser extends MsgParser {
  public COAdamsCountyParser() {
    super("ADAMS COUNTY", "CO");
    setFieldList("UNIT CALL ADDR APT CITY ID GPS CH INFO");
  }
  
  @Override
  public String getFilter() {
    return "tritechpaging@adcom911.org";
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[\\d{1,2}\\]| *\\[Shared\\]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    FParser fp = new FParser(body);
    if (fp.check("***NOTIFICATION***")) {
      if (!fp.check("UNIT:")) return false;
      data.strUnit = fp.get(30);
      if (!fp.check("PROBLEM:")) return false;
      data.strCall = fp.get(26);
      if (!fp.check("ADDRESS:")) return false;
      parseAddress(fp.get(30), data);
      if (!fp.check("APT/BLDG#:")) return false;
      data.strApt = append(data.strApt, "-", fp.get(8));
      if (!fp.check("CITY:")) return false;
      data.strCity = fixCity(fp.get(25));
      if (!fp.check("ZIP:")) return  false;
      String zip = fp.get(5);
      if (data.strCity.length() == 0) data.strCity = zip;
      if (!fp.check("  INC#:")) return false;
      data.strCallId = fp.get(20);
      if (!fp.check("LATITUDE:")) return false;
      String gps1 = fp.get(8);
      if (!fp.check("  LONGITUTDE:")) return false;
      String gps2 = fp.get(9);
      setGPSLoc(fixCoord(gps1)+','+fixCoord(gps2), data);
      if (!fp.check(" RADIO CHANNEL:")) return false;
      data.strChannel = fp.get(7);
      fp.setOptional();
      fp.check("   ");
      if (!fp.check("COMMENTS")) return false;
      String info = fp.get();
      data.strSupp = INFO_JUNK_PTN.matcher(info).replaceAll("");
      return true;
    }
    if (!fp.check("UNIT: ")) return false;
    data.strUnit = fp.get(6);
    if (!fp.check("PROBLEM: ")) return false;
    data.strCall = fp.get(26);
    if (!fp.check("ADDRESS: ")) return false;
    parseAddress(fp.get(30), data);
    if (!fp.check("APT/BLDG#: ")) return false;
    data.strApt = append(data.strApt, "-", fp.get(8));
    if (!fp.check("CITY: ")) return false;
    data.strCity = fixCity(fp.get(25));
    if (!fp.check("ZIP: ")) return  false;
    String zip = fp.get(5);
    if (data.strCity.length() == 0) data.strCity = zip;
    if (!fp.check("  INC#: ")) return false;
    data.strCallId = fp.get(20);
    if (!fp.check("LATITUDE: ")) return false;
    String gps1 = fp.get(9);
    if (!fp.check("  LONGITUDE: ")) return false;
    String gps2 = fp.get(9);
    setGPSLoc(fixCoord(gps1)+','+fixCoord(gps2), data);
    if (!fp.check(" RADIO CHANNEL: ")) return false;
    data.strChannel = fp.get(7);
    fp.setOptional();
    if (!fp.check("COMMENTS")) return false;
    String info = fp.get();
    data.strSupp = INFO_JUNK_PTN.matcher(info).replaceAll("");
    return true;
  }
  
  private String fixCity(String city) {
    city = stripFieldStart(city, "UNICORPORATED");
    if (city.endsWith(" COUNT")) city += "Y";
    return city;
  }
  
  private String fixCoord(String coord) {
    int pt = coord.length()-6;
    if (pt >= 0)  coord = coord.substring(0, pt) + '.' + coord.substring(pt);
    return coord;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return TWO_PT_FIVE.matcher(addr).replaceAll("2 1/2");
  }
  private Pattern TWO_PT_FIVE = Pattern.compile("\\b2\\.5\\b");
  
}
