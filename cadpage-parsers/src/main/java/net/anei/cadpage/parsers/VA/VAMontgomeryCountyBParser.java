package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAMontgomeryCountyBParser extends FieldProgramParser {
  
  public VAMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "VA", 
          "ADDRCITY CALL PLACE INFO X UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@nrv911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    body = stripFieldEnd(body, " <end>");
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Searching Cross Streets...")) return;
      super.parse(field,  data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) city = tmp;
    return city;
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "VIRGINIA TECH", "Blacksburg"
  });

}
