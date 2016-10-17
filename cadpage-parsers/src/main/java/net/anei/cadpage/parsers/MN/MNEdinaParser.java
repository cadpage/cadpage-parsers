package net.anei.cadpage.parsers.MN;

import java.util.Properties;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Edina, MN
 */
public class MNEdinaParser extends DispatchOSSIParser {
  public MNEdinaParser() {
    super(CITY_CODES, "EDINA", "MN",
        "( CANCEL ADDR CITY | ( PAGE | FYI? ) ( ADDRCITY | ADDR CITY? ) ( PLACE CALL | CALL! ) UNIT+? X X ) INFO+");
  }
  
  public String getFilter() {
    return "CAD@ci.edina.mn.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PAGE")) return new CallField("RICH +FD +FULL +CALLBACK|EDINA +FIRE +(?:GENERAL +ALARM|CHIEFS +PAGE|GROUP +\\d +PAGED)", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CALL")) return new  CallField("GV-.*|.* RICHFIELD|ASSIST .*", false);
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      super("[A-Z]*\\d+(?:,[A-Z]*\\d+)*|ST *\\d+", true);
    }
    @Override
    public void parse (String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EDI",            "EDINA",
      "RCH",            "RICHFIELD",
      "GV",             "GOLDEN VALLEY",
      "BLO",            "BLOOMINGTON",
      "MPLS",           "MINNEAPOLIS",
      "BROOKLYN PARK",  "BROOKLYN PARK",
      "SLP",            "ST LOUIS PARK"
  });
  
}
