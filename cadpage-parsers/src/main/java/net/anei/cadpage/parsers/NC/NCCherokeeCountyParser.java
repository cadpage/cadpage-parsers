
package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCCherokeeCountyParser extends DispatchSouthernParser {

  public NCCherokeeCountyParser() {
    super(CITY_LIST, "CHEROKEE COUNTY", "NC", DSFLAG_TRAIL_PLACE);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@cherokeecounty-nc.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern NORTH_PTN = Pattern.compile("\\bNORTH\\b");
  private static final Pattern STREET_PTN = Pattern.compile("\\bSTREET\\b");
  
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    address = NORTH_PTN.matcher(address).replaceAll("N");
    address = STREET_PTN.matcher(address).replaceAll("ST");
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1457 CHESTNUT ST",                     "+36.615523,-88.321282",
      "1459 CHESTNUT ST",                     "+36.615713,-88.322963",
      "58 DORM CIR",                          "+36.617188,-88.322025",
      "176 DORM CIR",                         "+36.617535,-88.319584",
      "290 DORM CIR",                         "+36.619501,-88.320618",
      "298 DORM CIR",                         "+36.618664,-88.319455",
      "300 DORM CIR",                         "+36.617402,-88.320712",
      "358 DORM CIR",                         "+36.619541,-88.321911",
      "0 DORM CIR X",                         "+36.618263,-88.320921",
      "77 EDUCATION LOOP",                    "+36.614804,-88.322641",
      "99 EDUCATION LOOP",                    "+36.614361,-88.322333",
      "101 EDUCATION LOOP",                   "+36.614105,-88.322486",
      "120 EDUCATION LOOP",                   "+36.612976,-88.322747",
      "125 EDUCATION LOOP",                   "+36.614111,-88.322454",
      "147 EDUCATION LOOP",                   "+36.613406,-88.322511",
      "149 EDUCATION LOOP",                   "+36.613822,-88.322354",
      "178 EDUCATION LOOP",                   "+36.613017,-88.323613",
      "180 EDUCATION LOOP",                   "+36.613008,-88.322297",
      "400 EDUCATION LOOP",                   "+36.613526,-88.321467",
      "27 GILBERT GRAVES DR",                 "+36.617224,-88.318206",
      "95 GILBERT GRAVES DR",                 "+36.618082,-88.318173",
      "1401 HIGHWAY 121 BYPASS N",            "+36.622671,-88.320007",
      "301 N 14TH ST",                        "+36.612954,-88.320366",
      "311 N 14TH ST",                        "+36.614551,-88.320619",
      "300 N 16TH ST",                        "+36.611395,-88.323385",
      "302 N 16TH ST",                        "+36.611812,-88.323697",
      "401 N 16TH ST",                        "+36.613816,-88.324753",
      "201 N 15 ST",                          "+36.611069,-88.322714",
      "213 N 15 ST",                          "+36.611987,-88.322276",
      "210 N 15TH ST",                        "+36.611576,-88.322281",
      "240 N 16 ST",                          "+36.611044,-88.323723",
      "350 N 16 ST",                          "+36.612405,-88.323651",
      "402 N 16 ST",                          "+36.613455,-88.325841",
      "380 N 16TH ST",                        "+36.612537,-88.325829",
      "500 N 16TH ST",                        "+36.615034,-88.323461",
      "503 N 16TH ST",                        "+36.615206,-88.324036",
      "980 N 16TH ST",                        "+36.622371,-88.324093",
      "1304 PAYNE ST",                        "+36.615394,-88.317916",
      "1399 PAYNE ST",                        "+36.614715,-88.319621",
      "1000 RACER DR",                        "+36.620676,-88.320505",
      "50 REGENTS DR",                        "+36.615853,-88.318301",
      "1525 UNIVERSITY DR",                   "+36.610074,-88.322553",
      "810 WALDROP DR",                       "+36.618341,-88.322459",
      "906 WALDROP DR",                       "+36.619654,-88.322478"

  });
  
  private static final String[] CITY_LIST = new String[]{

  //CITIES
      "ANDREWS",
      "MURPHY",

  //CENSUS-DESIGNATED PLACE

      "MARBLE",

  //UNINCORPORATED COMMUNITIES

      "CULBERSON",
      "OWL CREEK",
      "TOPTON",
      "UNAKA",
      "WOODVILLE",
      "RANGER",
      "HANGING DOG",

  //TOWNSHIPS

      "BEAVERDAM",
      "BRASSTOWN",
      "HOTHOUSE",
      "MURPHY",
      "NOTLA",
      "SHOAL CREEK",
      "VALLEYTOWN"

  };
}
