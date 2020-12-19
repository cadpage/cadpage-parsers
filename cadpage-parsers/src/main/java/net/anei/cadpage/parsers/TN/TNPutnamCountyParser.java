package net.anei.cadpage.parsers.TN;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TNPutnamCountyParser extends DispatchOSSIParser {

  public TNPutnamCountyParser() {
    super(CITY_CODES, "PUTNAM COUNTY", "TN",
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI DATETIME CALL ADDR ( PLACE CITY | CITY? ) X_PLACE/Z+? UNIT! UNIT/C+? INFO/N+? ID END )");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME"))  return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]{1,5}\\d?|\\d{3})\\b,?)+", true);
    if (name.equals("ID")) return new IdField("\\d{11}", true);
    return super.getField(name);
  }

  private class MyCrossPlaceField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.isEmpty() && ! isValidCrossStreet(field)) {
        data.strPlace = field;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ALGO", "ALGOOD",
      "BAXT", "BAXTER",
      "BLOO", "BLOOMINGTON SPRINGS",
      "BUFF", "BUFFALO VALLEY",
      "COOK", "COOKEVILLE",
      "MONT", "MONTEREY",
      "SILV", "SILVER POINT"
  });

}
