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
          "DATETIME_CODE CALL! ADDR! CITY! Apt:APT! Cross_Streets:X? EMPTY? GOOGMAP_GPS!");
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
  
  private static Pattern SUBJ_ID = Pattern.compile("CAD Page for CFS (B?\\d+-\\d+)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher mat = SUBJ_ID.matcher(subject);
    if (!mat.matches()) return false;
    data.strCallId = mat.group(1);
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("\\s*|(?i)[A-Z](?: [-*A-Z0-9 /]+)?", true); //might be blank
    if (name.equals("DATETIME_CODE")) return new MyDateTimeCodeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GOOGMAP_GPS")) return new MyGMapGPSField();
    return super.getField(name);
  }
  
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
      "ANT",        "ANTHONY TWP",
      "BAS",        "BASTRESS TWP",
      "BRO",        "BROWN TWP",
      "CHO",        "COGAN HOUSE TWP",
      "CLI",        "CLINTON TWP",
      "CRA",        "CRAWFORD TWP",
      "CUM",        "CUMMINGS TWP",
      "DUB",        "DUBOISTOWN",
      "ELD",        "ELDRED TWP",
      "FAI",        "FAIRFIELD TWP",
      "FRA",        "FRANKLIN TWP",
      "GAM",        "GAMBLE TWP",
      "GRN",        "GREENE TWP",
      "HEP",        "HEPBURN TWP",
      "JOR",        "JORDAN TWP",
      "JSH",        "JERSEY SHORE",
      "LEW",        "LEWIS TWP",
      "LIM",        "LIMESTONE TWP",
      "LOY",        "LOYALSOCK TWP",
      "LYC",        "LYCOMING TWP",
      "MCH",        "MCHENRY TWP",
      "MCI",        "MCINTYRE TWP",
      "MOR",        "MORELAND TWP",
      "MTG",        "MONTGOMERY",
      "MTV",        "MONTOURSVILLE",
      "MUN",        "MUNCY",
      "MUT",        "MUNCY TWP",
      "NIP",        "NIPENOSE TWP",
      "OLY",        "OLD LYCOMING TWP",
      "PCT",        "PINE CREEK TWP",
      "PEN",        "PENN TWP",
      "PIA",        "PIATT TWP",
      "SUS",        "SUSQUEHANNA TWP",
      "SWI",        "SOUTH WILLIAMSPORT",
      "UFA",        "UPPER FAIRFIELD TWP",
      "WAS",        "WASHINGTON TWP",
      "WAT",        "WATSON TWP",
      "WAY",        "WAYNE TWP",
      "WIL",        "WILLIAMSPORT"
  });
  
}
