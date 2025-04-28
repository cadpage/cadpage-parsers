package net.anei.cadpage.parsers.MT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTLewisAndClarkCountyCParser extends FieldProgramParser {

  public MTLewisAndClarkCountyCParser() {
    super("LEWIS AND CLARK COUNTY", "MT",
          "ID CODE ADDRCITYST PLACE GPS1 GPS2 CODE/L CALL CODE/L CALL/L NAME PHONE DATETIME INFO EMPTY UNIT EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "dispatch@helenamt.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return parseFields(body.split("\\|",-1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{3}");
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      if (field.equals(data.strCode))return;
      data.strCode = append(data.strCode, "/", field);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      if (field.equals(data.strCall))return;
      data.strCall = append(data.strCall, "/", field);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - Log - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
