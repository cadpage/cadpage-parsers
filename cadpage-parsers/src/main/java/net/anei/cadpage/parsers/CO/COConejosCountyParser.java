package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class COConejosCountyParser extends DispatchPremierOneParser {

  public COConejosCountyParser() {
    super("CONEJOS COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "@csp.noreply";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*) Notification");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1);
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
