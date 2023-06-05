package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAAmherstCountyParser extends FieldProgramParser {

  public VAAmherstCountyParser() {
    super("AMHERST COUNTY", "VA",
          "SRC_ID CALL ADDRCITYST PLACE APT! XST:X! NOTES:INFO! END");
  }

  @Override
  public String getFilter() {
    return "dispatch@amherstva911.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC_ID")) return new MySourceIdField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*): *(CAD\\d{4}-\\d{6})");
  private class MySourceIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1).trim();
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|UNIT) +(.+)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("//")) return;
      field = field.replace("//", "/");
      super.parse(field, data);
    }
  }
}
