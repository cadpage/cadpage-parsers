package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class PAMcKeanCountyParser extends MsgParser {
  
  public PAMcKeanCountyParser() {
    super("MCKEAN COUNTY", "PA");
    setFieldList("UNIT CALL SRC CITY ADDR APT");
  }
  
  @Override
  public String getFilter() {
    return "alerts@mckeancounty.ealertgov.com";
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("[_A-Z0-9]+");
  private static final Pattern MASTER = Pattern.compile("([ A-Z]+) ([A-Z]+) \\(\\) Loc:(.*)");
  
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!UNIT_PTN.matcher(subject).matches()) return false;
    data.strUnit = subject;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strSource = match.group(2).trim();
    parseAddress(match.group(3).trim(), data);
    
    String city =  SRC_CITY_TABLE.getProperty(data.strSource);
    if (city != null) data.strCity = city;
    return true;
  }
  
  private static final Properties SRC_CITY_TABLE = buildCodeTable(new String[]{
      "ELDAMB",   "ELDRED",
      "PAAS",     "PORT ALLEGANY"
  });
}
