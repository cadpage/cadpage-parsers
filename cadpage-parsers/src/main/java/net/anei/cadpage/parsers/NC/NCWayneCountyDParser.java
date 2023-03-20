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
          "| CALL ADDR ID CITY UNIT EMPTY GPS1 GPS2 CH! " + 
          ") INFO/N+");
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
    return super.getField(name);
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
