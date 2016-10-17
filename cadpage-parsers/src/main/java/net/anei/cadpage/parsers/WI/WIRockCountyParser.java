package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class WIRockCountyParser extends DispatchPremierOneParser {

  public WIRockCountyParser() {
    super("ROCK COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "RCCC@co.rock.wi.us";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]{2,4})-Active911");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
