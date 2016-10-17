package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class LAStCharlesParishParser extends DispatchB2Parser {
  
  private static final Pattern MISSING_PFX_PTN = Pattern.compile("[A-Z0-9]+>");
		
  public LAStCharlesParishParser() {
    super("911:", CITY_LIST, "ST CHARLES PARISH", "LA");
    setupCallList(CALL_LIST);
    setupSpecialStreets("HWY 90", "HWY 631","HWY 3127", "HWY 3142");
    setupMultiWordStreets(
      "ACADIAN OAKS",
      "AMERICAN BANK",
      "BAYOU GAUCHE",
      "BLUEBERRY HILL",
      "BOB AND LUCY",
      "BOUTTE ESTATES",
      "BRAYN V",
      "DOWN THE BAYOU",
      "FERRY INN",
      "GRAND BAYOU",
      "HUBCAP CITY",
      "J B GREEN",
      "JUDGE EDWARD DUFRESNE",
      "LISA ROBICHAUX",
      "LIVE OAK",
      "LULING ESTATE",
      "MAGNOLIA RIDGE",
      "NON SERV",
      "PAUL FREDRICK",
      "PAUL MAILLARD",
      "PURPLE MARTIN",
      "RIVER OAKS",
      "RIVER RIDGE",
      "SAN FRANCISCO",
      "ST ANTHONY",
      "ST JOHN",
      "ST MARIA",
      "TWIN BRIDGE",
      "UP THE BAYOU",
      "ZEE ANN"
    );
  }

  @Override
  public String getFilter() {
    return "911@stcharlessheriff.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (MISSING_PFX_PTN.matcher(body).lookingAt()) body = "911:" + body;
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    return true;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABNORMAL BREATHING",
      "ACCIDENT WITH INJURIES",
      "ATTEMPT SUICIDE",
      "AUTO ACCIDENT",
      "CHEMICAL LEAK",
      "CHEMICAL RELEASE/ODOR",
      "CLAMMY",
      "COMPLAINT",
      "CONTINUOUS/MULTI SEIZ/SEIZ HX",
      "DIFF SPEAKING BETWEEN BREATHS",
      "ELECTRICAL FIRE",
      "EMS ASSIST",
      "EXTRICATION",
      "FAINT AND ALERT >35 NO CARD HX",
      "FIRE ALARM",
      "FIRE ALARM SOUNDING",
      "GENERAL ALERT",
      "GROUND/PUBLIC ASSIST(NO INJ)",
      "HAZARD",
      "HIT AND RUN ACCIDENT",
      "LAND FIRE",
      "MOTOR VEHICLE ACCIDENT",
      "NON REC(>6HRS)INJ/NO PR SYMPTM",
      "NOT ALERT",
      "NOT BREATHING AT ALL",
      "NOT DANGEROUS BODY AREA",
      "ODOR",
      "OTHER EVENT NOT DEFINED",
      "PUBLIC ASSIST(NO INJ)",
      "RECKLESS DRIVING",
      "SEIZ OVER/BREATH EFF/(SEIZ HX)",
      "SEV RSP DSTRS DIF SPK BTWN",
      "STRUCTURE FIRE",
      "TRAFFIC INCIDENTS",
      "TRAFFIC/INJURIES",
      "TRAFFIC/UNK STAT OTH CODES NA",
      "TRANSPORTATION ONLY",
      "TRASH FIRE",
      "TRASH/GRASS FIRE",
      "TRAUMA INJ/POSS DANGEROUS AREA",
      "TRFC/HIGH MECH/AUTO-BIK/MTRCYC",
      "TRFC/HIGH MECH/AUTO-PEDESTRIAN",
      "TRFC/HIGH MECHANISM",
      "UNCONSCIOUS - EFFECTIVE BREATH",
      "UNKNOWN PROB/MED ALERT NOTIFY",
      "VEHICLE FIRE"

  );

  private static final String[] CITY_LIST = new String[] {
    "AMA", 
    "BAYOU GAUCHE",
    "BOUTTE",
    "DES ALLEMAND",
    "DES ALLEMANDS",
    "DESTREHAN",
    "HAHNVILLE",
    "KILLONA",
    "LULING",
    "MONTZ",
    "NEW SARPY",
    "NORCO",
    "PARADIS",
    "ST ROSE",
    "TAFT",
    
    "LAFOURCHE"
  };
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "DES ALLEMAND", "DES ALLEMANDS"
  });
}
