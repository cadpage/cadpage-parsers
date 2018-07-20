package net.anei.cadpage.parsers.CA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Yolo County, CA (B)
 */
public class CAYoloCountyBParser extends MsgParser {
  
  public CAYoloCountyBParser() {
    super("YOLO COUNTY", "CA");
    setFieldList("CODE CALL PLACE ADDR APT SRC UNIT MAP GPS INFO");
  }
  
  @Override
  public String getFilter() {
    return "YECA_CAD@yeca911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[\\d+\\]");
  
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
      data.strSource = p.get(35);
      
      if (!p.check(";")) return false;
      data.strUnit = p.get(30);
      
      if (!p.check(";")) return false;
      data.strMap = p.get(5);
      
      p.setOptional();
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
}
