package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAMasonCountyParser extends DispatchA19Parser {

  private static final Pattern APT_PTN = Pattern.compile("CONTACT APARTMENT *(.*)", Pattern.CASE_INSENSITIVE);
  
  public WAMasonCountyParser() {
    super("MASON COUNTY", "WA");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    // They report apt information in the name field
    String field = data.strName;
    data.strName = "";

    if (!field.equalsIgnoreCase("CONTACT") && !field.equalsIgnoreCase("YES")) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strApt = append(data.strApt, " - ", field);
    }
    
    return true;
  }
}
