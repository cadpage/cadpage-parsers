package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHAthensCountyParser extends DispatchEmergitechParser {
  
  public OHAthensCountyParser() {
    super(new String[]{"911_Dispatch:", "ohac911-dpfeiffer:"}, 
          false, null, CITY_LIST, "ATHENS COUNTY", "OH", TrailAddrType.INFO);
    setupMultiWordStreets(MWORD_STREET_LIST);
    addSpecialWords("COLUMBUS");
    setupSpecialStreets("LIGHTFRITZ RIDGE", "OREGON RIDGE", "COURT");
  }
  
  @Override
  public String getFilter() {
    return "911_Dispatch@athensoh.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.startsWith("CALLBK=")) {
      data.strPhone = data.strApt.substring(7).trim();
      data.strApt = "";
    }
    if (data.strSupp.length() == 0) {
      data.strSupp = data.strName;
      data.strName = "";
    }
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ANGEL RIDGE",
    "BETHANY RIDGE",
    "BIG BAILEY RUN",
    "BIG RUN",
    "BUCKLEY RUN",
    "BUCKS LAKE",
    "CANAANVILLE HILLS",
    "CARRIAGE HILL",
    "CIRCLE 33",
    "CONGRESS RUN",
    "COOLVILLE RIDGE",
    "EAST PARK",
    "ELM ROCK",
    "FIVE POINTS",
    "FOSSIL ROCK",
    "FOUR MILE CREEK",
    "GLOUSTER GLEN",
    "GRAHAM CHAPEL",
    "GRAND PK",
    "GREEN BRANCH",
    "GREEN VALLEY",
    "GUN CLUB",
    "HAGA RIDGE",
    "HAMLEY RUN",
    "HAMLEY SHORT CUT",
    "HAPPY HOLLOW",
    "HEALTH CENTER",
    "HIGH SCHOOL",
    "JENKINS DAM",
    "JEWELL HOLLOW",
    "JOHN LLOYD EVANS MEMORIAL",
    "LADD RIDGE",
    "LIGHTFRITZ RIDGE",
    "LONG RUN",
    "LONGVIEW HEIGHTS",
    "M E",
    "MILL SCHOOL",
    "MONSERAT RIDGE",
    "MT ST MARY",
    "MUSH RUN",
    "PARK LN",
    "PEACH RIDGE",
    "PEARL WOOD",
    "PERRY RIDGE",
    "PLEASANT HILL",
    "ROBINETTE RIDGE",
    "ROCK RUN",
    "SAND RIDGE",
    "SAND ROCK",
    "SAWYERS RUN",
    "TAYLOR RIDGE",
    "TEN SPOT",
    "UNIVERSITY ESTATES",
    "UTAH RIDGE",
    "VORE RIDGE"
  };

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "ATHENS",
    "NELSONVILLE",
    
    // Villages
    "ALBANY",
    "AMESVILLE",
    "BUCHTEL",
    "CHAUNCEY",
    "COOLVILLE",
    "GLOUSTER",
    "JACKSONVILLE",
    "TRIMBLE",
    
    // Townships
    "ALEXANDER TWP",
    "AMES TWP",
    "ATHENS TWP",
    "BERN TWP",
    "CANAAN TWP",
    "CARTHAGE TWP",
    "DOVER TWP",
    "LEE TWP",
    "LODI TWP",
    "ROME TWP",
    "TRIMBLE TWP",
    "TROY TWP",
    "WATERLOO TWP",
    "YORK TWP",

    // Census-designated place
    "THE PLAINS",

    // Other Communities
    "BEEBE",
    "CANAANVILLE",
    "CARBONDALE",
    "DOANVILLE",
    "ENTERPRISE",
    "FROST",
    "GUYSVILLE",
    "HEBBARDSVILLE",
    "HOCKINGPORT",
    "IMPERIAL",
    "KILVERT",
    "MILLFIELD",
    "MINERAL",
    "NEW ENGLAND",
    "NEW FLOODWOOD",
    "NEW MARSHFIELD",
    "REDTOWN",
    "SHADE",
    "SHARPSBURG",
    "STEWART",
    
    // Hocking County
    "BUTCHEL",
    "MURRAY CITY",
    
    "GREEN TWP",
    "STARR TWP",
    "WARD TWP",
    "WASHINGTON TWP",
  };
}
