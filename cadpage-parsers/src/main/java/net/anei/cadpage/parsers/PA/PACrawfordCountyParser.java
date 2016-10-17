package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;


public class PACrawfordCountyParser extends DispatchB3Parser {
  
  private static final Pattern PREFIX_PTN = Pattern.compile("(?:CRAWFORD COUNTY +911:? )?(?:CRAWFORD_COUNTY_911:|OESCAD:|OESCAD@WINDSTREAM.NET:)");

  public PACrawfordCountyParser() {
    super(PREFIX_PTN, CITY_LIST, "CRAWFORD COUNTY", "PA");
    removeWords("CIRCLE", "TRL");
    
    // Normally DispatchB3Parser subclasses do not need the call and multiple
    // word street lists because the subject splits them out nicely.  But
    // Crawford County sends text alerts in the default DispatchB2Parser fallback
    // format, so we have to cover that case as well.
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "OESCAD@WINDSTREAM.NET,CRAWFORD_COUNTY_911@oescad.com,CRAWFORD_COUNTY_911@CrawfordPA911.com,4702193542";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return WASHINGTON_STREET_EXT.matcher(address).replaceAll("$1");
  }
  private static final Pattern WASHINGTON_STREET_EXT = Pattern.compile("\\b(WASHINGTON ST(?:REET)?) EXT?\\b");
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("Reply STOP ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    if (subject.length() > 0) {
      if (subject.indexOf('>') < 0) {
        if (body.indexOf("= DSP ") < 0) {
          Matcher match = PREFIX_PTN.matcher(body);
          if (!match.lookingAt()) return false;
          setFieldList("INFO");
          data.msgType = MsgType.GEN_ALERT;
          data.strSupp = append(subject, " ", body.substring(match.end()).trim());
          return true;
        }
      }
    }
    
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.toUpperCase().endsWith(" BORO")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-5).trim();
    } else if (data.strCity.endsWith(" CO")) {
      data.strCity += "UNTY";
      if (data.strCity.equals("VNG COUNTY")) data.strCity = "VENANGO COUNTY";
    } else if (data.strCity.equals("LEBOUF TWP")) data.strCity = "LEBOEUF TWP";
    
    if (data.strCallId.length() == 0) data.expectMore = true;
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "ATLANTIC LAKE",
      "BEAVER CENTER",
      "BLOOMING VALLEY",
      "BOCKMAN HOLLOW",
      "BRICK CHURCH",
      "BROWN HILL",
      "CAMBRIDGE SPRINGS",
      "CAPTAIN WILLIAMS",
      "CLEVELAND BROWNS",
      "CONNEAUT LAKE",
      "COUNTY LINE",
      "CRYSTAL LAKE",
      "DARI DELL",
      "DRAKE HILL",
      "DUTCH HILL",
      "FOREST GREEN",
      "FRENCH CREEK",
      "GOSPEL HILL",
      "GRANGE CENTER",
      "GRAVEL PIT",
      "GRAVEL RUN",
      "GUYS MILLS",
      "HATCH HILL",
      "HIGBY HILL",
      "HIPPLE HILL",
      "HOGBACK WEST",
      "JIM TOBIN",
      "JOHN BROWN",
      "KIRILA LAKE",
      "LAKE CREEK",
      "LAKE FRONT",
      "LAKE RIDGE",
      "LEBOEUF TRAIL",
      "LEBOEUF TRL",
      "LITTLE COOLEY",
      "MAPLE HILL",
      "MILLER HILL",
      "MILLER STATION",
      "MORNING VIEW",
      "MOUNT PLEASANT",
      "MYSTIC PARK",
      "NICKEL PLATE",
      "OAKLAND BEACH",
      "OIL CREEK",
      "OWEN HILL",
      "PARK LANE",
      "PEPPERS TRAILER",
      "POST OFFICE",
      "POST RIDGE",
      "REASH CHURCH",
      "REEDS CORNERS",
      "RIDGE VIEW",
      "RIDGEWAY HILL",
      "ROGERS FERRY",
      "ROUND TOP",
      "SANDY LAKE",
      "SHADY ACRES",
      "SHERRED HILL",
      "SMITH HEATH",
      "SPOTTED FAWN",
      "STAR ROUTE",
      "STEEN HILL",
      "STONEY POINT",
      "SUGAR CREEK",
      "SUGAR LAKE",
      "SWIFT HILL",
      "TOURS END",
      "TROY CENTER",
      "WATSON RUN",
      "WHITE HILL",
      "WHITE OAK",
      "WILLOW RUN",
      "YANKEE HILL"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ALLERGIC REACTION",
      "ALTERED LOC",
      "ASSAULT",
      "ASSIST OTHER AGENCY",
      "ATV ACCIDENT",
      "BEHAVIORAL DISORDER",
      "BLEEDING",
      "BRUSH FIRE",
      "BURGLAR ALARM",
      "CARBON MONOXIDE INVESTIGATION",
      "CARDIAC ARREST",
      "CARDIAC SYMPTOMS",
      "CHECK WELFARE",
      "CHOKING VICTIM",
      "CHEST PAINS",
      "CITIZENS ASSIST",
      "CONTROLLED BURN",
      "DEBRIS ON ROADWAY",
      "DIABETIC",
      "DIFFICULTY BREATHING",
      "DISABLED VEHICLE",
      "DIZZINESS",
      "DOMESTIC",
      "DRILL",
      "EMS/QRS DEPT OUT OF SERVICE",
      "EMS TRANSPORT",
      "ENVIROMENTAL EMERGENCY",
      "FALL VICTIM",
      "FIRE ALARM",
      "FIRE STANDBY",
      "FLU LIKE SYMPTOMS",
      "FLOODING",
      "GENERAL ILLNESS",
      "GI PROBLEM",
      "HEAD INJURY",
      "HIT & RUN",
      "LACERATION",
      "LIFT ASSIST",
      "LIMITED EMS SERVICE",
      "MATERNITY",
      "MEDICAL ALARM",
      "MISC FIRE",
      "MISSING PERSON",
      "MVA NO INJURY",
      "MVA UNKNOWN INJURY OR ENTRAP",
      "MVA WITH ENTRAPMENT ONLY",
      "MVA WITH INJURY",
      "MVA WITH INJURY & ENTRAPMENT",
      "NATURAL GAS LEAK",
      "ODOR INVESTIGATION",
      "OVERDOSE",
      "PAIN GENERAL",
      "PEDESTRIAN STRUCK",
      "POSSIBLE DOA",
      "POST SURGICAL COMPLICATION",
      "PUBLIC SERVICE DETAIL",
      "RECKLESS DRIVING",
      "REKINDLE",
      "SEARCH",
      "SEIZURES",
      "SMOKE INVESTIGATION",
      "STROKE/CVA",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE W ENTRAPMENT",
      "SUSPICIOUS PERSON",
      "SYNCOPE",
      "TRAFFIC CONTROL",
      "TRAMATIC INJURY",
      "TREES WIRES DOWN NON-URGENT",
      "TREES/WIRES DOWN URGENT",
      "UNCONSCIOUS SUBJECT",
      "UNKNOWN",
      "UNKNOWN TYPE FIRE",
      "UNWANTED SUBJECT",
      "UTILITY EMERGENCY",
      "VEHICLE FIRE",
      "WARRANT SERVICE"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "MEADVILLE",
    "TITUSVILLE",
    
    "MEADVILLE CITY",
    "TITUSVILLE CITY",
    
    // Boroughs
    "BLOOMING VALLEY",
    "CAMBRIDGE SPRINGS",
    "CENTERVILLE",
    "CONNEAUT LAKE",
    "CONNEAUTVILLE",
    "COCHRANTON",
    "HYDETOWN",
    "LINESVILLE",
    "SAEGERTOWN",
    "SPARTANSBURG",
    "SPRINGBORO",
    "TOWNVILLE",
    "VENANGO",
    "WOODCOCK",

    "BLOOMING VALLEY BORO",
    "CAMBRIDGE SPRINGS BORO",
    "CENTERVILLE BORO",
    "CONNEAUT LAKE BORO",
    "CONNEAUTVILLE BORO",
    "COCHRANTON BORO",
    "HYDETOWN BORO",
    "LINESVILLE BORO",
    "SAEGERTOWN BORO",
    "SPARTANSBURG BORO",
    "SPRINGBORO BORO",
    "TOWNVILLE BORO",
    "VENANGO BORO",
    "WOODCOCK BORO",

    // Townships
    "ATHENS TWP",
    "BEAVER TWP",
    "BLOOMFIELD TWP",
    "CAMBRIDGE TWP",
    "CONNEAUT TWP",
    "CUSSEWAGO TWP",
    "EAST FAIRFIELD TWP",
    "EAST FALLOWFIELD TWP",
    "EAST MEAD TWP",
    "EAST MEAD TOWNSHIP",
    "FAIRFIELD TWP",
    "GREENWOOD TWP",
    "HAYFIELD TWP",
    "NORTH SHENANGO TWP",
    "OIL CREEK TWP",
    "PINE TWP",
    "RANDOLPH TWP",
    "RICHMOND TWP",
    "ROCKDALE TWP",
    "ROME TWP",
    "SADSBURY TWP",
    "SOUTH SHENANGO TWP",
    "SPARTA TWP",
    "SPRING TWP",
    "STEUBEN TWP",
    "SUMMERHILL TWP",
    "SUMMIT TWP",
    "TROY TWP",
    "UNION TWP",
    "VENANGO TWP",
    "VERNON TWP",
    "WAYNE TWP",
    "WEST FALLOWFIELD TWP",
    "WEST MEAD TWP",
    "WEST SHENANGO TWP",
    "WOODCOCK TWP",
    
    // Ashtabula County
    "ASHTABULA CO",
    
    // Erie County
    "ERIE CO",
    "ERIE COUNTY",
    "LEBOUF TWP",  // Misspelled
    "LEBOEUF TWP",
    "SUMMMIT TWP",
    "SUMMIT TOWNSHIP",
    
    // Mercer County
    "MERCER COUNTY",
    "MERCER CO",
    
    "FRENCH CREEK",
    "SANDY LAKE",
    
    "DEER CREEK TWP",
    "FRENCH CREEK TWP",
    
    // Venango County
    "VNG CO",
    "PLUM TWP"
  };
}
