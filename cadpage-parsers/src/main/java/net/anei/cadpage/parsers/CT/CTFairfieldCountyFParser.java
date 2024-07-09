package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTFairfieldCountyFParser extends FieldProgramParser {

  public CTFairfieldCountyFParser() {
    super("FAIRFIELD COUNTY", "CT",
          "ID EMPTY CALL EMPTY ADDR SKIP EMPTY CITY ZIP MAP_X INFO/N+? DATETIME/d EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "relay@newfairfieldct.gov";
  }

  private static final Pattern DELIM = Pattern.compile(" *\\| *");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP_X")) return new MyMapCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern LEAD_ZERO_PTN = Pattern.compile("0+(\\d.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = LEAD_ZERO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private static final Pattern MAP_CROSS_PTN = Pattern.compile("Map -(\\d(?: +[A-Z]-\\d{1,2})?) +(.*)");
  private class MyMapCrossField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = MAP_CROSS_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = match.group(1);
      data.strCross = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }
}
