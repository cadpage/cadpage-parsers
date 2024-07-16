package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class ARCrawfordCountyParser extends DispatchA33Parser {

  public ARCrawfordCountyParser() {
    super("CRAWFORD COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("Call (\\S+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strUnit = match.group(1);
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

}
