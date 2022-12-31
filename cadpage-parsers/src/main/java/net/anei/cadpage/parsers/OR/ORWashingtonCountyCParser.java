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
    return "portlandcomm@amr-ems.com,portlandcomm@gmr.net";
  }


  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(.*)\\bUNIT: *(\\S+) +INC#: *(\\d+)[ .]+((?:DSP|RCD|CLR):.*)");
  private static final Pattern RR_BRK_PTN = Pattern.compile("[ .]+(?=[A-Z]+:)");
  private static final Pattern TEMP_PAGE_PTN = Pattern.compile("(.*?) \\.{3}Run:(\\S+) \\.{3}add:(.*)");
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

    if (parseMsgFmt2(body, data)) return true;

    if (parseMsgFmt3(body, data)) return true;

    if (parseMsgFmt4(body, data)) return true;

    match = TEMP_PAGE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ID ADDR APT");
      data.strCall = match.group(1).trim();
      data.strCallId =  match.group(2);
      parseAddress(match.group(3).trim(), data);
      return true;
    }

    setFieldList("UNIT INFO");
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

  private boolean parseMsgFmt2(String body, Data data) {
    String prefix = "";
    int pt = body.indexOf("UNIT:");
    if (pt > 0 && pt <= 13) {
      prefix = body.substring(0,pt).trim();
      body = body.substring(pt);
    }
    FParser p = new FParser(body);
    if (!p.check("UNIT:"))  return false;
    String unit = p.get(7);
    if (!p.check(" INC#:")) return false;
    String id = p.get(7);
    if (!p.check(" ")) return false;
    String place = p.get(30);
    if (p.check(" ")) return false;
    String addr = p.get(28);
    String apt = p.get(10);
    if (p.check(" ")) return false;
    String city = p.get(17);
    if (!p.check(" ZIP:")) return false;
    p.skip(9);
    p.check(" XST:");
    String cross = p.get(40);
    if (!p.check("PRI:")) return false;
    String pri = p.get(2);
    p.check(" ");
    if (p.check(" ")) return false;
    String call = p.get();

    setFieldList("CALL UNIT ID PLACE ADDR APT CITY X PRI CODE CALL");
    data.strCall = prefix;
    data.strUnit = unit;
    data.strCallId = id;
    data.strPlace = place;
    pt = addr.indexOf('[');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", addr.substring(pt+1).trim());
      addr = addr.substring(0,pt).trim();
    }
    parseAddress(addr, data);
    if (!data.strApt.contains(apt)) {
      data.strApt = append(data.strApt, "-", apt);
    }
    data.strCity = convertCodes(city, CITY_CODES);
    data.strCross = cross;
    data.strPriority = pri;
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = append(data.strCall, " - ", call);

    return true;
  }

  private boolean parseMsgFmt3(String body, Data data) {
    String prefix = "";
    int pt = body.indexOf("UNIT:");
    if (pt > 0 && pt <= 20) {
      prefix = body.substring(0,pt).trim();
      body = body.substring(pt);
    }
    FParser p = new FParser(body);
    if (!p.check("UNIT:"))  return false;
    String unit = p.get(5);
    if (!p.check("INC#:")) return false;
    String id = p.get(8);
    if (!p.check(" ")) return false;
    String place = p.get(30);
    String addr = p.get(30);
    String apt = p.get();

    setFieldList("UNIT ID PLACE ADDR APT");
    data.strCall = prefix;
    data.strUnit = unit;
    data.strCallId = id;
    data.strPlace = place;
    pt = addr.indexOf('[');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", addr.substring(pt+1).trim());
      addr = addr.substring(0,pt).trim();
    }
    parseAddress(addr, data);
    data.strApt = append(data.strApt, "-", apt);
    return true;
  }

  private boolean parseMsgFmt4(String body, Data data) {
    FParser p = new FParser(body);
    String unit = p.get(6);
    if (!p.check("INC#")) return false;
    String id = p.get(7);
    if (!p.check(" ADDY:")) return false;
    String addr = p.get(30);
    if (!p.check(" #")) return false;
    String apt = p.get(10);
    if (!p.check(" BLDG:")) return false;
    apt = append(p.get(10), "-", apt);
    if (!p.check(" CITY:")) return false;
    String city = p.get(16);
    if (!p.check(" LOC NAME:")) return false;
    String place = p.get(30);
    if (!p.check(" XST:")) return false;
    String cross = p.get(30);
    if (!p.check(" TYPE:")) return false;
    String call = p.get(15);
    if (!p.check(" PRI:")) return false;
    String pri = p.get(29);
    String info = p.get();

    setFieldList("UNIT ID ADDR APT CITY PLACE X CALL PRI INFO");
    data.strUnit = unit;
    data.strCallId = id;
    parseAddress(addr, data);
    data.strApt = append(data.strApt, "-", apt);
    data.strCity = city;
    data.strPlace = place;
    data.strCross = cross;
    data.strCall = call;
    data.strPriority = pri;
    data.strSupp = info;
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
