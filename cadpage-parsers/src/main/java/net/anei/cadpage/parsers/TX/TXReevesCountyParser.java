package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXReevesCountyParser extends FieldProgramParser {

  public TXReevesCountyParser() {
    super("REEVES COUNTY", "TX",
          "CALL ADDRCITYST PLACE PLACE GPS1 GPS2 INFO DATETIME UNIT SRC X SKIP SKIP ID EMPTY END");
  }

  @Override
  public String getFilter() {
    return "RCESDCAD@reevescounty-esd.com";
  }

  private static final Pattern DELIM = Pattern.compile(" *\\| *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith("|")) return false;
    body = subject + ' ' + body;
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("CFS\\d+", true);
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
