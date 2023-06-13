package net.anei.cadpage.parsers.IN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class INVermillionCountyParser extends DispatchA29Parser {

  public INVermillionCountyParser() {
    super(CITY_LIST, "VERMILLION COUNTY", "IN");
    setupCities(MISSPELLED_CITIES);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "dispatch@vcsheriff.com";
  }

  private static final Pattern ST_BERN_PTN = Pattern.compile("\\bST. BERN\\b");
  @Override
  public boolean parseMsg(String body, Data data) {
    body = ST_BERN_PTN.matcher(body).replaceAll("ST_BERN");
    body = body.replace("BLK DIMD", "BLK_DIMD");
    body = body.replace(":#", ":");
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strAddress.indexOf(',');
    if (pt >= 0) data.strAddress = data.strAddress.substring(0,pt).trim();
    data.strCity = data.strCity.toUpperCase();
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    if (IL_CITY_SET.contains(data.strCity)) data.strState = "IL";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CITY", "CITY ST");
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_IMPLIED_INTERSECT;
  }

  private static Pattern STREET_TH_PTN = Pattern.compile("\\b(\\d{4,}) *TH\\b");

  @Override
  public String adjustMapAddress(String addr) {
    addr = STREET_TH_PTN.matcher(addr).replaceAll("$1");
    return super.adjustMapAddress(addr);
  }

  private static final String[] MWORD_STREET_LIST = new String[] {
      "CROWN HILL",
      "GENEAVA HILLS",
      "GENEVA HILLS",
      "HAZEL BLUFF",
      "LAKE VIEW",
      "SAINT PAUL",
      "SILVER ISLAND",
      "STILL ALARM KIBBY ST PIKE"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALARM-BURGLAR-RESIDENTIAL",
      "ARSON",
      "ASSIST ANOTHER AGENCY",
      "ASSAULT",
      "CHECK WELL BEING",
      "CHEST PAINS",
      "CO2 DETECTOR ALARM",
      "DOMESTIC COMPLAINT",
      "RESCUE/AMBULANCE",
      "FIRE-BRUSH",
      "FIRE-GENERAL",
      "FIRE-HAZMATS INVOLVED",
      "FIRE-LINE TROUBLE/STILL ALARM",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "FIRE ALARM",
      "GAS ODOR",
      "GAS SPILL",
      "HARRASSMENT",
      "INMATE DOWN",
      "JUVENILE PROBLEMS",
      "LIFTING ASSISTANCE",
      "MEDICAL",
      "MENTAL SUBJECT",
      "OVERDOSE",
      "PERSON DOWN",
      "POPULAR ST IN ALLY WAY",
      "POSS. HEART ATTACK",
      "POSS. STROKE",
      "POWER FAILURE",
      "SEIZURES",
      "SPEAK TO OFFICER",
      "SUICIDAL SUBJECT",
      "SUSPICIOUS PERSON",
      "TRAFFIC ACCIDENT-INJURIES",
      "TRAFFIC ACCIDENT-PROPERTY DAMAGE",
      "TRAFFIC HAZARD",
      "TROUBLE BREATHING",
      "UNATTENDED DEATH",
      "UNKNOWN PROBLEM",
      "UNRESPONSIVE/UNCONCIOUS",
      "UNWANTED SUBJECT - REMOVE"
  );

  private static final String[] CITY_LIST = new String[]{
    "ALTA",
    "BLANFORD",
    "BONO",
    "CAYUGA",
    "CENTENARY",
    "CLINTON",
    "DANA",
    "EUGENE",
    "FAIRVIEW",
    "FAIRVIEW PARK",
    "FLAT IRON",
    "GESSIE",
    "HIGHLAND",
    "HILLSDALE",
    "JONESTOWN",
    "KLONDYKE",
    "NEWPORT",
    "PERRYSVILLE",
    "QUAKER",
    "RANDALL",
    "RILEYSBURG",
    "SAINT BERNICE",
    "SANDYTOWN",
    "SUMMIT GROVE",
    "SYNDICATE",
    "ST BERNICE",
    "SUMMIT GROVE",
    "TORONTO",
    "TREE SPRING",
    "UNIVERSAL",

    "CLINTON TWP",
    "EUGENE TWP",
    "HELT TWP",
    "HIGHLAND TWP",
    "VERMILLION TWP",

    // Edgar County, IL
    "EDGAR CO",
    "BROULETTS CREEK TWP",
    "EDGAR TWP",
    "HUNTER TWP",
    "PARRIS TWP",
    "ROSS TWP",
    "PRAIRIE TWP",
    "CHRISMAN",

    // Fountain County
    "COVINGTON",
    "FOUNTAIN CO",
    "FULTON TWP",
    "TROY TWP",
    "WABASH TWP",
    "CONVINGTON",

    // Parke County
    "PARKE CO",
    "LIBERTY TWP",
    "RESERVE TWP",
    "WABASH TWP",
    "FLORIDA TWP",
    "COXVILLE",
    "HOWARD",
    "JESSUP",
    "MECCA",
    "LODI",
    "LYFORD",
    "MONTEZUMA",
    "ROSEDALE",
    "SYLVANIA",
    "TANGIER",

    // Vermilion County, IL
    "VERMILION CO",
    "DANVILLE TWP",
    "ELMWOOD TWP",
    "GEORGETOWN TWP",
    "LOVE TWP",
    "MCKENDREE TWP",
    "NEWELL TWP",
    "BELGIUM",
    "DANVILLE",
    "GEORGETOWN",
    "RIDGE FARM",
    "TILTON",
    "WESTVILLE",

    // Vigo County
    "VIGO CO",
    "FAYETTE TWP",
    "NEVINS TWP",
    "OTTER CREEK TWP",
    "NEW GOSHEN",
    "SHEPARDSVILLE",
    "VIGO",

    // Warren County
    "WARREN CO",
    "KENT TWP",
    "MOUND TWP",
    "CARBONDALE",
    "FOSTER",
    "STATE LINE CITY"
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "CHRISTMAN",      "CHRISMAN",
      "FAIRVIEW",       "FAIRVIEW PARK"
  });

  private static final Set<String> IL_CITY_SET = new HashSet<String>(Arrays.asList(
      // Edgar County, IL
      "EDGAR CO",
      "BROULETTS CREEK TWP",
      "EDGAR TWP",
      "HUNTER TWP",
      "PARRIS TWP",
      "ROSS TWP",
      "PRAIRIE TWP",
      "CHRISMAN",

      // Vermilion County, IL
      "VERMILION CO",
      "DANVILLE TWP",
      "ELMWOOD TWP",
      "GEORGETOWN TWP",
      "LOVE TWP",
      "MCKENDREE TWP",
      "NEWELL TWP",
      "BELGIUM",
      "DANVILLE",
      "GEORGETOWN",
      "RIDGE FARM",
      "TILTON",
      "WESTVILLE"
  ));
}
