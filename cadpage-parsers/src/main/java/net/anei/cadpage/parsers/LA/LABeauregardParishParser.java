package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Beauregard Parish, LA
 */
public class LABeauregardParishParser extends FieldProgramParser {
  
  public LABeauregardParishParser() {
    super("BEAUREGARD PARISH", "LA",
          "CALL ADDR CITY! EMPTY! END");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@kologik.com,cadalertsmessaging@gmail.com";
  }
  
  private static final Pattern SUBJECT_PTN =  Pattern.compile("(\\d\\d:\\d\\d) @ .*");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    if (!super.parseFields(body.split(";", -1), data)) return false;
    if (data.strCity.equals("UNKNOWN")) data.strCity = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "TIME " + super.getProgram();
  }
}
