package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyNParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyNParser() {
    super("CALL ADDR CITY/Y PLACE X PLACE_PHONE! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  private static final Pattern DATE_PTN = Pattern.compile("\\b\\d\\d/\\d\\d/\\d{4}\\b");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strSource = subject;
    if (!parseFields(body.split("\n"), data)) return false;
    
    // Rule out misparsed alerts from other formats
    if (DATE_PTN.matcher(data.strCross).find()) return false;
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
