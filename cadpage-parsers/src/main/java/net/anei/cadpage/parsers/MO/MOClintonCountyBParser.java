package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOClintonCountyBParser extends DispatchBCParser {
  
  
  public MOClintonCountyBParser() {
    this("CLINTON COUNTY", "MO");
  }
  
  MOClintonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@CAMERONMO.COM,DISPATCH@MAIL.PUBLICSAFETYSOFTWARE.NET,DISPATCH@OMNIGO.COM";
  }
}