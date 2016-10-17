package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

// Martin County, MN
 

public class MNMartinCountyParser extends DispatchA27Parser {
  
  private static final Pattern INN_PTN = Pattern.compile("I *\\d+.*");
  
  public MNMartinCountyParser() {
    super("MARTIN COUNTY", "MN", "[- /A-Z0-9]+");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    // Interstate mile markers got turned to apt numbers
    if (data.strApt.length() > 0 && INN_PTN.matcher(data.strAddress).matches()) {
      data.strAddress = data.strAddress + " MM " + data.strApt;
      data.strApt = "";
    }
    return true;
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
