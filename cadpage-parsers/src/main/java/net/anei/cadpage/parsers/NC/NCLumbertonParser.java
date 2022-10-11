package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCLumbertonParser extends DispatchOSSIParser {

  public NCLumbertonParser() {
    super(CITY_CODES, "LUMBERTON", "NC",
          "CALL ADDR ( ID! END " +
                    "| CITY/Y! " +
                    "| PLACE X/Z X/Z CITY/Y! " +
                    "| X/Z+? CITY/Y! " +
                    ") ID CODE");
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
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FAIR", "FAIRMONT",
      "GALA", "GALA",
      "LBRG", "LUMBER BRIDGE",
      "LUMB", "LUMBERTON",
      "ORR",  "ORRUM",
      "MAX",  "MAXTON",
      "PARK", "PARKTON",
      "PEMB", "PEMBROKE",
      "REDS", "RED SPRINGS",
      "ROW",  "ROWLAND",
      "SHAN", "SHANNON",
      "STP",  "ST PAULS"
  });

  private static final CodeTable CALL_TABLE = new StandardCodeTable();
}
