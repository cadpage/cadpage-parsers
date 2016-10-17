package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAFanninCountyParser extends DispatchB2Parser {

  public GAFanninCountyParser() {
    super("911-CENTER:", "FANNIN COUNTY", "GA", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BETHEL CHURCH",
        "DRY BRANCH",
        "FISH TRAP",
        "FOREST COVE",
        "GRADY HUNT",
        "MINERAL BLUFF",
        "POWER DAM",
        "SCENIC RIDGE",
        "WATSON GAP",
        "WINDY RIDGE"
    );
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@mydomain.com";
  }  
   
  private static final Pattern BEGIN_PTN = Pattern.compile("BEGINS? +(.*)");
  private static final Pattern LEAD_DIGIT_PTN = Pattern.compile("(\\d+) *(.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Weird cross street convention, which may or may not have prevented
    // the cross street from being identified as such
    Matcher match = BEGIN_PTN.matcher(data.strCross);
    if (match.matches()) {
      data.strCross = match.group(1);
    }
    else if ((match = BEGIN_PTN.matcher(data.strName)).matches()) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, match.group(1), data);
      data.strName = getLeft();
    }
    match = LEAD_DIGIT_PTN.matcher(data.strName);
    if (match.matches()) {
      data.strCross = append(data.strCross, " ", match.group(1));
      data.strName = match.group(2);
    }
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr, boolean cross) {
    if (cross) {
      Matcher match = LEAD_DIGIT_PTN.matcher(addr);
      if (match.matches()) addr = match.group(2);
    }
    return super.adjustMapAddress(addr);
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "BRUSH,FIELD,GRASS,WOODS FIRE",
      "CHIMNEY FIRE",
      "ELECTRICAL FIRE",
      "EXPLOSION",      
      "RESIDENTIAL-FIRE",
      "VEHICLE FIRE"
  );

}
