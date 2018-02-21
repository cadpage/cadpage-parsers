package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDAnneArundelCountyEMS2Parser extends FieldProgramParser {
  
  public MDAnneArundelCountyEMS2Parser() {
    super(CITY_LIST, "ANNE ARUNDEL COUNTY", "MD", 
          "ID CALL ADDR/SXP GPS MAP X1 X2 UNIT Notes:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@aacounty.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Assigned to Incident \\d{5,}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INCIDENT (\\d{5,})", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("GPS")) return new GPSField("COORDINATES\\b *(.*)");
    if (name.equals("MAP")) return new MyMapChannelField();
    if (name.equals("X1")) return new CrossField("FIRST CROSS STREET\\b *(.*)");
    if (name.equals("X2")) return new CrossField("SECOND CROSS STREET\\b *(.*)");
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("CODE (\\S+) - (.*?)(?: ALARM (\\d+))?");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
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
  private class MyMapChannelField extends Field {
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
  
  private class MyUnitField extends UnitField {
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
