package net.anei.cadpage.parsers.ID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA4Parser;


public class IDBlaineCountyAParser extends DispatchA4Parser {
  
  public IDBlaineCountyAParser() {
    super("BLAINE COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "ldispatch@co.blaine.id.us,LogiSYS CAD";
  }
  
  private static final Pattern PREFIX_PTN = Pattern.compile("Blaine County: \\((CAD Page for CFS \\S+)\\)\\s+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) {
      Matcher match = PREFIX_PTN.matcher(body);
      if (match.lookingAt()) {
        subject = match.group(1);
        body = body.substring(match.end());
      }
    }

    return super.parseMsg(subject, body, data);
  }
 
}
