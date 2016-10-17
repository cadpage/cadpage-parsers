package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCOrangeCountyAParser extends DispatchOSSIParser {
  
  public NCOrangeCountyAParser() {
    super(CITY_LIST, "ORANGE COUNTY", "NC",
           "ID? ADDR CITY? CALL! CH? CODE? ID? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@cedargrovefire.org,cadpage@orangeem.org,cad@orangecountync.gov,Orange Co EMS Dispatch,CFDFireCallsDistList@townofcarrboro.org,CHFDdispatch@townofchapelhill.org";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7,10}", true);
    if (name.equals("CH")) return new ChannelField("OPS.*", true);
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CARRBORO",
      "CHAPEL HILL",
      "DURHAM",
      "HILLSBOROUGH",
      "MEBANE",
      
      // Townships
      "BINGHAM TWP",
      "CEDAR GROVE TWP",
      "CHAPEL HILL TWP",
      "CHEEKS TWP",
      "ENO TWP",
      "HILLSBOROUGH TWP",
      "LITTLE RIVER TWP",
      
      // Unincorporated Communities
      "BLACKWOOD",
      "BUCKHORN",
      "CALDWELL",
      "CALVANDER",
      "CARR",
      "CEDAR GROVE",
      "DODSONS CROSSROADS",
      "DOGWOOD ACRES",
      "EFLAND",
      "ENO",
      "EUBANKS",
      "FAIRVIEW, ",
      "HILLSBOROUGH",
      "HURDLE MILLS",
      "LAWS",
      "MCDADE",
      "MILES",
      "OAKS",
      "ORANGE GROVE",
      "PINEY GROVE",
      "ROUGEMONT",
      "SCHLEY",
      "TEER",
      "UNIVERSITY",
      "WHITE CROSS",


  };
}
