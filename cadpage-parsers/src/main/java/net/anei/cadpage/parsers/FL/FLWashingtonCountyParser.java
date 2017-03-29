package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class FLWashingtonCountyParser extends SmartAddressParser {

  public FLWashingtonCountyParser() {
    super("WASHINGTON COUNTY", "FL");
    setFieldList("ADDR APT PLACE X INFO CALL NAME");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@exchangewcso.us,@wcso.us";
  }

  private static Pattern MASTER = Pattern.compile("(.*?)((?<=[A-Z])\\d+)?\n(.*?)(?:X2\\[(.*?)\\])?\n(Y)?\n\n(.*?)(?:\n(.*))?", Pattern.DOTALL);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check for alternate format first
    if (subject.equals("=?utf-8?B?NDEx?=")) return false;

    //check subject
    if (!subject.startsWith("=?")) return false;
    if (!subject.endsWith("?=")) return false;

    //parse with RegEx
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;

    String addr = mat.group(1).trim();
    parseAddress(addr, data);
    data.strApt = append(data.strApt, "-", getOptGroup(mat.group(2)));
    String place = mat.group(3).trim();
    if (!place.equals(addr)) data.strPlace = place;
    data.strCross = getOptGroup(mat.group(4)).trim();
    data.strSupp = getOptGroup(mat.group(5));
    data.strCall = mat.group(6).trim();
    data.strName = getOptGroup(mat.group(7)).trim();

    return true;
  }
}
