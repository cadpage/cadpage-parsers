package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class NCMaconCountyParser extends DispatchB3Parser {
  
  private static final Pattern PRIMARY_PREFIX_PTN = Pattern.compile("MACON 911 MACON911:|MACON 911: MACON911:|911 CENTER:|MACON911:");
 
  public NCMaconCountyParser() {
    super(PRIMARY_PREFIX_PTN, CITY_LIST, "MACON COUNTY", "NC");
    removeWords("DRIVE");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldEnd(body, " Stop");
    if (! super.parseMsg(subject, body, data)) return false;
    data.strCross = data.strCross.replace('@', '/');
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ADDINGTON BRIDGE",
    "BAIRD COVE",
    "BREEZY KNOB",
    "BROOKSTONE FOREST",
    "BROOKSTONE VISTA",
    "BRYSON CITY",
    "BRYSON CTY",
    "BUSTER CABE",
    "CAT CREEK",
    "COWEETA CHURCH",
    "FOX LAYRE",
    "GOLF VIEW",
    "GREEN KNOB",
    "HEATHERS COVE",
    "HERITAGE HOLLOW",
    "HILAND PARK",
    "HOLLY HILLS VISTA",
    "HOLLY SPRINGS CHURCH",
    "INDUSTRIAL PARK",
    "JACK CABE",
    "JOHN JUSTICE",
    "KINSLAND PARK",
    "LAKE EMORY",
    "LEATHERMAN GAP",
    "LEE TALLENT",
    "LOUISA CHAPEL",
    "LYLE DOWNS",
    "MAPLES PARK",
    "MISTY RIDGE",
    "NOAH GIBSON",
    "NORTH BLAINE BRANCH",
    "ONE CENTER",
    "ORCHARD VIEW",
    "PHILLIPS ST TO",
    "POINT PLEASANT",
    "PRESSLEY CIRCLE",
    "RABBIT CREEK",
    "ROLLER MILL",
    "ROSE CREEK",
    "SANDERSTOWN RIDGE",
    "SOUTH FRONT",
    "TOWN MOUNTAIN",
    "TRIMONT MOUNTAIN",
    "VALLEY VIEW",
    "VAN RAALTE",
    "WHITE PINE",
    "WIDE HORIZON"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "BREAKING AND ENTERING",
      "BRUSH FIRE",
      "DOA",
      "DOMESTIC DISTURBANCE",
      "EMERGENCY RUN",
      "FIGHT IN PROGRESS",
      "FIRE ALARM",
      "INVESTIGATE ---- ?",
      "NON EMERGENCY RUN",
      "POSS SUICIDE",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE",
      "VEHICLE ACCIDENT",
      "VEHICLE FIRE"
  );

  private static final String[] CITY_LIST = new String[]{
    "FRANKLIN", 
    "HIGHLANDS", 
    "OTTO",
    "FRANKLIN TWP", 
    "HIGHLANDS TWP", 
    "SUGARFORK TWP", 
    "BURNINGTOWN TWP",
    "CARTOOGECHAYE TWP", 
    "ELLIJAY TWP", 
    "MILLSHOAL TWP",
    "NANTAHALA TWP", 
    "SMITHBRIDGE TWP",
  };
}
