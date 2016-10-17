package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Herkimer County, NY (B)
 */
public class NYHerkimerCountyBParser extends DispatchA48Parser {
  
  public NYHerkimerCountyBParser() {
    super(CITY_LIST, "HERKIMER COUNTY", "NY", FieldType.NAME, A48_OPT_ONE_WORD_CODE);
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    fixCity(data);
    return true;
  }

  @Override
  public String getFilter() {
    return "HC911@herkimercounty.org";
  }
  
  static void fixCity(Data data) {
    Matcher match = CITY_VILLAGE_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1).trim();
  }
  private static final Pattern CITY_VILLAGE_PTN = Pattern.compile("(.*) VILLAGE", Pattern.CASE_INSENSITIVE);
  
  static final String[] CITY_LIST = new String[]{  
    "COLD BROOK",
    "COLD BROOK VILLAGE",
    "COLUMBIA",
    "DANUBE",
    "DEERFIELD", 
    "DOLGEVILLE",
    "DOLGEVILLE VILLAGE",
    "FAIRFIELD",
    "FRANKFORT",
    "FRANKFORT VILLAGE",
    "GERMAN FLATTS",
    "HERKIMER",
    "HERKIMER VILLAGE",
    "ILION",
    "ILION VILLAGE",
    "LITCHFIELD",
    "LITCHFIELD VILLAGE",
    "LITTLE FALLS",
    "LITTLE FALLS CITY",
    "MANHEIM",
    "MIDDLEVILLE",
    "MIDDLEVILLE VILLAGE",
    "MOHAWK",
    "MOHAWK VILLAGE",
    "NEWPORT",
    "NEWPORT VILLAGE",
    "NORWAY",
    "OHIO",
    "POLAND",
    "POLAND VILLAGE",
    "RUSSIA",
    "SALISBURY",
    "SCHUYLER",
    "STARK",
    "WARREN",
    "WEBB",
    "WEST WINFIELD",
    "WEST WINFIELD VILLAGE",
    "WINFIELD"
  }; 
}
