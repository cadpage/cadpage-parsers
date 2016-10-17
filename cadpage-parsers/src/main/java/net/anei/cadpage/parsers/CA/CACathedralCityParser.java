package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA58Parser;

public class CACathedralCityParser extends DispatchA58Parser {

  public CACathedralCityParser() {
    super("CCPD Alliance Message", CITY_CODES, "CATHEDRAL CITY", "CA");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern DIR_OF_PTN = Pattern.compile("[NSEW]O .*");
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (DIR_OF_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }

  private static Properties CITY_CODES = buildCodeTable(new String[] { 
      "CTHDRL CTY",   "Cathedral City" 
  });

}
