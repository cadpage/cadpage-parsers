package net.anei.cadpage.parsers.IN;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA56Parser;

/**
 * Parke County, IN
 */
public class INParkeCountyParser extends DispatchA56Parser {
  
  public INParkeCountyParser() {
    super("PARKE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@bloomingdaletel.com,DISPATCH@parkecounty-in.gov,parkecountydispatch911@gmail.com,DISPATCHtext@parkecounty-in.gov,ParkeCountyDispatch@outlook.com";
  }
  
  private static final Pattern SHORT_FORM_PTN = Pattern.compile("([^:\n]+?)//(?:[-/&A-Z0-9 ]+//)?([A-Z]{3,4}:[A-Z]{3,6})[,/]");

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // See if this is the short form which eliminates everything up to the first \n\n
    // If it is, add dummy information to make this parseable
    boolean shortForm = false;
    Matcher match = SHORT_FORM_PTN.matcher(body);
    if (match.lookingAt()) {
      String addr = match.group(1);
      if (!addr.contains("//") && !addr.contains(" - ")) {
        shortForm = true;
        body = match.group(2) + " - 99/99 99:99 - XXXX:99-999999 9999 XXXXX\n\n" + body;
      }
    }
    
    if (!body.startsWith("DISPATCH:")) body = "DISPATCH:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (shortForm) data.strDate = data.strTime = data.strSource = data.strCallId = data.strCode = data.strCall = "";
    return true;
  }
   
}
