package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALycomingCountyParser extends FieldProgramParser {

  public PALycomingCountyParser() {
    super(CITY_CODES, "LYCOMING COUNTY", "PA", 
          "( DISPATCH_REPORT! Ver:SKIP! CFS_Number:ID! DATETIME1! RECOMMENDED! UNIT1/ZS+ Note:INFO/N+ CALL:CALL! INC_DESC:CALL_DESC! LOCATION:ADDRCITY! GPS! BUSINESS:PLACE! RECEIVED:SKIP! Cross_Streets:X? Boundaries:INFO/N+ " +
          "| DATETIME_CODE! CALL! ADDR! CITY! Apt:APT! Cross_Streets:X? EMPTY? GOOGMAP_GPS! )");
  }
  
  @Override
  public String getFilter() {
    return "RipRun@lyco.org,CAD_Notification@lyco.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public String getProgram() {
    String prog = super.getProgram();
    //if we are in format B then we are parsing ID from the subject
    if (!prog.contains(" ID ")) prog = "ID " + prog;
    return prog;
  }
  
  private static Pattern TAG_ADDR = Pattern.compile("([A-Z]+:)\\s*(.*)");
  public String adjustMapAddress(String saddress) {
    //remove BLOCK: or U: or some other tag from MADDR
    Matcher mat = TAG_ADDR.matcher(saddress);
    if (mat.matches()) return mat.group(2);
    return saddress;
  }
  
  private static Pattern SUBJ_ID = Pattern.compile("CAD Page for CFS (\\d+-\\d+)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //format A
    if (subject.equals("DISPATCH REPORT")) {
      //insert a tag before CALL so SAP can parse it easily
      int ci = body.indexOf("RECOMMENDED:");
      if (ci == -1) return false;
      ci = body.indexOf("\n \n", ci) + 3;
      if (ci == 2) return false; //-1+3=2
      body = body.substring(0, ci) + "CALL:" + body.substring(ci);
      
      return parseFields(body.split("\n\\s*"), data);
    }
    
    //format B
    Matcher mat = SUBJ_ID.matcher(subject);
    if (!mat.matches()) return false;
    data.strCallId = mat.group(1);
    return parseFields(body.split("\n"), data);
  }
  
  private static final DateFormat DATE_FMT1 = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
  @Override
  public Field getField(String name) {
    //both formats
    if (name.equals("CALL")) return new CallField("\\s*|(?i)[A-Z](?: [A-Z0-9 /]+)?", true); //might be blank
    //format A
    if (name.equals("DISPATCH_REPORT")) return new SkipField("DISPATCH REPORT", true);
    if (name.equals("DATETIME1")) return new DateTimeField(DATE_FMT1);
    if (name.equals("RECOMMENDED")) return new SkipField("RECOMMENDED:", true);
    if (name.equals("UNIT1")) return new UnitField("([^\\s]+).+"); //first word only
    if (name.equals("CALL_DESC")) return new CallField();
    //format B
    if (name.equals("DATETIME_CODE")) return new MyDateTimeCodeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GOOGMAP_GPS")) return new MyGMapGPSField();
    return super.getField(name);
  }
  
  //////////   Format B fields   ///////////////////////////////////////////////////////////////////
  private static Pattern DT_CODE = Pattern.compile("(.+) (.+)");
  private static final DateFormat DATE_FMT2 = new SimpleDateFormat("EEE MMM dd yyyy HH:mm");
  private class MyDateTimeCodeField extends DateTimeField {
    public MyDateTimeCodeField() {
      super(DATE_FMT2);
    }
    
    @Override
    public void parse(String field, Data data) {
      //separate and parse DATETIME and CODE
      Matcher mat = DT_CODE.matcher(field);
      if (!mat.matches()) abort();
      super.parse(mat.group(1), data);
      data.strCode = mat.group(2);
    }

    @Override
    public String getFieldNames() { return "DATE TIME CODE"; }
  }
  
  //just replace * with /
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace(" * ", " / "), data);
    }
  }
  
  //parse gps coords from google maps query
  private static Pattern QUERY_GPS = Pattern.compile("\\?q=([+\\-]\\d+(?:\\.\\d+)?)%20([+\\-]\\d+(?:\\.\\d+))");
  private class MyGMapGPSField extends Field {
    public MyGMapGPSField() {
      super("http://.*", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      //separate and parse DATETIME and CODE
      Matcher mat = QUERY_GPS.matcher(field);
      if (!mat.find()) return;
      setGPSLoc(mat.group(1) + "," + mat.group(2), data);
    }

    @Override
    public String getFieldNames() { return "GPS"; }
  }
  
  //////////   Other stuff   ///////////////////////////////////////////////////////////////////
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANT",        "ANTHONY",
      "BAS",        "BASTRESS",
      "CHO",        "COGAN HOUSE",  //??
      "CRA",        "CRAWFORD",
      "CUM",        "CUMMINGS",
      "DUB",        "DUBOISTOWN",
      "ELD",        "ELDRED",
      "FAI",        "FAIRFIELD",
      "GAM",        "GAMBLE",
      "HEP",        "HEPBURN",
      "JSH",        "JERSEY SHORE",
      "LEW",        "LEWIS",
      "LIM",        "LIMESTONE",
      "LOY",        "LOYALSOCK",
      "LYC",        "LYCOMING",
      "MCH",        "MCHENRY",
      "MCI",        "MCINTYRE",
      "MTV",        "MONTOURSVILLE",
      "MUT",        "MUNCY",
      "NIP",        "NIPENOSE",
      "OLY",        "OLD LYCOMING",
      "PIA",        "PIATT",
      "SUS",        "SUSQUEHANNA",
      "SWI",        "SOUTH WILLIAMSPORT",
      "UFA",        "UPPER FAIRFIELD",
      "WAS",        "WASHINGTON",
      "WAT",        "WATSON",
      "WAY",        "WAYNE",
      "WIL",        "WILLIAMSPORT"
  });
  
}
