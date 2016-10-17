package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;



public class COElPasoCountyAParser extends MsgParser {
  
  private static final Pattern JUNK_PTN = Pattern.compile("\\[[_A-Za-z]+\\]\\d+ chars\\.");
  
  private static final Pattern MASTER = 
      Pattern.compile("(?:(FC PMO):|\\[([-A-Z0-9 ]+): *([^\\]]*?)\\]) *([^~]*?)~([^~]+?)~([^#]+?)\\.?#([^~]*?)~([^~]*?)~(?:x:([^~]*?)(?:   +~?|~|$))?(?:ALRM:([\\d ])~)?(?:CMD:([^~]*)~?)?(?:([-A-Z0-9]+))?(?: +[A-Z0-9]+)?(?: +~(\\d{8,9}) +~(\\d{6,9}))? *~*");
  
  private static final Pattern MASTER2 =
      Pattern.compile("INFO from EPSO: (.*?)  (.*?)  (.*?)  (.*?)  +(?:JURIS: )?(.*?)(?:  ALRM: (\\d*))?(?: *CMD:(.*))?");
  
  private static final Pattern MASTER3 = 
      Pattern.compile("([ A-Z]+) - (.*?)  +(.*?) - (.*)");
  
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d");
  
  public COElPasoCountyAParser() {
    super("EL PASO COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "ept@ept911.info";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Square bracket got turned into a subject and needs to be turned back
    if (subject.length() > 0) body = '[' + subject + "] " + body;
    
    // Strip off extra trailer
    body = stripFieldStart(body,  "Txt:");
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // And a new junk construct
    body = JUNK_PTN.matcher(body).replaceAll("");
    
    // Not everyone is using it, but see if this is the new standard dispatch format
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("SRC MAP UNIT CALL ADDR APT PLACE X PRI INFO ID GPS");
      String src = match.group(1);
      if (src != null) {
        data.strSource = src;
      } else {
        data.strSource = match.group(2).trim();
        data.strMap = match.group(3).trim();
      }
      data.strUnit = match.group(4).trim();
      data.strCall = match.group(5).trim();
      String addr = match.group(6).trim();
      addr =stripFieldStart(addr, "0 ");
      parseAddress(addr, data);
      data.strApt = match.group(7).trim();
      data.strPlace = match.group(8).trim();
      data.strCross = getOptGroup(match.group(9));
      data.strPriority = getOptGroup(match.group(10));
      data.strSupp = getOptGroup(match.group(11));
      data.strCallId = getOptGroup(match.group(12));
      String gps1 = match.group(13);
      String gps2 = match.group(14);
      if (gps1 != null && gps2 != null) {
        setGPSLoc(convGPS(gps1)+','+convGPS(gps2), data);
      }
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT PLACE SRC PRI UNIT");
      data.strCall = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strApt = match.group(3).trim();
      data.strPlace = match.group(4).trim();
      data.strSource = match.group(5).trim();
      data.strPriority = getOptGroup(match.group(6));
      data.strUnit = getOptGroup(match.group(7));
      return true;
    }
    
    match = MASTER3.matcher(body + ' ');
    if (match.matches()) {
      setFieldList("SRC CALL ADDR APT INFO NAME PHONE");
      data.strSource = match.group(1);
      data.strCall = match.group(2);
      parseAddress(match.group(3).trim(), data);
      Parser p = new Parser(' ' + match.group(4));
      data.strPhone = p.getLastOptional(" PH ");
      data.strName = p.getLastOptional(" RP ");
      data.strSupp = p.get();
      return true;
    }
    
    FParser p = new FParser(body);
    if (p.check("INFO from EPSO:")) {
      setFieldList("CALL ADDR APT PLACE CITY PRI INFO");
      data.strCall = p.get(30);
      parseAddress(p.get(30), data);
      if (p.checkAhead(105, "JURIS:")) p.skip(50);
      data.strApt = p.get(5);
      data.strPlace = p.get(50);
      String city;
      if (!p.check("JURIS:")) {
        if (!p.check(" ")) return false;
        city = p.get(30);
      } else {
        city = p.get(50);
      }
      if (!city.equals("EPSO Unincorporated Area")) data.strCity = city;
      if (!p.check("ALRM:")) return false;
      if (!p.check("CMD:")) {
        data.strPriority = p.get(1);
        if (!p.check("CMD:")) return false;
      }
      data.strSupp = p.get();
      return true;
    }
    
    if (p.check("From EPSO -")) {
      setFieldList("CALL ADDR APT INFO");
      data.strCall = p.get(24);
      if (!p.check(" ") || p.check(" ")) return false;
      parseAddress(p.get(50), data);
      if (!p.check("  ") || p.check(" ")) return false;
      data.strSupp = p.get();
      return true;
    }
    
    if (p.check("FCFES : ~")) {
      setFieldList("CALL ADDR APT INFO");
      data.strCall = p.get(40);
      if (!p.check("~")) return false;
      parseAddress(p.get(30), data);
      data.strApt = p.get(5);
      if (!p.check("~")) return false;
      data.strSupp = p.get();
      return true;
    }
    
    if (p.check("AD:")) {
      setFieldList("ADDR APT CALL ID TIME INFO");
      parseAddress(p.get(25), data);
      if (!p.check("Unit:")) return false;
      data.strApt = append(data.strApt, "-", p.get(3));
      if (!p.check("RE:")) return false;
      data.strCall = p.get(15);
      if (!p.check("Call#:")) return false;
      data.strCallId = p.get(9);
      if (!p.check(" ")) return false;
      String time = p.get(5);
      if (!TIME_PTN.matcher(time).matches()) return false;
      data.strTime = time;
      data.strSupp = p.get();
      return true;
    }
    
    return false;
  }

  private String convGPS(String gps) {
    int pt = gps.length()-6;
    if (pt < 2) return "";
    return gps.substring(0,pt) + '.' + gps.substring(pt);
  }
}
