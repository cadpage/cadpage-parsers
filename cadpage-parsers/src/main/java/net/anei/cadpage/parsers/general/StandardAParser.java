package net.anei.cadpage.parsers.general;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Parser class that handles standard text format A
 */
public class StandardAParser  extends FieldProgramParser{
  
  public StandardAParser() {
    super("", "", 
           "CALL ADDR CITY INFO/N+");
  }
  
  @Override
  public String getLocName() {
    return "Standard Format A";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    // TODO Auto-generated method stub
    return super.parseHtmlMsg(subject, body, data);
  }
  
  private static final Pattern DELIM = Pattern.compile("[;,\n]");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!isPositiveId()) return false;
    return parseFields(DELIM.split(body), data);
  }
}
