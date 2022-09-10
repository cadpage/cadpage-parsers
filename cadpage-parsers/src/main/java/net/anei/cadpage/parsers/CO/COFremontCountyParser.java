package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class COFremontCountyParser extends DispatchA33Parser {


  public COFremontCountyParser() {
    super("FREMONT COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "FRECOM911@FREMONTSO.COM,FRECOM911@PUBLICSAFETYSOFTWARE.NET,FRECOM911@OMNIGO.COM,FRECOM911@ITIUSA.COM,noreply@omnigo.com";
  }
}
