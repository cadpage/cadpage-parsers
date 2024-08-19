package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJMonmouthCountyDParser extends DispatchA19Parser {

  public NJMonmouthCountyDParser() {
    super(NJMonmouthCountyAParser.CITY_CODES, "MONMOUTH COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "MCSOPageNotification@mcsonj.org,MCSOCallComplete@mcsonj.org";
  }

  private static final Pattern FIX1_PTN = Pattern.compile("(LONG TERM CAD #: \\S+) {3,}(INCIDENT #: [^ \\\\]+)");
  private static final Pattern FIX2_PTN = Pattern.compile(" {4,}(PRIORITY:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Have to fix some odd things
    if (subject.equals("MCSO Page Notification")) {
      body = FIX1_PTN.matcher(body).replaceFirst("$2\n$1");
      body = FIX2_PTN.matcher(body).replaceFirst("\n$1");
    }

    int pt = body.indexOf("\nCONFIDENTIALITY NOTICE");
    if (pt >= 0) body = body.substring(0, pt).trim();

    return super.parseMsg(subject, body, data);
  }
}
