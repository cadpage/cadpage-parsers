package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA2Parser;

public class NYOtsegoCountyParser extends DispatchA2Parser {
    
    public NYOtsegoCountyParser() {
      super("OTSEGO COUNTY", "NY");
    }
    
    @Override
    public String getFilter() {
      return "CAD@otsegocounty.com";
    }
	}
	