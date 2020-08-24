package net.anei.cadpage.parsers.IN;



import net.anei.cadpage.parsers.dispatch.DispatchA56Parser;

/**
 * Parke County, IN
 */
public class INParkeCountyParser extends DispatchA56Parser {
  
  public INParkeCountyParser() {
    super("PARKE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@bloomingdaletel.com,DISPATCH@parkecounty-in.gov,parkecountydispatch911@gmail.com";
  }
}
