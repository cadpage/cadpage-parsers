package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Yolo County, CA (B)
 */
public class CAYoloCountyBParser extends MsgParser {
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[\\d+\\]");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("\\bLAT:? +([-+]?[\\.\\d]+) +LONG:? +([-+]?[\\.\\d]+),?");
  
  public CAYoloCountyBParser() {
    super("YOLO COUNTY", "CA");
    setFieldList("CALL ADDR APT UNIT MAP INFO GPS");
  }
  
  @Override
  public String getFilter() {
    return "YECA_CAD@yeca911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ALERT")) return false;
    
    body = stripFieldEnd(body, " [Shared],");
    
    String[] flds = INFO_BRK_PTN.split(body);
    body = stripFieldEnd(flds[0].trim(), "_");
    for (int jj = 1; jj<flds.length; jj++) {
      String fld = flds[jj].trim();
      Matcher match = INFO_GPS_PTN.matcher(fld);
      if (match.find()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        fld = append(fld.substring(0,match.start()).trim(), " ", fld.substring(match.end()).trim());
      }
      data.strSupp = append(data.strSupp, "\n", fld);
    }
    
    FParser p = new FParser(body);
    
    if (p.check("ESP  .")) {
      data.strCall = p.get(30);
      if (!p.check(".at.")) return false;
      parseAddress(p.get(25), data);
      if (!p.check("#")) return false;
      data.strApt = append(data.strApt, "-", p.get(4));
      return p.check(".");
    }
    
    p.check("_");
    data.strCall = p.get(30);
    p.check("_at_");
    parseAddress(p.get(50), data);
    p.setOptional();
    p.check("_");
    if (p.check("UNITS:")) {
      String unit = p.get();
      int pt = unit.indexOf("MAP:");
      if (pt >= 0) {
        data.strMap = unit.substring(pt+4).trim();
        unit = unit.substring(0,pt).trim();
      }
      data.strUnit = unit;
    } else if (p.check("#")) {
      data.strApt = p.get();
    } else return false;
    return true;
  }
}
