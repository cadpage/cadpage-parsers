package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNSullivanCountyParser extends FieldProgramParser {

  public TNSullivanCountyParser() {
    super("SULLIVAN COUNTY", "TN",
          "ADDRCITYST GPS1 GPS2 X DATETIME! INFO/N+? EMPTY/Z! END");
  }

  @Override
  public String getFilter() {
    return "noreplycentralsquare@scsotn.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("([ A-Z]+) - +(.*)");
  private static final Pattern DELIM = Pattern.compile(" \\|(?= |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strCode = match.group(1).trim();
      data.strCall = stripFieldEnd(match.group(2), " - None");
    } else if (!subject.equals("- - None")) return false;
    int pt = body.indexOf("\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public String getProgram() {
    return "CODE CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
