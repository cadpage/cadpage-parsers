package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCRobesonCountyParser extends DispatchOSSIParser {

  public NCRobesonCountyParser() {
    super(CITY_CODES, "LUMBERTON", "NC",
          "CH? CALL ADDR! ( END " +
                         "| ID! " +
                         "| CITY/Y! ID? " +
                         "| X2 CITY? ID? " +
                         "| X X/Z+? ( CITY! ID? | ID! ) " +
                         "| PLACE X/Z+? ( CITY! ID? | ID! ) " +
                         ") CODE");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:"))  body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (!data.strCode.isEmpty()) {
      String call = CALL_TABLE.getCodeDescription(body);
      if (call != null) data.strCall = call;
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("FTAC\\d+");
    if (name.equals("X2")) return new CrossField("(?:X-?ST|X)[- ]+(.*)", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM)[-* ]+(.*)|(\\d{1,4}(?:-?[A-Z])?)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = match.group(2);
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = field;
      }
    }

  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FAIR", "FAIRMONT",
      "GALA", "GALA",
      "LBRG", "LUMBER BRIDGE",
      "LUMB", "LUMBERTON",
      "ORR",  "ORRUM",
      "MAX",  "MAXTON",
      "MAXT", "MAXTON",
      "PARK", "PARKTON",
      "PEMB", "PEMBROKE",
      "PROC", "PROCTORVILLE",
      "RED",  "RED SPRINGS",
      "REDS", "RED SPRINGS",
      "RESP", "RED SPRINGS",
      "ROW",  "ROWLAND",
      "SHAN", "SHANNON",
      "STP",  "ST PAULS"
  });

  private static final CodeTable CALL_TABLE = new StandardCodeTable();
}
