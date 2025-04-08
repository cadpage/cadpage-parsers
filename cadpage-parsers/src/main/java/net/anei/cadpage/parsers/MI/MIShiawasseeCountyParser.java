package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIShiawasseeCountyParser extends DispatchOSSIParser {

  public MIShiawasseeCountyParser() {
    super("SHIAWASSEE COUNTY", "MI",
          "CALL ADDR! SRC? INFO/N+? CITY_UNIT GPS1 ( GPS2 | SKIP ) UNIT END");
    setupCities(CITY_LIST);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,5}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CITY_UNIT")) return new MyCityUnitField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);

    return super.getField(name);
  }

  private static final Pattern UNIT_PTN = Pattern.compile("(?:\\b[A-Z]{1,4}\\d*\\b,?)+");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (isCity(field)) {
        data.strCity = field;
      } else if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = append(data.strUnit, ",", field);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT CITY";
    }
  }

  private class MyCityUnitField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (isCity(field)) {
        data.strCity = field;
      } else {
        data.strUnit = field;
      }

    }

    @Override
    public String getFieldNames() {
      return "CITY UNIT";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CORUNNA",
      "DURAND",
      "LAINGSBURG",
      "OVID",
      "OWOSSO",
      "PERRY",

      // Villages
      "BANCROFT",
      "BYRON",
      "LENNON",
      "MORRICE",
      "NEW LOTHROP",
      "VERNON",

      // Charter TWPs
      "CALEDONIA TWP",
      "OWOSSO TWP",

      // Civil TWPs
      "ANTRIM TWP",
      "BENNINGTON TWP",
      "BURNS TWP",
      "FAIRFIELD TWP",
      "HAZELTON TWP",
      "MIDDLEBURY TWP",
      "NEW HAVEN TWP",
      "PERRY TWP",
      "RUSH TWP",
      "SCIOTA TWP",
      "SHIAWASSEE TWP",
      "VENICE TWP",
      "VERNON TWP",
      "WOODHULL TWP",

      // Census-designated places
      "HENDERSON",
      "MIDDLETOWN",

      // Other unincorporated communities
      "ANTRIM CENTER",
      "BENNINGTON",
      "BURTON",
      "CARLAND",
      "EASTON",
      "FIVE POINTS",
      "FOREST GREEN ESTATES",
      "HOOVERS CORNERS",
      "JUDDVILLE",
      "KERBY",
      "NEWBURG",
      "NEW HAVEN",
      "NICHOLSON",
      "OLNEY CORNERS",
      "PITTSBURG",
      "SHAFTSBURG",
      "SHIAWASSEETOWN",
      "SMITH CROSSING",
      "UNION PLAINS"
  };
}
