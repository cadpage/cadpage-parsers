package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALFayetteCountyParser extends FieldProgramParser {

  public ALFayetteCountyParser() {
    super("FAYETTE COUNTY", "AL",
          "Nature:CALL! Add:ADDRCITY! Comm:INFO! Coords:GPS! Date/Time:DATETIME! Unit:UNIT? END");
  }

  @Override
  public String getFilter() {
    return "countyoffayette911@gmail.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("Call: (\\d+) Case No:");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);

    int pt = body.indexOf("\nFrom :");
    if (pt >= 0) body = body.substring(0, pt).trim();

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
