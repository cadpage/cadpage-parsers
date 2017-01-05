package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class NJHunterdonCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("(?:([A-Z0-9]+):)?([-A-Z /0-9]+): *(?:\\((\\d+)\\)|([A-Z ]+)) *(.*?) (?:NEAR: *(.*?) )?(\\d{8})\\b[: ]*(.*?)");
  
  public NJHunterdonCountyParser() {
    super("HUNTERDON COUNTY", "NJ");
    setFieldList("SRC UNIT CALL CITY PLACE ADDR APT X ID INFO");
  }
  
  @Override
  public String getFilter() {
    return "hces@hunterdon.co.nj.us,messaging@iamresponding.com,7127390583";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (body.startsWith("Fwd:")) {
      body = body.substring(4).trim();
      if (subject.length() == 0 && body.startsWith("(")) {
        int pt = body.indexOf(')', 1);
        if (pt >= 0) {
          subject = body.substring(1,pt).trim();
          body = body.substring(pt+1).trim();
        }
      }
    }
    
    data.strSource = subject;
    
    body = body.replace('\n', ':');
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = getOptGroup(match.group(1));
    data.strCall = match.group(2);
    String code = match.group(3);
    if (code != null) {
      data.strCity = convertCodes(code, CITY_CODES);
    } else {
      data.strCity = match.group(4);
    }
    Parser p = new Parser(match.group(5));
    data.strPlace = p.getOptional(" / ");
    parseAddress(p.get().replace(" NO ", " "), data);
    data.strCross = getOptGroup(match.group(6));
    data.strCallId = match.group(7);
    data.strSupp = match.group(8);
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "2",  "WASHINGTON TWP",
      "11", "FRENCHTOWN",
      "12", "GLEN GARDNER",
      "13", "HAMPTON",
      "14", "HIGH BRIDGE",
      "15", "HOLLAND TWP",
      "16", "KINGWOOD",
      "17", "LAMBERTVILLE",
      "18", "LEBANON",
      "19", "LEBANON TWP",
      "21", "RARITAN TWP",
      "22", "READINGTON TWP",
      "23", "STOCKTON",
      "24", "TEWKSBURY",
      "25", "PATTENBURG",
      "26", "WEST AMWELL TWP",
      "31", "EAST WHITEHOUSE",
      "32", "READINGTON",
      "33", "THREE BRIDGES",
      "34", "FAIRMOUNT",
      "41", "QUAKERTOWN",
      "42", "FRANKLIN TWP",
      "43", "BLOOMSBURY",
      "44", "CALIFON",
      "45", "CLINTON",
      "46", "ANNANDALE",
      "47", "SERGEANTSVILLE",
      "48", "AMWELL VALLEY",
      "49", "FLEMINGTON",
      "52", "ASBURY",
      "57", "POTTERSVILLE",
      "74", "BRANCHBURG",
      "79", "HILLSBOROUGH",
      "91", "QUAKERTOWN",
      "92", "MILFORD",
      "93", "COUNTRY HILLS",
      "94", "DELAWARE VALLEY",
      "95", "UPPER BLACK EDDY",
      "96", "NEW HOPE EAGLE"

  });
}
