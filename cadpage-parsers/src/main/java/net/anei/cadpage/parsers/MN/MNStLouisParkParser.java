package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA60Parser;

/**
 * St Louis Park, MN
 */
public class MNStLouisParkParser extends DispatchA60Parser {

  public MNStLouisParkParser() {
    super("ST LOUIS PARK", "MN");
  }

  @Override
  public String getFilter() {
    return "zuercher@stlouispark.org,@stlouispark.gov";
  }

  private static final Pattern SPEC_CALL_PTN = Pattern.compile("There is an? (All Call|Group 2)(?: for)? ");
  private static final Pattern TAIL_DATE_TIME_PTN = Pattern.compile(": \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    if (!subject.equals("CAD Call")) {
      Matcher match = SPEC_CALL_PTN.matcher(body);
      if (match.lookingAt()) {
        body = match.group(1) + " : " + body.substring(match.end()).trim();
      } else {
        body = subject + " : " + body;
      }
      match = TAIL_DATE_TIME_PTN.matcher(body);
      if (match.find()) {
        body = body.substring(0,match.start()) + ": " + body.substring(match.start());
      }
    }
    return super.parseMsg(body, data);
  }
}
