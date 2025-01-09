package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INHancockCountyDParser extends FieldProgramParser {

  public INHancockCountyDParser() {
    super("HANCOCK COUNTY", "IN",
          "ID CALL ADDRCITY! INFO/N+? UNIT END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    if (name.equals("INFO"))  return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d[- ]+");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (field.startsWith("Suffix:") ||
          field.startsWith("CAD Response")) return;
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) {
        field = field.substring(match.end());
        if (field.startsWith("Reconfigured Code:") ||
            field.startsWith("Dispatch Code:")) return;
        if (field.startsWith("Callback:")) {
          data.strPhone = field.substring(9).trim();
          return;
        }
      } else {
        if (data.strPlace.isEmpty() && data.strSupp.isEmpty()) {
          data.strPlace = field;
          return;
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE INFO PHONE";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("(.*) From ([A-Za-z]+)\\b.*");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      Matcher match = UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1).replace(" ", "");
      data.strSource = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT SRC";
    }
  }
}
