package net.anei.cadpage.parsers.TN;

/**
 * Sumner County, TN
 */
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class TNSumnerCountyAParser extends DispatchA3Parser {
  
  public TNSumnerCountyAParser() {
    super("", "SUMNER COUNTY", "TN",
           "EMPTY ADDR APT UNK CITY PLACE CALL! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "E911@sumnersheriff.com";
  }
}