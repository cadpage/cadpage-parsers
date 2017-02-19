package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAGraysHarborCountyBParser extends DispatchA19Parser {
  
  public WAGraysHarborCountyBParser() {
    super("GRAYS HARBOR COUNTY", "WA");
  }
  
  @Override
  public String getFilter() {
    return "noreply@gh911.org";
  }

  private static final Pattern MBLANK_PTN = Pattern.compile(" {3,}");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("Call Closed ")) {
      subject = subject.substring(13).trim();
      subject = MBLANK_PTN.matcher(subject).replaceAll("\n");
      body = subject + "\n" + body;
      subject = "";
    }
    return super.parseMsg(subject, body, data);
  }
  
}
