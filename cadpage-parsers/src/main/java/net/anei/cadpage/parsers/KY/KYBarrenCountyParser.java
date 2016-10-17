package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA54Parser;


public class KYBarrenCountyParser extends DispatchA54Parser {
  
  public KYBarrenCountyParser() {
    super("BARREN COUNTY", "KY", DataType.NAME, "270");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  public String getFilter() {
    return "911-CENTER@Barren911.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Occasionally dispatch swaps the address and cross streets :(
    // the street number ends up in the apt field.  So we try to
    // fix everything
    if (data.strPhone.length() == 0 && data.strCross.length() > 0 &&  
        data.strApt.length() > 0 && NUMERIC.matcher(data.strApt).matches()) {
      if (checkAddress(data.strAddress) == STATUS_STREET_NAME) {
        String cross = data.strAddress;
        data.strAddress = "";
        String addr = append(data.strApt, " ", data.strCross);
        data.strApt = "";
        parseAddress(addr, data);
        data.strCross = cross;
      }
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = CPW_PTN.matcher(addr).replaceAll("CUMBERLAND PKWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern CPW_PTN = Pattern.compile("\\bCPW\\b", Pattern.CASE_INSENSITIVE);
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "A B BROWN",
    "A L SHIRLEY",
    "APPLE GROVE",
    "ARNETT GROVE",
    "BESSIE BASTIN",
    "BIG MEADOW",
    "BLACK DOT",
    "CARL FOX",
    "CHRIS WILSON",
    "CORAL HILL",
    "CORAL HILL HALFWAY",
    "COUNTRY CLUB",
    "CRENSHAW CASSADY",
    "DRIPPING SPRINGS",
    "EDWIN P TERRY",
    "G L COMER",
    "GOODNIGHT HISEVILLE",
    "GOODNIGHT TERRACE",
    "GREEN PLOT",
    "GRINSTEAD MILL",
    "HAPPY VALLEY",
    "HARRY KING",
    "HISEVILLE BEAR WALLOW",
    "HISEVILLE BEARWALLOW",
    "HISEVILLE CORAL HILL",
    "HISEVILLE MAIN",
    "HISEVILLE PARK",
    "HORTON RIGDON",
    "HUCKLEBERRY KNOB",
    "INDIAN MILL",
    "J BROWN",
    "JACK SMITH",
    "JACK TURNER",
    "KNOB LICK WISDOM",
    "L ROGERS WELLS",
    "L WELLS",
    "LEE FENDELL",
    "LES TURNER",
    "MAMMOTH CAVE",
    "MAYFIELD MILL",
    "OAK GROVE CHURCH",
    "OAK HILL SCHOOL",
    "OAK RIDGE SCHOOL",
    "OWL SPRING",
    "PARK CITY BON AYR",
    "PARK CITY GLASGOW",
    "PARK MAMMOTH",
    "PRAK CITY GLASGOW",
    "PREWITTS KNOB",
    "R CRUMP",
    "ROBERT BISHOP",
    "SLICK ROCK",
    "SQUIRE EDWARDS",
    "TICK RIDGE",
    "TOOHEY RIDGE",
    "VETERANS OUTER",
    "WHITNEY WOODS",
    "WILSON CHILDRESS"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "1038 POSSIBLE SUICIDE",
      "1046 MVA WITH INJURIES",
      "1060 FIGHT",
      "ALARM FIRE ALARM ACTIVATION",
      "ASSIST ASSIST AMBULANCE SERVICE",
      "ASSTPD ASST POLICE DEPT/OFFICER",
      "BLEED BLEEDING",
      "BREATH BREATHING DIFFICULTIES",
      "BURN BURN INJURY",
      "BUSBLD FIRE - BUSINESS BUILDING",
      "CARMOX CARBON MONOXIDE DETECTOR",
      "CVA STROKE",
      "EXPLOD EXPLOSION",
      "FACTRY FIRE - FACTORY",
      "FAINT FAINTING / PASSED OUT",
      "FALL FALL",
      "FIELD FIRE - FIELD/BRUSH",
      "FIRE UNDEFINED TYPE OF FIRE",
      "FUEL FUEL SPILL",
      "GARAGE FIRE - GARAGE",
      "GLEAK GAS LEAK",
      "HEART CARDIAC/CHEST PAINS",
      "HOUSE FIRE - HOUSE",
      "ILL ILLNESS",
      "INVEST INVESTIGATION",
      "LA LIFELINE ALARM",
      "MA MUTUAL AID FOR ANOTHER FD",
      "NGO NATURAL GAS ODOR",
      "OD OVERDOSE",
      "SEIZE SEIZURE",
      "SERVIC SERVICE CALL",
      "SMOKE FIRE - SMOKE DETECTOR",
      "SUGAR DIABETIC EMERGENCY",
      "TCON TRAFFIC CONTROL",
      "TREE TREE DOWN",
      "UNRESP UNRESPONSIVE PERSON",
      "VEHICL FIRE - VEHICLE"
  );
}
