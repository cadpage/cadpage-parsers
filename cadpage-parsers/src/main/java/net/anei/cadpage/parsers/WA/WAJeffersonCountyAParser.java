package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;



public class WAJeffersonCountyAParser extends FieldProgramParser {
  
  public WAJeffersonCountyAParser() {
    super(CITY_LIST, "JEFFERSON COUNTY", "WA",
          "CALL UNIT PLACE ADDR CITY X DATETIME! NARRATIVE INFO+");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,messaging@emergencysmc.com,dispatch@jcpsn.us";
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:((?:[A-Za-z]*(?:\\d+(?:Vol|VOL)?|OffDuty) )*) )?(Ludlow North|[A-Za-z ]+ \\d{3}) (.*?)(?: (\\d{4}-\\d{8})\\b|(?<=TRAN)(?:  |$)) *(.*)");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\bE911 Info .*|\\bCall Number \\d+ was ceated from Call Number \\d+|\\([^\\(]+ \\d\\d?:\\d\\d[AP]M\\)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    if (!subject.equals("Incident")) data.strSource = subject;
    String[] flds = body.split(" : ");
    if (flds.length >= 7) {
      return parseFields(body.split(" : "), 7, data);
    }
    
    // New format drops all of the colon delimiters :(
    setFieldList("UNIT MAP ADDR APT PLACE CITY CALL ID INFO");
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strUnit = getOptGroup(match.group(1));
    data.strMap = match.group(2).trim();
    String addr = getOptGroup(match.group(3));
    data.strCallId = getOptGroup(match.group(4));
    String info = match.group(5);
    
    String call = CALL_LIST.getCode(addr, true);
    int flags = FLAG_IMPLIED_INTERSECT | FLAG_PAD_FIELD;
    if (call != null) {
      data.strCall = call;
      addr = addr.substring(0,addr.length()-call.length()).trim();
      flags |= FLAG_ANCHOR_END;
    }

    parseAddress(StartType.START_ADDR, flags, addr, data);
    data.strPlace = getPadField();
    
    if (call == null) {
      data.strCall = getLeft();
      if (data.strCall.length() == 0) return false;
    }
    data.strAddress = cleanBK(data.strAddress);
    data.strPlace = cleanBK(data.strPlace);
    
    info = INFO_JUNK_PTN.matcher(info).replaceAll("").trim();
    data.strSupp = info;
    return true;
  }
  
  private String cleanBK(String fld) {
    int pt = fld.indexOf("BK=");
    if (pt >= 0) fld = fld.substring(0,pt).trim();
    return fld;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private class NarrativeField extends SkipField {
    @Override
    public void parse(String field, Data data) {
      if (!"Narrative".startsWith(field)) abort();
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NARRATIVE")) return new NarrativeField();
    return super.getField(name);
  }
  
  @Override
  protected void setupCallList(CodeSet callDictionary) {
    super.setupCallList(callDictionary);
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "ALS",
      "ALS - CPR",
      "BLS",
      "DOA",
      "Fire",
      "Fire - Alarm Commercial",
      "Fire - Alarm Residential",
      "Fire - Brush",
      "Fire - Commercial",
      "Fire - Dumpster",
      "Fire - Illegal Burn",
      "Fire - Other",
      "Fire - Residential",
      "HazCon",
      "Rescue - Technical",
      "Rescue - Water",
      "SUIC",
      "TC",
      "TC - Multi",
      "TRAN"

  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "PORT TOWNSEND",

    // Census-designated places
    "BRINNON",
    "MARROWSTONE",
    "PORT HADLOCK",
    "PORT HADLOCK-IRONDALE",
    "PORT LUDLOW",
    "QUEETS",
    "QUILCENE",

    // Unincorporated communities
    "ADELMA BEACH",
    "BECKETT POINT",
    "CAPE GEORGE",
    "CENTER",
    "CHIMACUM",
    "CLEARWATER",
    "COYLE",
    "CROCKER LAKE",
    "DABOB",
    "DISCOVERY BAY",
    "EAST QUILCENE",
    "GARDINER",
    "GLEN COVE",
    "IRONDALE",
    "KALA POINT",
    "KALALOCH",
    "LELAND",
    "MATS MATS",
    "OAK BAY"
  };
}
