package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORDeschutesCountyParser extends FieldProgramParser {
  
  public ORDeschutesCountyParser() {
    super("DESCHUTES COUNTY", "OR",
          "CALL CALL+? PRI UNITSRC ADDR! PLACE? MAP TIME");
    setupMultiWordStreets(
        "CATTLE DRIVE",
        "OLD BEND REDMOND"
    );
  }
  
  @Override
  public String getFilter() {
    return "911@deschutes.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replace('\n', ' ');
    // Normally broken by dash field separators.
    // but a dash with a space on both side is probably a "REAL" dash and
    // needs to be projected from our parsing breaks
    body = body.replace(" - ", " %% ");
    body = body.replace("NON-EMERGENCY", "NON%%EMERGENCY");
    body = body.replace("NON-INJURY", "NON%%INJURY");
    body = body.replace("Car-Bike", "Car%%Bike");
    body = body.replace("20-22", "20%%22");
    body = body.replace("FOIN-FOLLETTE", "FOIN%%FOLLETTE");
    body = body.replace("Headache-No", "Headache%%No");
    body = body.replace("9-1-1", "9%%1%%1");
    if (body.startsWith("--")) body = "-XXXX-" +  body.substring(3);
    if (!parseFields(body.split("-"), 4, data)) return false;
    if (data.strPriority.equals("XXXX")) data.strPriority = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PRI")) return new PriorityField("[A-Z][A-Z0-9]{0,3}|\\dE|ASSTF", true);
    if (name.equals("UNITSRC")) return new UnitSourceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  // Call field, replace double underscores with dash
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("%%", "-");
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private static final Pattern STATION_PAT = Pattern.compile("STA\\d+");
  private class UnitSourceField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      if (STATION_PAT.matcher(field).matches()) {
        data.strSource = field;
      } else  {
        data.strUnit = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "SRC UNIT";
    }
  }
  
  
  private static final Pattern MP_PTN1 = Pattern.compile("^(\\d+) +MP\\b");
  private static final Pattern MP_PTN2 = Pattern.compile("^(\\d+)\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("%%", "-");
      boolean repMP = false;
      Matcher match = MP_PTN1.matcher(field);
      if (match.find()) {
        repMP = true;
        field = match.group(1) + field.substring(match.end()); 
      }
      field = MP_PTN1.matcher(field).replaceFirst("$1");
      parseAddress(StartType.START_ADDR, field, data);
      data.strPlace = getLeft();
      if (repMP) {
        data.strAddress = MP_PTN2.matcher(data.strAddress).replaceFirst("$1 MP");
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
  
  private class MyMapField extends MapField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0 || field.equals("M")) return true;
      if (!field.startsWith("Map ")) return false;
      super.parse(field.substring(4).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
