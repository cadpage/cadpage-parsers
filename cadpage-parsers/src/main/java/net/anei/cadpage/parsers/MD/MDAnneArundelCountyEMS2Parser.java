package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDAnneArundelCountyEMS2Parser extends FieldProgramParser {
  
  public MDAnneArundelCountyEMS2Parser() {
    super(CITY_LIST, "ANNE ARUNDEL COUNTY", "MD", 
          "( SELECT/1 Incident_Number:ID? Call_Category:SKIP! Priority:PRI1? Location:ADDR! Business/Bldg:PLACE! Apartment:APT! Cross_Streets:X! " +
            "Longitude:GPS1! Latitude:GPS2! Nature:CALL1! Date_&_Time:DATETIME1! Box_Area:BOX! " + 
            "( Tac_Ch:CH Apparatus:UNIT! Calltaker:SKIP! Dispatcher:SKIP! | Map_Page:MAP! ) Caution_Notes:ALERT! System_Notes:INFO! " +
          "| ID2 CALL2 ADDR/SXP GPS2 MAP2 X1 X2 UNIT2 Notes:INFO! ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@aacounty.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Assigned to Incident (\\d{5,})");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    setSelectValue(body.startsWith("Incident Number:") || body.startsWith("Call Category:") ? "1" : "2");
    body = body.replace(" Tac Ch:", "\nTac Ch:");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI1")) return new MyPriority1Field();
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("DATETIME1")) return new MyDateTime1Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID2")) return new IdField("INCIDENT (\\d{5,})", true);
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("GPS2")) return new GPSField("COORDINATES\\b *(.*)");
    if (name.equals("MAP2")) return new MyMapChannel2Field();
    if (name.equals("X1")) return new CrossField("FIRST CROSS STREET\\b *(.*)");
    if (name.equals("X2")) return new CrossField("SECOND CROSS STREET\\b *(.*)");
    if (name.equals("UNIT2")) return new MyUnit2Field();
    return super.getField(name);
  }
  
  private class MyPriority1Field extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" Alarm: ", "-");
      super.parse(field, data);
    }
  }
  
  private static final Pattern CODE_CALL_PTN1 = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) +(.*)", Pattern.CASE_INSENSITIVE);
  private class MyCall1Field extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN1.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern DATE_TIME_PTN1 = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}),? +(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTime1Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(",")) return;
      Matcher match = DATE_TIME_PTN1.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "&");
      super.parse(field, data);
    }
  }
  
  private static final Pattern CODE_CALL_PTN2 = Pattern.compile("CODE (\\S+) - (.*?)(?: ALARM (\\d+))?");
  private class MyCall2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN2.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
      data.strPriority = getOptGroup(match.group(3));
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL PRI";
    }
  }
  
  private static final Pattern MAP_CH_PTN = Pattern.compile("Grid +(\\S*) +Map +AreaArea +(\\S*) +CHANNEL\\b *(.*)");
  private class MyMapChannel2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_CH_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = append(match.group(1), "/", match.group(2));
      data.strChannel = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "MAP CH";
    }
  }
  
  private class MyUnit2Field extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("APPARATUSAPPARATUS ")) abort();
      field = field.substring(19).trim().replace("; ", " ");
      super.parse(field, data);
    }
  }
  
  
  private static final String[] CITY_LIST = new String[]{
    
    // City
    "ANNAPOLIS",

    // Town
    "HIGHLAND BEACH",

    // Census-designated places
    "ANNAPOLIS NECK",
    "ARDEN ON THE SEVERN",
    "ARNOLD",
    "BROOKLYN PARK",
    "CAPE SAINT CLAIRE",
    "CROFTON",
    "CROWNSVILLE",
    "DEALE",
    "EDGEWATER",
    "FERNDALE",
    "FORT MEADE",
    "FRIENDSHIP",
    "GALESVILLE",
    "GAMBRILLS",
    "GLEN BURNIE",
    "HERALD HARBOR",
    "JESSUP",
    "LAKE SHORE",
    "LINTHICUM",
    "MARYLAND CITY",
    "MAYO",
    "NAVAL ACADEMY",
    "ODENTON",
    "PAROLE",
    "PASADENA",
    "PUMPHREY",
    "RIVA",
    "RIVIERA BEACH",
    "SEVERN",
    "SEVERNA PARK",
    "SHADY SIDE"
  };
}
