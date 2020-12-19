package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class LACalcasieuParishBParser extends DispatchOSSIParser {

  public LACalcasieuParishBParser() {
    super(CITY_CODES, "CALCASIEU PARISH", "LA",
          "( CANCEL ADDR! CITY? " +
          "| FYI? CALL ADDR ( PLACE CITY | CITY | X | PLACE X | ) EMPTY+? X+? ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@calcasieu911.com,@calcasieu-911.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CROSS_PTN = Pattern.compile("ONRAMP.*");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (super.checkParse(field,  data)) return true;
      if (!CROSS_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CARL", "CARLYSS",
      "DEQU", "DEQUINCY",
      "IOWA", "IOWA",
      "LAKE", "LAKE CHARLES",
      "STAR", "STARKS",
      "SULP", "SULPHUR",
      "VINT", "VINTON",
      "WEST", "WESTLAKE"
  });

}
