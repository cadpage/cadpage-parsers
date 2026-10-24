package net.anei.cadpage.parsers.VT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class VTOrangeCountyBParser extends DispatchA27Parser {

  public VTOrangeCountyBParser() {
    super("ORANGE COUNTY", "VT");
  }

  @Override
  public String getFilter() {
    return "notification@nhpd.cloud";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(\\d{4}-\\d{6}) \\(P#:(\\d\\d-\\d{6})\\)\n");
  private static final Pattern TRAIL_MAP_PTN = Pattern.compile("\nFire Zone: *(.*) EMS Zone: *(.*)$");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = subject.indexOf('|');
    if (pt < 0) return false;
    if (!subject.substring(pt+1).equals("CSI Active911 - CFS Notification")) return false;
    subject = subject.substring(0,pt).trim();

    Matcher match = LEAD_ID_PTN.matcher(body.substring(0,pt));
    if (!match.lookingAt()) return false;
    String callId = match.group(1)+'/'+match.group(2);
    body = "Notification from CIS Active911:\n\n" + body.substring(match.end()).trim();

    match = TRAIL_MAP_PTN.matcher(body);
    if (match.find()) {
      data.strMap = append(match.group(1),  "/",  match.group(2));
      body = body.substring(0, match.start()).trim();
    }

    if (!super.parseMsg(subject, body, data)) return false;
    data.strCallId = callId;
    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram() + " MAP";
  }
}
