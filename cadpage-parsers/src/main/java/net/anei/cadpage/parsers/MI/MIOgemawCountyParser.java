package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIOgemawCountyParser extends DispatchOSSIParser {
  
  public MIOgemawCountyParser() {
    super("OGEMAW COUNTY", "MI", 
          "( CANCEL ADDR SKIP " +
          "| FYI? CALL ADDR! X+? " + 
          ") INFO/N+");
  }
  
  private static final Pattern MARKER = Pattern.compile("([A-Z0-9]+)\nCAD\n|ACTIVE 911 CAD ");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strSource = getOptGroup(match.group(1));
    body = "CAD:" + body.substring(match.end());
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

}
