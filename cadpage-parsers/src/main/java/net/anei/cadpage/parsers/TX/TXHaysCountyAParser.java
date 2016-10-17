package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class TXHaysCountyAParser extends MsgParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Alarm - (.*?) - (.*)");
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+) RESPOND TO (.*?) (?:\\[(.*) )?FOR (.*?) DET CODE: *(.*?)(?: APPARATUS: (.*?))?");
  
  public TXHaysCountyAParser() {
    super("HAYS COUNTY", "TX");
    setFieldList("UNIT SRC ADDR APT CITY CALL CODE");
  }
  
  public String getFilter() {
    return "dispatch@smhcems.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) {
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (!match.matches()) return false;
      data.strUnit = match.group(1).trim();
      data.strCall = match.group(2).trim();
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    data.strCity = getOptGroup(match.group(3));
    data.strCall = match.group(4).trim();
    data.strCode = match.group(5).trim();
    if (data.strCode.length() < 2) data.strCode = "";
    String unit = match.group(6);
    if (unit != null) data.strUnit = unit;
    return true;
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return RR_PTN.matcher(sAddress).replaceAll("RT");
  }
  private static final Pattern RR_PTN = Pattern.compile("\\bRR\\b");
}
