package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA56Parser;

public class NYWashingtonCountyParser extends DispatchA56Parser {

  public NYWashingtonCountyParser() {
    super("WASHINGTON COUNTY", "NY");
  }
  
  @Override public String getFilter() {
    return "DISPATCH@co.washington.ny.us";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("\\[\\w+\\] +([A-Z]+)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
}
