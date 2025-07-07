package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class ALColbertCountyCParser extends DispatchA77Parser {

  public ALColbertCountyCParser() {
    super("CAD Alert", "COLBERT COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\S+) Station Alerting");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1);
      subject = "CAD Alert";
    }
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
