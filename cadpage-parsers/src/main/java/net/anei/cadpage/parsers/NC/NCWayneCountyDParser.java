package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCWayneCountyDParser extends DispatchOSSIParser {

  public NCWayneCountyDParser() {
    super(CITY_CODES, "WAYNE COUNTY", "NC",
          "( CANCEL ADDR CITY " +
          "| CALL ADDR ID CITY UNIT EMPTY GPS1 GPS2 CH! DATETIME UNIT/C! " +
          ") INFO/N+? UNIT2");
  }

  private static final Pattern MARKER = Pattern.compile("([A-Z0-9]+)\nCAD\n\\s*");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      data.strSource = match.group(1);
      body = body.substring(match.end());
    }
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT2")) return new MyUnit2Field();
    return super.getField(name);
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+(?:,.*)?");
  private class MyUnit2Field extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!UNIT_PTN.matcher(field).matches()) return false;
      data.strUnit = append(data.strUnit, ",", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "DUD", "DUDLEY",
      "EUR", "EUREKA",
      "FOR", "FOUR OAKS",
      "FRE", "FREMONT",
      "GLB", "GOLDSBORO",
      "LAG", "LA GRANGE",
      "MTO", "MOUNT OLIVE",
      "PRN", "PRINCETON",
      "PIK", "PIKEVILLE",
      "SSP", "SEVEN SPRINGS",
      "STA", "STANTONSBURG"
  });
}
