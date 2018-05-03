package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
    removeWords("CIRCLE", "LANE", "TRL");
    setupSaintNames("JAMES");
    
    // Normally DispatchB3Parser subclasses do not need the call and multiple
    // word street lists because the subject splits them out nicely.  But
    // Crawford County sends text alerts in the default DispatchB2Parser fallback
    // format, so we have to cover that case as well.
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "OESCAD@WINDSTREAM.NET,CRAWFORD_COUNTY_911@oescad.com,CRAWFORD_COUNTY_911@CrawfordPA911.com,4702193542,8573032010,777";
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
    else if (data.strCity.equals("FRENCK CREEK TWP")) data.strCity = "FRENCH CREEK TWP";
    
    if (OHIO_CITIES.contains(data.strCity)) data.strState = "OH";
    
    if (data.strCallId.length() == 0) data.expectMore = true;
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CITY", "CITY ST");
  }
  
  @Override
  public Field getField(String name) {
    if(name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      return super.checkParse(field, data);
    }
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      parse(field, data);
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "ATLANTIC LAKE",
      "BEAVER CENTER",
      "BELL HILL",
      "BLOOMING VALLEY",
      "BLUE FALLS",
      "BOCKMAN HOLLOW",
      "BOY SCOUT",
      "BRADLEY TOWN",
      "BRICK CHURCH",
      "BROWN HILL",
      "BRUNOT CORNERS",
      "CAMBRIDGE SPRINGS",
      "CAPTAIN WILLIAMS",
      "CHUCK A LOU",
      "CLEVELAND BROWNS",
      "CONNEAUT LAKE",
      "COUNTRY ACRES TRAILER",
      "COUNTY LINE",
      "CRYSTAL LAKE",
      "DARI DELL",
      "DE VILLARS",
      "DIBBLE HILL",
      "DRAKE HILL",
      "DUTCH HILL",
      "ELLIS HILL",
      "FOREST GREEN",
      "FRENCH CREEK",
      "GOLF COURSE",
      "GOSPEL HILL",
      "GRANGE CENTER",
      "GRAVEL PIT",
      "GRAVEL RUN",
      "GUYS MILLS",
      "HATCH HILL",
      "HICKORY CORNERS",
      "HIGBY HILL",
      "HIPPLE HILL",
      "HOGBACK WEST",
      "HUMES HILL",
      "HUNTERS RIDGE",
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
      "MIKE WOOD",
      "MILLER HILL",
      "MILLER STATION",
      "MORNING VIEW",
      "MOSS GROVE",
      "MOUNT PLEASANT",
      "MYSTIC PARK",
      "NEWTON TOWN",
      "NICKEL PLATE",
      "OAKLAND BEACH",
      "OIL CREEK",
      "OWEN HILL",
      "PARK LANE",
      "PEACH BLOSSOM",
      "PEPPERS TRAILER",
      "PINEVIEW CAMPLANDS",
      "POST OFFICE",
      "POST RIDGE",
      "REASH CHURCH",
      "REEDS CORNERS",
      "RIDGE VIEW",
      "RIDGEWAY HILL",
      "ROCK CREEK",
      "ROCKY GLEN",
      "ROGERS FERRY",
      "ROUND TOP",
      "RUSSELLS COTTAGE",
      "SAINT JOHN",
      "SANDY LAKE",
      "SHADY ACRES",
      "SHEAKLEYVILLE - GREENVILLE",
      "SHERRED HILL",
      "SMITH HEATH",
      "SPOTTED FAWN",
      "STANHOPE KELLOGSVILLE",
      "STAR ROUTE",
      "STATE PARK ACCESS",
      "STEEN HILL",
      "STONEY POINT",
      "STORM FELL",
      "SUGAR CREEK",
      "SUGAR LAKE",
      "SWIFT HILL",
      "THREE BRIDGES",
      "TOURS END",
      "TROY CENTER",
      "TURKEY TRACK",
      "WALTON HILL",
      "WATSON RUN",
      "WHITE HILL",
      "WHITE OAK",
      "WILLOW RUN",
      "YANKEE HILL"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 CALL CHECK",
      "ALLERGIC REACTION",
      "ALTERED LOC",
      "ASSAULT",
      "ASSIST OTHER AGENCY",
      "ATV ACCIDENT",
      "BEHAVIORAL DISORDER",
      "BLEEDING",
      "BRUSH FIRE",
      "BURGLAR ALARM",
      "BURN VICTIM",
      "CAD TEST",
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
      "INTOXICATED SUBJECT",
      "JUVENILE OFFENSE",
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
      "POISONING",
      "POSSIBLE DOA",
      "POST SURGICAL COMPLICATION",
      "PUBLIC SERVICE DETAIL",
      "RECKLESS DRIVING",
      "REKINDLE",
      "SEARCH",
      "SEIZURES",
      "SMOKE INVESTIGATION",
      "STABBING",
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
    
    "OHIO",
    
    // Ashtabula County, Ohio
    "ASHTABULA",
    "ASHTABULA CO",
    "ASHTABULA COUNTY",
    
    "ANDOVER TWP",
    "PIERPONT TWP",
    "WILLIAMSFIELD TWP",
    
    "ANDOVER",
    "PIERPONT",
    "WILLIAMSFIELD",
    
    // Erie County
    "ERIE",
    "ERIE CO",
    "ERIE COUNTY",
    
    "CONCORD TWP",
    "CONNEAUT TWP",
    "ELK CREEK TWP",
    "FRANKLIN TWP",
    "LEBOUF TWP",  // Misspelled
    "LEBOEUF TWP",
    "SUMMMIT TWP",
    "SUMMIT TOWNSHIP",
    "UNION TWP",
    "WASHINGTON TWP",

    "CONCORD",
    "CONNEAUT",
    "ELK CREEK",
    "FRANKLIN",
    "LEBOEUF",
    "SUMMMIT",
    "UNION",
    "WASHINGTON",
    
    "ALBION",
    "CORRY",
    "CRANESVILLE",
    "EDINBORO",
    "ELGIN",
    "MILL VILLAGE",
    "UNION CITY",
    
    // Mercer County
    "MERCER",
    "MERCER COUNTY",
    "MERCER CO",
    
    "DEER CREEK TWP",
    "FRENCH CREEK TWP",
    "FRENCK CREEK TWP",  // Misspelled
    "GREENE TWP",
    "HEMPFIELD TWP",
    "MILL CREEK TWP",
    "NEW VERNON TWP",
    "OTTER CREEK TWP",
    "PERRY TWP",
    "SALEM TWP",
    "SANDY CREEK TWP",
    "SUGAR GROVE TWP",
    
    "DEER CREEK",
    "FRENCH CREEK",
    "GREENE",
    "HEMPFIELD",
    "NEW VERNON",
    "OTTER CREEK",
    "PERRY",
    "SALEM",
    "SANDY CREEK",
    "SANDY LAKE",
    "SUGAR GROVE",
    
    "GREENVILLE",
    "JAMESTOWN",
    "NEW LEBANON",
    "SHEAKLEYVILLE",
    
    // Trumbull County, Ohio
    "TRUMBULL",
    "TRUMBULL COUNTY",
    "TRUMBULL CO",
    
    "KINSMAN TWP",
    
    "KINSMAN",
    "VERNON",
    
    // Venango County
    "VENANGO",
    "VENANGO CO",
    "VENANGO COUNTY",
    "VNG CO",
    "VNG COUNTY",
    
    "CANAL TWP",
    "JACKSON TWP",
    "PLUM TWP",
    
    "CANAL",
    "COOPERSTOWN",
    "JACKSON",
    "PLUM",
    
    "COOPERSVILLE",
    "CHAPMANVILLE",
    "UTICA",
    
    // Warren County
    "WARREN",
    "WARREN CO",
    "WARREN COUNTY",
    
    "COLUMBUS TWP",
    "SPRING CREEK TWP",
    "ELDRED TWP",
    "SOUTHWEST TWP",
    
    "COLUMBUS",
    "SPRING CREEK",
    "ELDRED",
    "SOUTHWEST"
  };
  
  private static final Set<String> OHIO_CITIES = new HashSet<String>(Arrays.asList(
      "ASHTABULA",
      "ASHTABULA CO",
      "ASHTABULA COUNTY",
      "ANDOVER TWP",
      "PIERPONT TWP",
      "WILLIAMSFIELD TWP",
      "ANDOVER",
      "PIERPONT",
      "RICHMOND",
      "WILLIAMSFIELD",
      
      "TRUMBULL",
      "TRUMBULL COUNTY",
      "TRUMBULL CO",
      "KINSMAN TWP",
      "KINSMAN",
      "VERNON"
  )); 
}
