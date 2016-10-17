package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Washington County, OR (Variant C)
 * Also Clackamas County
 */
public class ORWashingtonCountyCParser extends MsgParser {
  
  public ORWashingtonCountyCParser() {
    this("WASHINGTON COUNTY", "OR");
  }
  
  public ORWashingtonCountyCParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "ORWashingtonCountyCParser";
  }
  
  @Override
  public String getFilter() {
    return "portlandcomm@amr-ems.com";
  }
  
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(.*)\\bUNIT: *([^ ]+) +INC#: *(\\d+) +((?:RCD|CLR):.*)");
  private static final Pattern RR_BRK_PTN = Pattern.compile(" +(?=[A-Z]+:)");
  private static final Pattern GEN_UNIT_PTN = Pattern.compile("UNIT[ :#]+(\\S+) +(.*)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("VisiCAD Email")) return false;
    
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = match.group(1).trim();
      data.strUnit = match.group(2);
      data.strCallId = match.group(3);
      data.strSupp = RR_BRK_PTN.matcher(match.group(4)).replaceAll("\n");
      return true;
    }

    if (parseMsgFmt1(body, data)) return true;
    
    data.initialize(this);
    if (parseMsgFmt2(body, data)) return true;

    setFieldList("UNIT INFO");
    data.initialize(this);
    data.msgType = MsgType.GEN_ALERT;
    match = GEN_UNIT_PTN.matcher(body);
    if (match.matches()) {
      data.strUnit = match.group(1);
      body = match.group(2);
    }
    data.strSupp = body;
    return true;
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) +([^ ].*)");
  
  private boolean parseMsgFmt1(String body, Data data) {
    int pt = body.indexOf("UNIT:");
    if (pt > 0 && pt <= 13) {
      data.strCall = body.substring(0,pt).trim();
      body = body.substring(pt);
    }
    FParser p = new FParser(body);
    if (!p.check("UNIT: "))  return false;
    data.strUnit = p.get(9);
    if (!p.check(" INC#: ")) return false;
    data.strCallId = p.get(9);
    if (!p.check(" ")) return false;
    data.strPlace = p.get(33);
    if (p.check(" ")) return false;
    String addr = p.get(31);
    pt = addr.indexOf('[');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", addr.substring(pt+1).trim());
      addr = addr.substring(0,pt).trim();
    }
    parseAddress(addr, data);
    data.strApt= p.get(11);
    if (p.check(" ")) return false;
    data.strCity = convertCodes(p.get(18), CITY_CODES);
    if (!p.check(" ZIP: ")) return false;
    p.skip(11);
    p.check(" XST: ");
    data.strCross = stripFieldStart(p.get(42), "btwn ");
    if (!p.check("PRI:")) return false;
    data.strPriority = p.get(2);
    if (!p.check(" ") || p.check(" ")) return false;
    String call = p.get();
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = append(data.strCall, " - ", call);
    
    setFieldList("UNIT ID PLACE ADDR APT CITY X PRI CODE CALL");
    return true;
  }
  
  private boolean parseMsgFmt2(String body, Data data) {
    int pt = body.indexOf("UNIT:");
    if (pt > 0 && pt <= 13) {
      data.strCall = body.substring(0,pt).trim();
      body = body.substring(pt);
    }
    FParser p = new FParser(body);
    if (!p.check("UNIT:"))  return false;
    data.strUnit = p.get(7);
    if (!p.check(" INC#:")) return false;
    data.strCallId = p.get(7);
    if (!p.check(" ")) return false;
    data.strPlace = p.get(30);
    if (p.check(" ")) return false;
    String addr = p.get(28);
    pt = addr.indexOf('[');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", addr.substring(pt+1).trim());
      addr = addr.substring(0,pt).trim();
    }
    parseAddress(addr, data);
    data.strApt= p.get(10);
    if (p.check(" ")) return false;
    data.strCity = convertCodes(p.get(17), CITY_CODES);
    if (!p.check(" ZIP:")) return false;
    p.skip(9);
    p.check(" XST:");
    data.strCross = p.get(40);
    if (!p.check("PRI:")) return false;
    data.strPriority = p.get(2);
    p.check(" ");
    if (p.check(" ")) return false;
    String call = p.get();
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = append(data.strCall, " - ", call);
    
    setFieldList("UNIT ID PLACE ADDR APT CITY X PRI CODE CALL");
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FRVW", "FAIRVIEW",
      "GRSM", "GRESHAM",
      "MULT", "MULTNOMAH COUNTY",
      "PORT", "PORTLAND",
      "TRO",  "TROUTDALE",
      "WVLG", "WOOD VILLAGE",

  });
}
