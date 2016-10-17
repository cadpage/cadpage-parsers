package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXRoanokeParser extends DispatchA18Parser {
  
  public TXRoanokeParser() {
    super(CITY_LIST, "ROANOKE","TX");
  }
  
  @Override
  public String getFilter() {
    return "prc@roanokepolice.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    return SMALL_BLOCK_PTN1.matcher(sAddress).replaceAll("$1_$2");
  }
  private static final Pattern SMALL_BLOCK_PTN1 = Pattern.compile("(SMALL) +(BLOCK)", Pattern.CASE_INSENSITIVE);

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return SMALL_BLOCK_PTN2.matcher(sAddress).replaceAll("$1 $2");
  }
  private static final Pattern SMALL_BLOCK_PTN2 = Pattern.compile("(SMALL)_(BLOCK)", Pattern.CASE_INSENSITIVE);

  private static String[] CITY_LIST = new String[]{
    "ROANOKE",
    "FORT WORTH",
    "DENTON"
  };
}
