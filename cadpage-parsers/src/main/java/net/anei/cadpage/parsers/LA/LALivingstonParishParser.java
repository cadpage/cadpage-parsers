package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Livingston Parish, LA
*/

public class LALivingstonParishParser extends DispatchA49Parser {

  public LALivingstonParishParser() {
    super("LIVINGSTON PARISH","LA", CALL_CODES);
  }

  @Override
  public String getFilter() {
    return "cadalert@lafayettela.gov,alerts@carencrofd.org";
  }

  private static final Pattern FIX_MARK_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4} Time:\\d\\d:\\d\\d)\n(?:EQ ID:(.*)\n)?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert Recieved")) return false;
    Matcher match = FIX_MARK_PTN.matcher(body);
    if (match.lookingAt()) {
      StringBuilder sb = new StringBuilder("Date:");
      sb.append(match.group(1));
      String num = match.group(2);
      if (num != null) {
        sb.append(" Num:");
        sb.append(num);
      }
      sb.append('\n');
      sb.append(body.substring(match.end()));
      body = sb.toString();
    }
    body = body.replace("\nRemarks >", "\nRemarks:\n>");
    return super.parseMsg(body, data);
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "COMALARM", "FIRE ALARM - COMMERCIAL",
      "COMMFIRE", "STRUCTURE FIRE-COMMERCIAL",
      "MEDICAL",  "MEDICAL",
      "MVA",      "MVA",
      "PUBASST",  "PUBLIC ASSIST",
      "RESDFIRE", "STRUCTURE FIRE-RESIDENTIAL",
  });


}
