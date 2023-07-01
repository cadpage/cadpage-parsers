package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNCottonwoodCountyParser extends FieldProgramParser {

  public MNCottonwoodCountyParser() {
    super("COTTONWOOD COUNTY", "MN",
          "DATETIME CALL ADDRCITYST ( NONE | INFO INFO/N+? ) UNIT! UNIT/C+? GPS1 GPS2");
  }

  @Override
  public String getFilter() {
    return "noreply@co.cottonwood.mn.us";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    if (name.equals("NONE")) return new SkipField("None", true);
    return super.getField(name);
  }

  private static final Pattern INFO_CH_PTN = Pattern.compile("(.*?)[- ]*\\b(?:USE )?(I?OPS *\\d+)", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    public MyInfoField() {
      super("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *(.*)", true);
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CH_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strChannel = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH";
    }
  }
}
