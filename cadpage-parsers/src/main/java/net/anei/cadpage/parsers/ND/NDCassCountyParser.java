package net.anei.cadpage.parsers.ND;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NDCassCountyParser extends SmartAddressParser {
  
  private static final Pattern DATE_TIME_CFS_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) CFS #:? (\\d+) ");
  private static final Pattern CALL_PFX_PTN = Pattern.compile("X - |X-SEND FIRE ", Pattern.CASE_INSENSITIVE);
  private static final Pattern UNIT_PTN = Pattern.compile("(?: \\d{3}| \\d{4}-[A-Z]+)+$");
 
  public NDCassCountyParser() {
    super(CITY_CODES, "CASS COUNTY", "ND");
    setFieldList("CALL ADDR APT CITY ST PLACE DATE TIME ID INFO UNIT");
    setupCallList(CALL_LIST);
    removeWords("ESTATES");
    setupMultiWordStreets(
        "MAPLE RIVER",
        "TURTLE LAKE"
    );
  }
  
  @Override
  public String getFilter() {
    return "dispatch@rrrdc.or,dispatch@cityoffargo.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = DATE_TIME_CFS_PTN.matcher(body);
    if (!match.find()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strCallId = match.group(3);
    String sAddr = body.substring(0,match.start()).trim();
    String sInfo = body.substring(match.end()).trim();
    
    String prefix = null;
    match = CALL_PFX_PTN.matcher(sAddr);
    if (match.lookingAt()) {
      prefix = match.group();
      sAddr = sAddr.substring(match.end());
    }
    
    sAddr = sAddr.replace("\\", "&");
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_IMPLIED_INTERSECT, sAddr, data);
    if (prefix != null) data.strCall = prefix + data.strCall;
    
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1);
      if (data.strState.equals("ND")) data.strState = "";
      data.strCity = data.strCity.substring(0,pt).trim();
    }
    
    String sPlace = getLeft();
    pt = sPlace.indexOf(" - ");
    if (pt >= 0) sPlace = sPlace.substring(0,pt).trim();
    if (sPlace.length() <= 1) sPlace = "";
    data.strPlace = sPlace;
    
    match = UNIT_PTN.matcher(sInfo);
    if (match.find()) {
      data.strUnit = match.group().trim();
      sInfo = sInfo.substring(0,match.start()).trim();
    }
    for (String info : sInfo.split("Nature Of Call:")) {
      info = info.trim();
      if (info.length() > data.strSupp.length()) data.strSupp = info;
    }
    return true;
  }
  
  private static CodeSet CALL_LIST = new CodeSet(
      "01 ABDOMINAL PAIN",
      "01 ABDOMINAL PAIN/PROBLEMS",
      "02 ALLERGIES/ENVENOMATIONS",
      "06 BREATHING PROBLEMS",
      "07 BURNS/SCALDS",
      "09 CARDIAC/RESPIRATORY ARREST",
      "09 CARDIAC/RESPIRATORY ARREST",
      "10 CHEST PAIN",
      "12 CONVULSIONS/SEIZURE",
      "13 DIABETIC PROBLEMS",
      "17 FALLS",
      "21 HEMORRAHAGE - LACERATIONS",
      "23 OVERDOSE - POISONING",
      "24 PREGNANCY - CHILDBIRTH",
      "26 SICK PERSON",
      "28 STROKE  - CVA",
      "30 TRAUMATIC INJURIES",
      "31 UNCONSCIOUS - FAINTING",
      "32 UNKNOWN PROBLEM - MAN DOWN",
      "33 TRANSFER INTERFACILITY",
      "ACCIDENT - INJURY",
      "ACCIDENT - PROPERTY",
      "AIRCRAFT CRASH",
      "ARCING WIRE/TRANSFORMER FIRE",
      "BKOA",
      "BON FIRE",
      "CARBON MONOXIDE DETECTOR",
      "COMMERCIAL FIRE",
      "DOMESTIC",
      "DUMPSTER FIRE",
      "GAS/FUEL SPILLS",
      "GAS LEAK",
      "GRASS FIRE",
      "HAZ-MAT INCIDENT",
      "IMPAIRED DRIVER",
      "IMPAIRED PERSON",
      "LIFTING ASSISTANCE",
      "MEDICAL",
      "MEDICAL-SEND FIRE",
      "MEDICAL ASSIST-OFFICER REQUESTED",
      "MENTALLY IMPAIRED",
      "MISC",
      "MISC FIRE",
      "MUTUAL AID FIRE",
      "RESCUE",
      "RESIDENTIAL FIRE",
      "SUICIDAL PERSON",
      "VEHICLE FIRE",
      "WATER BREAK/WASHED OUT ROAD"
  );
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ABSA", "ABSARAKA/ND",
      "ALIC", "ALICE/ND",
      "AMEN", "AMENIA/ND",
      "ARGU", "ARGUSVILLE/ND",
      "ARTH", "ARTHUR/ND",
      "AVIL", "AVERILL/MN",
      "AYR",  "AYR/ND",
      "BAKE", "BAKER/MN",
      "BARN", "BARNESVILLE/MN",
      "BORU", "BORUP/MN",
      "BRIA", "BRIARWOOD/ND",
      "BUFF", "BUFFALO/ND",
      "CASS", "",
      "CAST", "CASSELTON/ND",
      "CHAF", "CHAFFEE/ND",
      "CHRI", "CHRISTINE/ND",
      "CLAY", "CLAY COUNTY/MN",
      "CLIF", "CLIFFORD/ND",
      "COMS", "COMSTOCK/MN",
      "CROM", "CROMWELL TOWNSHIP/MN",
      "DALE", "DALE/MN",
      "DAVE", "DAVENPORT/ND",
      "DILW", "DILWORTH/MN",
      "DOWN", "DOWNER/MN",
      "DURB", "DURBIN/ND",
      "EGLO", "EGLON TOWNSHIP/MN",
      "ELKT", "ELKTON TOWNSHIP/MN",
      "ELMW", "ELMWOOD TOWNSHIP/MN",
      "EMBD", "EMBDEN/ND",
      "ENDE", "ENDERLIN/ND",
      "ERIE", "ERIE/ND",
      "FELT", "FELTON/MN",
      "FGO",  "FARGO/ND",
      "FING", "FINGAL/ND",
      "FLOW", "FLOWING TOWNSHIP/MN",
      "GALE", "GALESBURG/ND",
      "GARD", "GARDNER/ND",
      "GEOR", "GEORGETOWN/MN",
      "GLYN", "GLYNDON/MN",
      "GOOS", "GOOSE PRAIRIE TOWNSHIP/MN",
      "GRAN", "GRANDIN/ND",
      "HAGE", "HAGEN TOWNSHIP/MN",
      "HAWL", "HAWLEY/MN",
      "HICK", "HICKSON/ND",
      "HIGH", "HIGHLAND GROVE TOWNSHIP/MN",
      "HITT", "HITTERDAL/MN",
      "HOLY", "HOLY CROSS TOWNSHIP/MN",
      "HOPE", "HOPE/ND",
      "HORA", "HORACE",
      "HUMB", "HUMBOLDT TOWNSHIP/MN",
      "HUNT", "HUNTER/ND",
      "KEEN", "KEENE TOWNSHIP/MN",
      "KIND", "KINDRED/ND",
      "KRAG", "KRAGNESS TOWNSHIP/MN",
      "KURT", "KURTZ TOWNSHIP/MN",
      "LEON", "LEONARD/ND",
      "LPRK", "LAKE PARK/MN",
      "MAPL", "MAPLETON/ND",
      "MHD",  "MOORHEAD/MN",
      "MOLA", "MOLAND TOWNSHIP/MN",
      "MORK", "MORKEN TOWNSHIP/MN",
      "NARW", "HARWOOD/ND",
      "NEWR", "NEW ROCKFORD/ND",
      "NORA", "HORACE/ND",
      "NROT", "NORTH RIVER/ND",
      "OAKP", "OAKPORT TOWNSHIP/MN",
      "OXBO", "OXBOW/ND",
      "PAGE", "PAGE/ND",
      "PARK", "PARKE TOWNSHIP/MN",
      "PELI", "PELICAN RAPIDS/MN",
      "PERL", "PERLEY/MN",
      "PRAI", "PRAIRIE ROSE/ND",
      "PROS", "PROSPER/ND",
      "REIL", "REILE'S ACRES/ND",
      "RIVE", "RIVERTON TOWNSHIP/MN",
      "ROGE", "ROGERS/ND",
      "ROLL", "ROLLAG/MN",
      "RRON", "FRONTIER/ND",
      "RUST", "RUSTAD CITY/ND",
      "SABI", "SABIN/MN",
      "SHEL", "SHELDON/ND",
      "SKRE", "SKREE TOWNSHIP/MN",
      "SPRI", "SPRING PRAIRIETOWNSHIP/MN",
      "TANS", "TANSEM TOWNSHIP/MN",
      "TOWE", "TOWER CITY/ND",
      "TWIN", "TWIN VALLEY/MN",
      "ULEN", "ULEN/MN",
      "VIDI", "VIDING TOWNSHIP/MN",
      "WARR", "WARREN/MN",
      "WFGO", "WEST FARGO/ND",
      "WHEA", "WHEATLAND/ND",   // Changed from MN
      "WILD", "WILD RICE/ND",
      "WOLV", "WOLVERTON/MN",
      
      "BECKCO",          "BECKER COUNTY/MN",
      "RICHCO",          "RICHLAND COUNTY",
      "ROTHSAY",         "ROTHSAY/MN",
      "WALCOT",          "WALCOTT",
      "WALCOTT",         "WALCOTT",
      "WALCOT RICHCO",   "WALCOTT",
      "WALCOTT RICHCO",  "WALCOTT",
      "RICHCO WALCOT",   "WALCOTT",
      "RICHCO WALCOTT",  "WALCOTT",
      "WILKCO",          "WILKIN COUNTY/MN",
      "WILKCO ROTHSAY",  "ROTHSAY/MN",
      "WOLVERTON",       "WOLVERTON/MN"

  });
}
