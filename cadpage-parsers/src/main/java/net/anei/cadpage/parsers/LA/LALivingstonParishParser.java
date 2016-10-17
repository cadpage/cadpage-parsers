package net.anei.cadpage.parsers.LA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Livingston Parish, LA
*/

public class LALivingstonParishParser extends DispatchA49Parser {
  
  public LALivingstonParishParser() {
    super("LIVINGSTON PARISH","LA");
  }

  @Override
  public String getFilter() {
    return "cadalert@lafayettela.gov,alerts@carencrofd.org";
  }

  private static final Pattern FIX_MARK_PTN = Pattern.compile("^(\\d\\d/\\d\\d/\\d{4} Time:\\d\\d:\\d\\d)\nEQ ID:(.*)\n");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert Recieved")) return false;
    body = FIX_MARK_PTN.matcher(body).replaceFirst("Date:$1 Num:$2\n");
    body = body.replace("\nRemarks >", "\nRemarks:\n>");
    return super.parseMsg(body, data);
  }
 
}
