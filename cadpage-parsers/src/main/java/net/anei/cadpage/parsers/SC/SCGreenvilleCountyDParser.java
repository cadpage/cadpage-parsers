package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class SCGreenvilleCountyDParser extends MsgParser {
  
  public SCGreenvilleCountyDParser() {
    super("GREENVILLE COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "InformCADPaging@Greenvillecounty.org";
  }
  
  private static final Pattern APT_PTN = Pattern.compile("\\d{1,5}[A-Z]?|[A-Z]");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    FParser fp = new FParser(body);
    
    int priPos = findPriorityAt(fp, 85, 115);
    if (priPos >= 0) {
      setFieldList("CALL CITY ADDR APT PLACE PRI ID X GPS INFO");
      int pad = priPos == 115 ? 10 : 0;
      data.strCall = fp.get(20+pad);
      data.strCity = stripFieldStart(fp.get(19), "(C)");
      if (!fp.check(" ") || fp.check(" ")) return false;
      parseAddress(fp.get(30+pad), data);
      parseAptPlace(fp.get(15+pad), data);;
      if (fp.check(" ")) return false;
      data.strPriority = fp.get(24);
      if (!fp.check(" ") || fp.check(" ")) return false;
      data.strCallId = fp.get(19);
      if (!fp.check(" ")) return false;
      data.strCross = fp.get(40);
      if (!fp.check("[1] ")) return false;
      parseInfo(fp.get(), data);
      return true;
    }
    
    String call = fp.getOptional("@", 20);
    if (call != null) {
      data.strCall = call;
      setFieldList("CALL ADDR UNIT");
      parseAddress(fp.get(25), data);
      if (!fp.check("UNIT ASSIGNED-")) return false;
      data.strUnit = fp.get();
      return true;
    }
    
    data.strCall = fp.get(30);
    if (fp.check("@")) {
      setFieldList("CALL ADDR UNIT");
      parseAddress(fp.get(30), data);
      if (!fp.checkBlanks(370)) return false;
      if (!fp.check("UNIT ASSIGNED-"))  return false;
      data.strUnit = fp.get();
      return true;
      
    }
    setFieldList("CALL ADDR APT CITY PLACE GPS INFO PRI ID X UNIT");
    if (fp.check(" ")) return false;
    parseAddress(fp.get(100), data);
    if (!fp.checkAhead(0, " ")) {
      data.strCity = stripFieldStart(fp.get(34), "(C)");
      if (!fp.check(" ")) return false;
      parseAptPlace(fp.get(100), data);
      if (fp.atEnd()) return true;
      if (!fp.check("[1] ")) return false;
      if (checkForPriorityAt(fp, 496)) {
        parseInfo(fp.get(496), data);
      } else {
        parseInfo(fp.get(4092), data);
        if (fp.check(" ")) return false;
      }
      data.strPriority = fp.get(30);
      if (fp.check(" ")) return false;
      data.strCallId =  fp.get(20);
      data.strCross = fp.get();
      return true;
    }
    if (!fp.checkBlanks(150)) return false;  
    if (!fp.checkAhead(0, " ")) {
      data.strCity = stripFieldStart(fp.get(34), "(C)");
      if (!fp.check(" ")) return false;
      parseAptPlace(fp.get(400), data);
      if (!fp.check("[1] ")) return false;
      parseInfo(fp.get(4092), data);
      if (fp.checkAhead(0, " ") && !fp.checkBlanks(3904)) return false;
      if (fp.check(" ")) return false;
      data.strPriority = fp.get(30);
      if (fp.check(" ")) return false;
      data.strCallId =  fp.get(20);
      data.strCross = fp.get();
      return true;
    }
    if (!fp.checkBlanks(150)) return false;
    data.strCity = stripFieldStart(fp.get(34), "(C)");
    if (!fp.check(" ")) return false;
    parseAptPlace(fp.get(400), data);
    if (!fp.check("[1] ")) return false;
    parseInfo(fp.get(4092), data);
    if (fp.check(" ")) return false;
    data.strCallId = fp.get(20);
    if (fp.check(" ")) return false;
    data.strPriority = fp.get(30);
    return true;
  }
  
  private int findPriorityAt(FParser fp, int ... pos) {
    for (int p : pos) {
      if (checkForPriorityAt(fp, p)) return p;
    }
    return -1;
  }
  
  private boolean checkForPriorityAt(FParser fp, int pos) {
    String priority = fp.lookahead(pos, 20);
    return (priority.equals("Emergency") || priority.equals("Non-Emergency"));

  }
  
  private void parseAptPlace(String field, Data data) {
    if (field.length() == 0) return;
    if (APT_PTN.matcher(field).matches()) {
      data.strApt = append(data.strApt, "-", field);
    } else {
      data.strPlace = field;
    }
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[\\d+\\]");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("\\bLAT: *([-+]?\\d{2,3}\\.\\d{6,}) +LON: *([-+]?\\d{2,3}\\.\\d{6,})\\b");
  private void parseInfo(String field, Data data) {
    for (String part : INFO_BRK_PTN.split(field)) {
      part = part.trim();
      part = stripFieldEnd(part, ",");
      part = stripFieldEnd(part, "[shared]");
      Matcher match = INFO_GPS_PTN.matcher(part);
      if (match.find()) {
        setGPSLoc(match.group(1) + ',' + match.group(2), data);
        continue;
      }
      if (part.startsWith("***Class of Serivce:")) continue;
      if (part.startsWith("***ADD'L Wire")) continue;
      if (part.startsWith("A cellular re-bid")) continue;
      if (part.startsWith("Automatic Case Number(s)")) continue;
      
      data.strSupp = append(data.strSupp, "\n", part);
    }
    
  }
}
