package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class CAContraCostaCountyCParser extends MsgParser {
  
  public CAContraCostaCountyCParser() {
    super("CONTRA COSTA COUNTY", "CA");
    setFieldList("CALL PRI ADDR APT X PLACE MAP GPS SRC UNIT");
  }
  
  @Override
  public String getFilter() {
    return "srvfire@dapage.net,srvfire@srvfire.dapage.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD_ACTIVE911")) return false;
    
    body = stripFieldStart(body, "-Type: ");
    body = stripFieldEnd(body, "/TT CAD");
    FParser fp = new FParser(body);
    if (!fp.check("***CAD***")) return false;
    data.strCall = fp.get(30);
    if (!fp.check("-- Alarm:")) return false;
    data.strPriority = fp.get(1);
    if (!fp.check("-- Loc:")) return false;
    parseAddress(fp.get(25), data);
    if (!fp.check("-- Bldg:")) return false;
    data.strApt = append(data.strApt, "-", fp.get(5));
    if (!fp.check("-- Apt:")) return false;
    data.strApt = append(data.strApt, "-", fp.get(5));
    if (!fp.check("-- X-St:")) return false;
    data.strCross = fp.get(30);
    if (!fp.check(", Place:")) return false;
    data.strPlace = fp.get(20);
    if (!fp.check("-- Map:")) return false;
    data.strMap = fp.get(10);
    if (!fp.check("--Coord--")) return false;
    data.strMap = append(data.strMap, " / ", fp.get(30));
    if (!fp.check(", Lat:")) return false;
    String gps1 = fp.get(9);
    if (!fp.check(" Long:")) return false;
    String gps2 = fp.get(9);
    setGPSLoc(cvtGPS(gps1)+','+cvtGPS(gps2), data);
    if (!fp.check(" -- Station:")) return false;
    data.strSource = fp.get(30);
    if (!fp.check("-- Units:")) return false;
    data.strUnit = fp.get();
    return true;
  }

  private String cvtGPS(String gps) {
    int pt = gps.length()-6;
    if (pt < 1) return "";
    return gps.substring(0,pt) + '.' + gps.substring(pt);
  }
}
