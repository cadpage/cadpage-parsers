package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


/**
 * Pulaski County, MO
 */
public class MOPulaskiCountyBParser extends DispatchA19Parser {

  public MOPulaskiCountyBParser() {
    super("PULASKI COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "DISPATCH,messaging@iamresponding.com,911dispatch@embargmail.com,pulaskicommcenter@gmail.com";
  }

  private static final Pattern MISSING_CITY_PTN = Pattern.compile("\nAddress: (.*?) , (.*)\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Fix IAR edits
    if (subject.equals("Waynesville Fire")) {
      if (!body.contains("LONG TERM CAD#")) {
        body = body.replace("\nREPORTED:", "\nLONG TERM CAD#   X   ACTIVE CALL# X\nPRIORITY:          REPORTED:");
      }
      if (!body.contains(" Zone:")) {
        body = MISSING_CITY_PTN.matcher(body).replaceFirst("\nAddress: $1 Zone: City:$2\n");
      }
    }

    if (!super.parseMsg(subject, body, data)) return false;
    data.strCallId = stripFieldEnd(data.strCallId, "/X/X");
    return true;
  }

}
