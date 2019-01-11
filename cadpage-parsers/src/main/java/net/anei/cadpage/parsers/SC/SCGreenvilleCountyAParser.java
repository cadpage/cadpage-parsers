package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Greenville County, SC
 */
public class SCGreenvilleCountyAParser extends DispatchOSSIParser {
  
  
  public SCGreenvilleCountyAParser() {
    super(CITY_CODES, "GREENVILLE COUNTY", "SC",
           "CALL ADDR CITY X/Z+? MAP NAME:NAME? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@greenvillecounty.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CAUTION: ")) {
      int pt = body.indexOf("\n\n\n");
      if (pt < 0) return false;
      body = body.substring(pt+3).trim();
    }
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  private class MyMapField extends MapField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf("(S) (N)");
      if (pt < 0) return false;
      data.strSupp = field.substring(0,pt).trim();
      data.strMap = field.substring(pt+7).trim();
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return "INFO MAP";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "BELT", "BELTON",
      "CAMP", "CAMPOBELLO",
      "CLEV", "CLEVELAND",
      "FOUN", "FOUNTAIN INN",
      "GRER", "GREER",
      "GRVL", "GREENVILLE",
      "HONE", "HONES PATH",
      "LAND", "LANDRUM",
      "MARI", "MARIETTA",
      "MAUL", "MAULDIN",
      "PELZ", "PELZER",
      "PIED", "PIEDMONT",
      "SIMP", "SIMPSONVILLE",
      "SLAT", "SLATER",
      "TRAV", "TRAVELERS REST",
      "TAYL", "TAYLORS"
  });
}
