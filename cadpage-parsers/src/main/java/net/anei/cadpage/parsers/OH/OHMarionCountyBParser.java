package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class OHMarionCountyBParser extends DispatchOSSIParser {

  public OHMarionCountyBParser() {
    super(CITY_CODES, "MARION COUNTY", "OH",
          "( CANCEL ADDR! CITY? | FYI ( SRC ( MUT_AID EVT_SPAWN? CALL/SDS EMPTY? ( INFO ADDR | INFO INFO ADDR! | ADDR! ) | ( PLACE ADDR/Z CITY | ADDR CITY? ) CALL! ) | CALL ADDR! CITY? ) ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "firemedic745@yahoo.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{2,4}", true);
    if (name.equals("MUT_AID")) return new CallField("MUTUAL AID", true);
    if (name.equals("EVT_SPAWN")) return new SkipField("Event spawned from.*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("CORNER OF") && isValidAddress(field)) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LAR", "LARUE",
      "NEW", "NEW BLOOMINGTON",
      "PRO", "PROSPECT"
  });
}
