package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDSomersetCountyBParser extends FieldProgramParser {

  public MDSomersetCountyBParser() {
    super("SOMERSET COUNTY", "MD",
          "UNIT! UNIT/CS+? ADDRCITYST APT PLACE X CALL! INFO/N+? ID GPS1 GPS2 SRC! SRC/CS+? SKIP+");
  }

  @Override
  public String getFilter() {
    return "no-reply@somersetmd.us";
  }

  private static final Pattern DELIM = Pattern.compile(" *(?<!Code)[;:] +");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith("Respond to Incident")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z]{3,5}", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("SRC")) return new SourceField("(?:A|ST)\\d+(?:-\\d)?", true);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM) *(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +(?=\\d{1,2}\\. )");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Code: ")) {
        data.strCode = field.substring(6).trim();
      } else {
        field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }
}
