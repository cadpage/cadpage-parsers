package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TNJeffersonCountyParser extends DispatchSouthernParser {


  public TNJeffersonCountyParser() {
    super(CITY_LIST, "JEFFERSON COUNTY", "TN",
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE2|DSFLG_OPT_ID|DSFLG_OPT_TIME);
    removeWords("CIRCLE", "COURT", "COVE", "PLACE", "ROAD", "TERRACE");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@jeffersoncountytn911.org,911@jeffersoncountytn911.org";
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

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
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
    "MORRISTOWN",

    // Sevior County
    "GATLINBURG",
    "PITTMAN CENTER"
  };

}
