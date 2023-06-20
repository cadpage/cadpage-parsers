package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class OHLoganCountyCParser extends FieldProgramParser {

  public OHLoganCountyCParser() {
    super("LOGAN COUNTY", "OH",
          "DATETIME CALL! ADDR Apt:APT? CITY PLACE END");
  }

  @Override
  public String getFilter() {
    return "smarlow@ci.bellefontaine.oh.us";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(", (?=\\d{4}/\\d\\d/\\d\\d \\d\\d:\\d\\d )");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Dispatched")) return false;

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();

    String info = null;
    Matcher match = INFO_BRK_PTN.matcher(body);
    if (match.find()) {
      info = body.substring(match.end());
      body = body.substring(0, match.start()).trim();
    }

    if (!parseFields(body.split(", "), data)) return false;

    if (info != null) {
      for (String line : info.split("\n")) {
        line = line.trim();
        pt = line.indexOf(": ");
        if (pt >= 0) line = line.substring(pt+2).trim();
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d");
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class  MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      data.strCode = field.substring(0, pt);
      data.strCall = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
