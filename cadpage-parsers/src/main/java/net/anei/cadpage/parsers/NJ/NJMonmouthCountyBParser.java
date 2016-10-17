package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Monmouth County, NJ (variant B)
 */
public class NJMonmouthCountyBParser extends FieldProgramParser {
  
  public NJMonmouthCountyBParser() {
    super(CITY_CODES, "MONMOUTH COUNTY", "NJ",
           "CALL ADDR/S X-ST:X! INFO TIME DATE!");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!parseFields(body.split("\\|"), data)) return false;
    return true;
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
  });
}
