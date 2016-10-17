package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class TNWashingtonCountyAParser extends FieldProgramParser {
  
  private static final String EXTRA = "Think green: ";
  private static final Pattern TRAILER = Pattern.compile("\\b(\\d\\d:\\d\\d:\\d\\d)(?: (\\d+))?$");
  private static final Pattern TRAILER2 = Pattern.compile(" +\\d\\d:[\\d:]*$");
  
  private boolean ok = false;
  
  public TNWashingtonCountyAParser() {
    super("WASHINGTON COUNTY", "TN",
           "CPU UNIT2? ADDR PLACE+? ( X X2+? | ) MAP");
  }
  
  @Override
  public String getFilter() {
    return "@johnsoncitytn.org,CAD@wc911.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Dump trailing comment
    int pt = body.lastIndexOf('\n');
    if (pt < 0) return false;
    String tail = body.substring(pt+1).trim();
    if (tail.startsWith(EXTRA) || EXTRA.startsWith(tail)) {
      body = body.substring(0,pt).trim();
    }
    
    ok = false;
    Matcher match = TRAILER.matcher(body);
    if (match.find()) {
      ok = true;
      data.strTime = match.group(1);
      data.strCallId = getOptGroup(match.group(2));
      body = body.substring(0,match.start());
    }
    String flds[] = body.split("\n");
    if (!ok && flds.length < 4) return false;
    if (!ok) {
      String sMap = flds[flds.length-1];
      if (sMap.startsWith("Map")) {
        ok = true;
        match = TRAILER2.matcher(sMap);
        if (match.find()) {
          flds[flds.length-1] = sMap.substring(0,match.start());
        }
      }
    }
    if (!parseFields(flds, data)) return false;
    return ok;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " TIME ID";
  }
  
  // CPU - Combined Call / Priority / Unit field
  private static final Pattern UNIT_PTN = Pattern.compile("\\b[A-Z0-9]{2,4}(?:,[A-Z0-9]{2,4})*$");
  private static final Pattern PRI_PTN = Pattern.compile("-(?:ALPHA|BRAVO|CHARLIE|DELTA)");
  private class CallPriUnitField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_PTN.matcher(field);
      if (match.find()) {
        ok = true;
        data.strCall = field.substring(0,match.start()).trim();
        data.strPriority = (match.group().substring(1,2));
        data.strUnit = field.substring(match.end()).trim();
      } else {
        match = UNIT_PTN.matcher(field);
        if (match.find()) {
          data.strUnit = match.group();
          field = field.substring(0,match.start()).trim();
        }
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL PRI UNIT";
    }
  }
  
  // UNIT2 Lists units if there wern'e included in the CPU field
  private class Unit2Field extends UnitField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strUnit.length() > 0) return false;
      Matcher match = UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = field;
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("X-STR=") || isMapField(field)) return false;
      data.strPlace = append(data.strPlace, " - ", field);
      return true;
    }
  }
  
  private class MyCrossField extends CrossField {
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (! field.startsWith("X-STR=")) return false;
      ok = true;
      field = field.substring(6).trim();
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyCross2Field extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (isMapField(field)) return false;
      parse(field, data);
      return true;
    }
  }
  
  // MAP field has to start with map
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Map ")) {
        field = field.substring(4).trim();
        super.parse(field, data);
      } else {
        if (!"Map ".startsWith(field)) abort();
      }
    }
  }
  
  private boolean isMapField(String field) {
    if (field.startsWith("Map ")) return true;
    if ("Map".startsWith(field)) return true;
    return false;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CPU")) return new CallPriUnitField();
    if (name.equals("UNIT2")) return new Unit2Field();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
}
