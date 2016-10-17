package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class NCMartinCountyAParser extends DispatchA3Parser {
  
  private static final Pattern PREFIX_PTN = Pattern.compile("Count=1\nID=\n\\[Subscriber1\\]\nSubscriberName=([A-Z0-9]+)\n\\[Message\\]\nLineCount=18\n");

  public NCMartinCountyAParser() {
    super("", "MARTIN COUNTY", "NC", 
          "Line1:ID! Line2:ADDR! Line3:APT! Line4:APT! Line5:CITY! Line6:X! Line7:X! Line8:EMPTY! Line9:EMPTY! Line10:CODE! Line11:CALL! Line12:NAME! Line13:PHONE! Line14:INFO2! Line15:INFO2! Line16:INFO2! Line17:INFO2! Line18:INFO!");
    setBreakChar('=');
  }
  
  @Override
  public String getFilter() {
    return "MC911@martincountyncgov.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Header")) return false;
    Matcher match = PREFIX_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strSource = match.group(1);
    body = body.substring(match.end()).trim();
    body = stripFieldEnd(body, "*");
    return parseFields(body.split("\\*\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO2")) return new InfoField();
    return super.getField(name);
  }
}
