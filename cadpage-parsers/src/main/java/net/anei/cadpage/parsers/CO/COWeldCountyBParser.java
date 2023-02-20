package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COWeldCountyBParser extends FieldProgramParser {

  public COWeldCountyBParser() {
    super(CITY_LIST, "WELD COUNTY", "CO",
          "( EMPTY ADDR ( APT APT CITY | CITY ) PLACE UNIT CALL? ID | ) INFO! GPS1/d GPS2/d MAP? EMPTY/Z END");
    setupCities(MAP_CITY_TABLE.stringPropertyNames());
  }

  @Override
  public String getFilter() {
    return "777";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("WELD COUNTY: ", "");
    body = stripFieldStart(body, "Automated message from Dispatch:");

    int pt = body.indexOf("\nText STOP");
    if (pt >= 0) body = body.substring(0,pt).trim();

    return parseFields(body.split("\\|", -1), 3, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ID")) return new IdField("\\d\\d[A-Z]{2,3}\\d{6}|\\d+-\\d+", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      return super.checkParse(cleanField(field), data);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(cleanField(field), data);
    }
    
    private String cleanField(String field) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0,pt).trim();
      return field;
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ,]*\\[\\d\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
  
  
  
  @Override
  public String adjustMapCity(String city) {
    String mapCity = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (mapCity != null) city = mapCity;
    return city;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // County
    "WELD",
    "LARIMER COUNTY",
    "MORGAN COUNTY",
    
    // Cities
    "BRIGHTON",
    "DACONO",
    "EVANS",
    "FORT LUPTON",
    "GREELEY",
    "LONGMONT",
    "NORTHGLENN",
    "THORNTON",
    
    // Towns
    "AULT",
    "BERTHOUD",
    "EATON",
    "ERIE",
    "EVANSTON",
    "FIRESTONE",
    "FREDERICK",
    "GARDEN CITY",
    "GILCREST",
    "GROVER",
    "HUDSON",
    "JOHNSTOWN",
    "KEENESBURG",
    "KERSEY",
    "LASALLE",
    "LA SALLE",
    "LOCHBUIE",
    "MEAD",
    "MILLIKEN",
    "NUNN",
    "PIERCE",
    "PLATTEVILLE",
    "RAYMER",
    "SEVERANCE",
    "WINDSOR",
    
    // Census-designated places
    "ARISTOCRAT RANCHETTES",
    "BRIGGSDALE",
    
    // Unincorporated communities
    "AUBURN",
    "AVALO",
    "CARR",
    "DEARFIELD",
    "GALETON",
    "GILL",
    "HEREFORD",
    "HIGHLANDLAKE",
    "IONE",
    "KEOTA",
    "LUCERNE",
    "ROGGEN",
    "STONEHAM",
    "WATTENBURG",
    
    // Ghost towns
    "ALDEN",
    "DEARFIELD",
    "ELWELL",
    "FORT ST VRAIN",
    "LATHAM",
    "MASTERS",
    "ROSEDALE",
    "SERENE",
    "SLIGO",
    
    // Latimer County
    "FORT COLLINS",
    "LOVELAND",
    
    // Elsewhere
    "AURORA"
  };
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ARISTOCRAT RANCH", "ARISTOCRAT RANCHETTES",
      "ARROWHEAD",        "GREELEY",
      "CAMFIELD",         "EATON",
      "HILL N PARK",      "GREELEY",
      "INDIAN HEAD",      "LOVELAND",
      "NEW RAYMER",       "RAYMER"
  });
}
