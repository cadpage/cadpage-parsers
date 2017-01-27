package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class AZMaricopaCountyDParser extends DispatchA9Parser {
  
  public AZMaricopaCountyDParser() {
    super("MARICOPA COUNTY", "AZ");
  }
  
  @Override
  public String getFilter() {
    return "fire@srpmic-nsn.gov";
  }
}


