package net.anei.cadpage.parsers.MD;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Baltimore County, MD
 */
public class MDBaltimoreCountyBParser extends FieldProgramParser {
  
  private static Field infoField = null;
  
  private class RunReportEntry {
    String key;
    Field field;
    String label;
    
    public RunReportEntry(String key) {
      this(key, null, null);
    }
    
    public RunReportEntry(String key, String field) {
      this(key, field, null);
    }
    
    public RunReportEntry(String key, String field, String label) {
      this.key = key;
      if ("INFO".equals(field)) {
        if (infoField == null) {
          infoField = getField(field);
          infoField.setQual("N");
        }
        this.field = infoField;
      } else {
        this.field = field == null ? null : getField(field);
      }
      this.label = label;
    }
    
    public void parse(String value, Data data) {
      if (field == null) return;
      if (value.isEmpty()) return;
      if (label != null) value = label + ": " + value;
      field.parse(value, data);
    }
  }
  
  private static RunReportEntry[] runRptTable;
  
  public MDBaltimoreCountyBParser() {
    super("BALTIMORE COUNTY", "MD",
           "BOX:MAP CALL:CALL! ADDR:ADDR/S6! PL:PLACE UNIT:UNIT! INFO:INFO DATE:DATE TIME:TIME ID:ID%");
    runRptTable = new RunReportEntry[] {
      new RunReportEntry("b ", "MAP"),
      new RunReportEntry(" cc# ", "ID"),
      new RunReportEntry(" unit ", "UNIT"),
      new RunReportEntry("\n"),
      new RunReportEntry("\nl ", "ADDR"),
      new RunReportEntry("\n"),
      new RunReportEntry("\n\nd ", "INFO", "Dispatched"),
      new RunReportEntry(" e ", "INFO", "Enroute"),
      new RunReportEntry("\n\na ", "INFO", "Arrived"),
      new RunReportEntry(" t ", "INFO", "Transport"),
      new RunReportEntry("\n\nh ", "INFO", "Hospital"),
      new RunReportEntry(" c ", "INFO", "Cleared"),
      new RunReportEntry("\n\ntitle ", "CALL")
    };
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('', '\'');
    
    if (body.startsWith("b ")) {
      
      setFieldList("MAP ID UNIT ADDR APT CITY INFO CALL");
      data.msgType = MsgType.RUN_REPORT;
      if (!parseRunReport(body, data)) return false;
    }

    else {
      if (!parseFields(body.split("\n"), data)) return false;
      
      // Address and place names get crossed when we deal with interchanges or mile markers
      if (data.strPlace.contains(" MM ") || data.strPlace.contains(" BET ")) {
        data.strCross = data.strAddress.replace('*', '/');
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
    }
    
    String mapCode = data.strMap;
    int pt = mapCode.indexOf('-');
    if (pt >= 0) mapCode = mapCode.substring(0, pt);
    mapCode = stripFieldStart(mapCode, "0");
    String city = MAP_CITY_TABLE.getProperty(mapCode);
    if (city != null) data.strCity = city;
    return true;
  }
  
  private boolean parseRunReport(String body, Data data) {
    try {
      RunReportEntry lastEntry = null;
      Parser p = new Parser(body);
      for (RunReportEntry entry : runRptTable) {
        String val = p.getRequired(entry.key);
        if (val == null) return false;
        
        if (lastEntry != null) {
          lastEntry.parse(val, data);
        }
        lastEntry = entry;
      }
      lastEntry.parse(p.get(), data);
      return true;
    } catch (FieldProgramException ex) {
      return false;
    }
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "PLACE X");
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("[A-Z0-9]{2,3}-\\d{2,3}|MUTAID|", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    return super.getField(name);
  }
  
  private static final Pattern UPDATED_LOC = Pattern.compile("[ \\*/]*\\b((?:UPDATED|UPDATED|CORRECT(?:ED)?)(?: LOC(?:ATION)?)?)\\b[ \\*/]*");
  private static final Pattern DIR_BOUND = Pattern.compile("\\b([NSEW])/B\\b");
  private static final Pattern APT_MARKER = Pattern.compile(" +(APT|ROOM|RM|BLDG|SUITE|CONDO) +");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      field = UPDATED_LOC.matcher(field).replaceAll(" ($1) ").trim();
      field = DIR_BOUND.matcher(field).replaceAll("$1B");
      field = field.replace('@', '&');
      field = field.replace(" AT ", " & ");
      
      String apt = "";
      Matcher match = APT_MARKER.matcher(field);
      if (match.find()) {
        apt = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
        String type = match.group(1);
        if (type.equals("BLDG")) apt = append(type, " ", apt);
      }
      
      super.parse(field, data);
      data.strApt = append(data.strApt, " ", apt);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      // Break field up into blank delimited tokens
      List<String> unitList = new ArrayList<String>();
      for (String part : field.split(" +")) {
        
        // Skip single digits
        if (part.length() == 1 && Character.isDigit(part.charAt(0))) continue;
        
        // Skip duplicate box number
        if (part.equals(data.strBox)) continue;
        
        // If it starts with FS add to source field
        if (part.startsWith("FS")) {
          data.strSource = append(data.strSource, " ", part);
          continue;
        }
        
        // Add to unit list unless it is already in there somewhere
        if (!unitList.contains(part)) unitList.add(part);
      }
      
      // Finally unpack list into unit field
      StringBuilder sb = new StringBuilder();
      for (String part : unitList) {
        if (sb.length() > 0) sb.append(' ');
        sb.append(part);
      }
      data.strUnit = sb.toString();
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT SRC";
    }
  }
  
  private class MyDateField extends DateField {
    
    public MyDateField() {
      super("\\d\\d-\\d\\d-\\d\\d|", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('-', '/');
      super.parse(field, data);
    }
  }
  
  @Override
  public boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(")) return true;
    return super.isNotExtraApt(apt);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = GN_PTN.matcher(addr).replaceAll("GREEN");
    addr = GR_PTN.matcher(addr).replaceAll("GARTH");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern GN_PTN = Pattern.compile("\\bGN\\b");
  private static final Pattern GR_PTN = Pattern.compile("\\bGR\\b");
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "01", "TOWSON",
      "02", "PIKESVILLE",
      "03", "WOODLAWN",
      "04", "CATONSVILLE",
      "05", "HALETHORPE",
      "06", "DUNDALK",
      "07", "ESSEX",
      "08", "BALTIMORE",
      "09", "BALTIMORE",
      "10", "PARKVILLE",
      "11", "BALTIMORE",
      "12", "MIDDLE RIVER",
      "13", "BALTIMORE",
      "14", "BALTIMORE",
      "15", "DUNDALK",
      "16", "BALTIMORE",
      "17", "TIMONIUM",
      "18", "RANDALLSTOWN",
      "19", "OWINGS MILLS",
      "20", "WHITE MARSH",
      "21", "MIDDLE RIVER",
      "26", "SPARROWS POINT",
      "27", "DUNDALK",
      "28", "BALTIMORE",
      "29", "TOWSON",
      "30", "LUTHERVILLE",
      "31", "OWINGS MILLS",
      "32", "PIKESVILLE",
      "33", "WOODLAWN",
      "35", "ARBUTUS",
      "36", "LANSDOWNE",
      "37", "BALTIMORE",
      "38", "GLEN ARM",
      "39", "COCKEYSVILLE",
      "40", "GLYNDON",
      "41", "REISTERSTOWN",
      "44", "MONKTON",
      "45", "FREELAND",
      "46", "RANDALLSTOWN",
      "47", "PHOENIX",
      "48", "KINGSVILLE",
      "49", "SPARKS",
      "50", "OWINGS MILLS",
      "51", "ESSEX",
      "54", "MIDDLE RIVER",
      "55", "PERRY HALL",
      "56", "REISTERSTOWN",
      "57", "SPARROWS POINT",
      "60", "PARKTON",
      "74", "MIDDLE RIVER",
      "85", "UPPERCO"
  });
}
