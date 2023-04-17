package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VAGreensvilleCountyParser extends DispatchOSSIParser {

  public VAGreensvilleCountyParser() {
    super(CITY_CODES, "GREENSVILLE COUNTY", "VA",
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? DATETIME ( SRC ID? CALL ADDR! X+? | CALL ID PLACE ADDR! X X ) INFO/N+? UNIT )");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new BaseCancelField(".* PAGE|DRIVER NEEDED|.*HEALTH ADVISORY ALERT");
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}", true);
    if (name.equals("ID")) return new IdField("\\d*", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\d{3}[A-Z]?");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!UNIT_PTN.matcher(field).matches()) return false;
      data.strUnit = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "EMPO", "EMPORIA"
  });

}
