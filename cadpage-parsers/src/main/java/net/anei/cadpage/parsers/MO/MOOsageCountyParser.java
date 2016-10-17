package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOOsageCountyParser extends DispatchGlobalDispatchParser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?: +(?:[A-Z]+\\d+|MOAD|MSHP|RDB|RDC|RDL|RDL \\d|RDW|STAFF|7[01]\\d|8\\d{2}|61\\d{2}|[A-Z]{1,4}FD))+$");
  private static final Pattern DASH_DIR_PTN = Pattern.compile("- *([NSEW]|[NS][EW])\\b");
  private static final Pattern CROSS_UNIT_PTN = Pattern.compile("(.* (?:[NSEW]|[NS][EW]))\\b *(.*)");
  
  public MOOsageCountyParser() {
    super(CITY_LIST, "OSAGE COUNTY", "MO", PLACE_FOLLOWS_ADDR);
    setupMultiWordStreets(
        "MARTINS BLUFF",
        "ROLLINS FERRY"
    );
    setAllowDirectionHwyNames();
  }
  
  @Override
  public String getFilter() {
    return "sms911@socket.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    subject = subject.trim();
    if (!subject.equals("OsageCo 911 EOC") && !subject.equals("OsageCounty 911/EOC")) return false;
    if (!body.contains(" CrossStreets:")) {
      setFieldList("ADDR APT CITY CALL");
      
      Matcher match = UNIT_PTN.matcher(body);
      if (!match.find()) return false;
      data.strUnit = match.group().trim();
      body = body.substring(0,match.start());

      int pt = body.indexOf("  ");
      if (pt >= 0) {
        data.strCall = body.substring(pt+2).trim();
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, body.substring(0,pt), data);
      } else {
        parseAddress(StartType.START_ADDR, body, data);
        data.strCall = getLeft();
      }
      if (data.strCity.equals("OSAGE COUNTY")) data.strCity = "";
      return true;
    }
    
    body = DASH_DIR_PTN.matcher(body).replaceAll("$1");
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("OSAGE COUNTY")) data.strCity = "";
    
    // Split off unit info from cross street
    Matcher match = CROSS_UNIT_PTN.matcher(data.strCross);
    String sUnit;
    if (match.matches()) {
      data.strCross = match.group(1);
      sUnit = match.group(2);
    } else {
      sUnit = data.strCross;
      data.strCross = "";
    }
    data.strUnit = append(data.strUnit, " ", sUnit);
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ARGYLE",
    "BELLE",
    "BONNOTS MILL",
    "CHAMOIS",
    "FOLK",
    "FRANKENSTEIN",
    "FREEBURG",
    "KOELTZTOWN",
    "LINN",
    "LOOSE CREEK",
    "META",
    "RICH FOUNTAIN",
    "WESTPHALIA",
    
    "OSAGE COUNTY"
  };
}
