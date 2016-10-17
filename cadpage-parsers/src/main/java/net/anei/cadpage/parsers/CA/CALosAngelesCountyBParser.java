package net.anei.cadpage.parsers.CA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Los Angeles County, CA (B)
 */
public class CALosAngelesCountyBParser extends FieldProgramParser {
  
  public CALosAngelesCountyBParser() {
    super(CITY_CODES, "LOS ANGELES COUNTY", "CA",
          "DAREA:MAP! INC:ID! TYPE:CODE! DESC:CALL! PRI:PRI! ADDRESS:ADDRCITY! PLACE:PLACE! DETAIL:UNIT? TEXT:INFO LAT:SKIP LONG:SKIP PRI_UNIT:UNIT UNIT+");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("DETAIL:", "DETAIL:\n");
    if (! parseFields(body.split("\n"), data)) return false;
    return true;
  }
  
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new PlaceField("\\(([^(]+)\\)", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  

  private static final Pattern EXTRA_FIELD_PATTERN = Pattern.compile("From.*");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = EXTRA_FIELD_PATTERN.matcher(field);
      if (m.matches()) return;
      
      if (field.length() == 0) return;
      
      Set<String> unitSet = new HashSet<String>();
      if (data.strUnit.length() > 0) {
        unitSet.addAll(Arrays.asList(data.strUnit.split(",")));
      }
      for (String unit : field.split(",")) {
        unit = unit.trim();
        if (unit.length() > 0 && !unitSet.contains(unit)) {
          data.strUnit = append(data.strUnit, ",", unit);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" INFO";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CAR" , "CARSON",
      "HER" , "HERMOSA BEACH",
      "CEC" , "COMPTON COLLEGE",
      "LOS" , "LOS ANGELES",
      "RED" , "REDONDO BEACH",
      "CUL" , "CULVER CITY",
      "LON" , "LONG BEACH",
      "COM" , "COMPTON",
      "ELS" , "EL SEGUNDO",
      "ECC" , "EL CAMINO",
      "LOM" , "LOMITA",
      "GAR" , "GARDENA",
      "MAN" , "MANHATTAN BEACH",
      "ING" , "INGLEWOOD",
      "HAW" , "HAWTHORNE",
      "LAW" , "LAWNDALE",
      "TOR" , "TORRANCE"
  });
}
