package net.anei.cadpage.parsers.NY;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class NYStLawrenceCountyParser extends DispatchA19Parser {
  
  public NYStLawrenceCountyParser() {
    super("ST LAWRENCE COUNTY", "NY");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "esspillmanalert@stlawco.org";
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "A LOT",                                "+44.592942,-75.157808",
      "ADMISSIONS",                           "+44.588479,-75.161936",
      "APPLETON ARENA",                       "+44.586157,-75.165817",
      "ATWOOD",                               "+44.592675,-75.163283",
      "B LOT",                                "+44.590562,-75.158112",
      "BETA",                                 "+44.593401,-75.162292",
      "BEWKES HALL",                          "+44.587804,-75.161074",
      "BREWERS BOOKSTORE",                    "+44.588748,-75.163247",
      "BROWN HALL",                           "+44.588260,-75.159806",
      "C LOT",                                "+44.588041,-75.157503",
      "CAMPUS KITCHENS",                      "+44.588345,-75.164706",
      "CANTON NY",                            "+44.595641,-75.169320",
      "CARNEGIE",                             "+44.590852,-75.162735",
      "CHAPEL",                               "+44.591471,-75.162878",
      "D LOT",                                "+44.585465,-75.163442",
      "DANA B. TORREY HEALTH CENTER",         "+44.590006,-75.164553",
      "DANA DINING HALL",                     "+44.589788,-75.162312",
      "DEAN-EATON",                           "+44.590815,-75.161670",
      "FACILITIES",                           "+44.586797,-75.157403",
      "GAINES",                               "+44.593855,-75.159805",
      "GRIFFITHS ART CENTER",                 "+44.592794,-75.160243",
      "HEPBURN",                              "+44.590412,-75.163068",
      "HULETT HALL",                          "+44.592801,-75.159091",
      "J LOT",                                "+44.586721,-75.159606",
      "JAVA & KSLU",                          "+44.588324,-75.159214",
      "JENCKS HALL",                          "+44.591706,-75.158719",
      "JOHNSON HALL OF SCIENCE",              "+44.588632,-75.161206",
      "KIRK DOUGLAS HALL",                    "+44.591784,-75.161264",
      "LEE EAST",                             "+44.589481,-75.158238",
      "LEE HALL",                             "+44.589415,-75.158570",
      "LEE NORTH",                            "+44.589640,-75.158651",
      "LEE WEST",                             "+44.589298,-75.158833",
      "LEITHEAD FIELD HOUSE",                 "+44.586447,-75.161823",
      "NEWELL FIELD HOUSE & MUNRO FAMILY CLIMBING WALL","+44.586365,-75.162977",
      "ODY LIBRARY",                          "+44.591353,-75.164236",
      "62 PARK",                              "+44.590787,-75.165416",
      "PISKOR",                               "+44.588955,-75.162331",
      "PRIEST RESIDENCE HALL",                "+44.588916,-75.165117",
      "REBERT EAST",                          "+44.589610,-75.159629",
      "REBERT HALL",                          "+44.589777,-75.160020",
      "REBERT NORTH",                         "+44.589943,-75.160153",
      "REBERT SOUTH",                         "+44.589537,-75.160036",
      "REIFF RESIDENCE HALL",                 "+44.589100,-75.164488",
      "RICHARDSON HALL",                      "+44.591994,-75.163145",
      "ROBBIE SQUASH COURTS",                 "+44.586709,-75.161310",
      "SAFETY AND SECURITY",                  "+44.589889,-75.164963",
      "STAFFORD FITNESS CENTER",              "+44.586639,-75.163526",
      "SULLIVAN STUDENT CENTER",              "+44.589857,-75.161399",
      "SYKES RESIDENCE HALL",                 "+44.589605,-75.163339",
      "THE ARTS ANNEX",                       "+44.593432,-75.160161",
      "THE NOBLE CENTER",                     "+44.592040,-75.160771",
      "VALENTINE HALL",                       "+44.587742,-75.160328",
      "VILAS HALL",                           "+44.592632,-75.162055",
      "WHITMAN ANNEX",                        "+44.590191,-75.159027",
      "WHITMAN HALL",                         "+44.590741,-75.159934"
 
  });
}
	