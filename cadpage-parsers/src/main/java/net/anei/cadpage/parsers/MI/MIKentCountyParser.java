package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class MIKentCountyParser extends DispatchPremierOneParser {

  public MIKentCountyParser() {
    super("KENT COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "KCCC@Kent911.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*) (?:Med|Fire) Alert");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1).trim();
    
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
