package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TNJeffersonCountyParser extends DispatchSouthernParser {
  
  
  public TNJeffersonCountyParser() {
    super(CITY_LIST, "JEFFERSON COUNTY", "TN", DSFLAG_OPT_DISPATCH_ID | DSFLAG_ID_OPTIONAL | DSFLAG_TRAIL_PLACE);
    removeWords("CIRCLE", "COURT", "PLACE", "TERRACE");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@jeffersoncountytn911.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern HIGHWAY_25_PTN = Pattern.compile("\\b(HIGHWAY|HWY|US|RT) 25 (32|70)\\b");
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" E/W ", " ");
      field = HIGHWAY_25_PTN.matcher(field).replaceAll("$1 $2");
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{
    "JEFFERSON CITY",
    "CHESTNUT HILL",
    "DANDRIDGE",
    "NEW MARKET",
    "PIEDMONT",
    "SEVIERVILLE",
    "STRAWBERRY PLAINS",
    "TALBOTT",
    "WHITE PINE",
    
    // Hamblen County
    "MORRISTOWN"
  };

}
