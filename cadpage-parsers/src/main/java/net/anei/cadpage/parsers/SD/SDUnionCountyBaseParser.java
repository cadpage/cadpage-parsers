package net.anei.cadpage.parsers.SD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


abstract public class SDUnionCountyBaseParser extends FieldProgramParser {
  
  public SDUnionCountyBaseParser(String program) {
    super(CITY_LIST, "UNION COUNTY", "SD", program);
  }
  
  @Override
  public String getFilter() {
    return "@sioux-city.org";
  }
  
  @Override
  public boolean parseFields(String[] fields, Data data) {
    if (!super.parseFields(fields, data)) return false;
    if (data.strCity.equals("SIOUX CITY")) data.strState = "IA";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, "/", field);
    }
  }
  
  private static final Pattern SRC_CODE_PTN = Pattern.compile("([A-Z]+)\\d+");
  private class MySourceField extends SourceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = SRC_CODE_PTN.matcher(field);
      if (!match.matches()) return false;
      String city = STATION_CODES.getProperty(match.group(1));
      if (city == null) return false;
      data.strSource = field;
      if (data.strCity.length() == 0) data.strCity = city;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ALCESTER",
    "ALSEN",
    "BERESFORD",
    "DAKOTA DUNES",
    "ELK POINT",
    "GARRYOWEN",
    "JEFFERSON",
    "NORA",
    "NORTH SIOUX",
    "RICHLAND",
    "SPINK",
    
    "SIOUX CITY"
  };
  
  private static final Properties STATION_CODES = buildCodeTable(new String[]{
      "DDUNES",   "DAKOTA DUNES",
      "NSIOUX",   "NORTH SIOUX",
      "SCFD",     "SIOUX CITY"
  });
}
