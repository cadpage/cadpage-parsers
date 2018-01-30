package net.anei.cadpage.parsers.IN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class INVermillionCountyParser extends DispatchA29Parser {
  
  public INVermillionCountyParser() {
    super(CITY_LIST, "VERMILLION COUNTY", "IN");
    setupCallList(CALL_LIST);
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
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strAddress.indexOf(',');
    if (pt >= 0) data.strAddress = data.strAddress.substring(0,pt).trim();
    data.strCity = data.strCity.toUpperCase();
    if (data.strCity.equals("FAIRVIEW")) data.strCity = "FAIRVIEW PARK";
    if (IL_CITY_SET.contains(data.strCity)) data.strState = "IL";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CITY", "CITY ST");
  }
  
  private static Pattern STREET_TH_PTN = Pattern.compile("\\b(\\d{4,}) *TH\\b");
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = STREET_TH_PTN.matcher(addr).replaceAll("$1");
    return super.adjustMapAddress(addr);
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ARSON",
      "ASSAULT",
      "CHECK WELL BEING",
      "CHEST PAINS",
      "RESCUE/AMBULANCE",
      "FIRE-BRUSH",
      "FIRE-GENERAL",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "FIRE ALARM",
      "GAS ODOR",
      "GAS SPILL",
      "LIFTING ASSISTANCE",
      "MEDICAL",
      "OVERDOSE",
      "PERSON DOWN",
      "SPEAK TO OFFICER",
      "SUICIDAL SUBJECT",
      "SUSPICIOUS PERSON",
      "TRAFFIC ACCIDENT-INJURIES",
      "TRAFFIC ACCIDENT-PROPERTY DAMAGE",
      "UNATTENDED DEATH"
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
