package net.anei.cadpage.parsers.MO;


import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Jasper County, MO
 */
public class MOJasperCountyParser extends DispatchOSSIParser {
 
  public MOJasperCountyParser() {
    super(CITY_CODES, "JASPER COUNTY", "MO",
           "FYI? CALL ( CHASING! | ADDR/Z! END | ( PLACE ADDR/Z CITY! | ADDR/Z CITY! | ADDR/Z! ) X? X? CODE? ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@jasco.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CHASING")) return new PlaceField("Unit [A-Z0-9]+ chasing");
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Z]", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALBA",   "ALBA",
      "APTD",   "AIRPORT DRIVE",
      "ASBR",   "ASBURY",
      "AVLA",   "AVILLA",
      "BRHT",   "BROOKLYN HEIGHTS",
      "CJCT",   "CARL JUNCTION",
      "CRTH",   "CARTHAGE",
      "CRTV",   "CARTERVILLE",
      "DNWG",   "DUENWEG",
      "DQSN",   "DUQUESNE",
      "FDLT",   "FIDELITY",
      "JPLN",   "JOPLIN",
      "JSPR",   "JASPER",
      "LRSL",   "LA RUSSELL",
      "NKCT",   "NECK CITY",
      "ORNG",   "ORONOGO",
      "PRCL",   "PURCELL",
      "REDS",   "REEDS",
      "SRCX",   "SARCOXIE",
      "WBCT",   "WEBB CITY",
      
      "CNTY",   "JASPER COUNTY"

  });
}