package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class NJMiddlesexCountyEParser extends DispatchA48Parser {
  
  public NJMiddlesexCountyEParser() {
    super(null, "MIDDLESEX COUNTY", "NJ", FieldType.X);
  }

  @Override
  public String getFilter() {
    return "@Rutgers.edu";
  }
}
