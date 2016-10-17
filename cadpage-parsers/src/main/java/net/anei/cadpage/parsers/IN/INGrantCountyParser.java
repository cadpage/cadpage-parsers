package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA56Parser;

public class INGrantCountyParser extends DispatchA56Parser {

  public INGrantCountyParser() {
    super("GRANT COUNTY", "IN");
  }
  
  @Override public String getFilter() {
    return "DISPATCH@GrantCounty.net";
  }
  
  private static Pattern MAP_PLACE_PTN = Pattern.compile("([A-Z]+\\d+) {3,}(.+)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("FIRE")) data.strSource = subject;
    if (!super.parseMsg(body, data)) return false;
    Matcher match = MAP_PLACE_PTN.matcher(data.strPlace);
    if (match.matches()) {
      data.strMap = match.group(1);
      data.strPlace = match.group(2);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram().replace("PLACE", "MAP PLACE");
  }
}
