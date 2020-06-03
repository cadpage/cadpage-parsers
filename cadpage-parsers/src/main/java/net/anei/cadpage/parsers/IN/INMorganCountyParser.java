package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INMorganCountyParser extends DispatchA19Parser {
  
  public INMorganCountyParser() {
    super("MORGAN COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "notification@pd.mooresville.in.gov";
  }

}
