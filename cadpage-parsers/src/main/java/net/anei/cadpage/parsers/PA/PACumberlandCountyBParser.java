package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACumberlandCountyBParser extends FieldProgramParser {

  public PACumberlandCountyBParser() {
    super(CITY_CODES, "CUMBERLAND COUNTY", "PA",
          "CALL! Alarm:PRI! Loc:ADDR/S! X:X! Box:BOX! Lat/Lon:GPS! Time:DATETIME! MI#:ID! Disp:UNIT! END");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "ep911@ccpa.net,dispatch@cgfrems.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith("IPS I/Page Notification")) return false;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("0")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern OOC_PFX_PTN = Pattern.compile("([A-Z]{2}): @[A-Z]{3}[,:;] *");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|LOT) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      String countyCode = null;
      Matcher match = OOC_PFX_PTN.matcher(field);
      if (match.lookingAt()) {
        countyCode = match.group(1);
        field = field.substring(match.end());
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
        field = getLeft();
      }

      Parser p = new Parser(field);
      data.strPlace = stripFieldStart(p.getLastOptional(':'), "@");
      String apt = p.getLastOptional(',');
      apt = append(p.getLastOptional(';'), "-", apt);
      match = APT_PTN.matcher(apt);
      if (match.matches()) apt = match.group(1);
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);

      if (data.strCity.length() > 0) {
        data.strCity = stripFieldEnd(data.strCity, " BORO");
      } else if (countyCode != null) {
        String city = COUNTY_CODES.getProperty(countyCode);
        if (city != null) data.strCity = city;
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", ",");
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADMS", "ADAMS COUNTY",
      "CUMB", "CUMBERLAND COUNTY",
      "DAUP", "DAUPHIN COUNTY",
      "FRAN", "FRANKLIN COUNTY",

      "CA CUMB", "CARLISLE",
      "CH CUMB", "CAMP HILL",
      "CK CUMB", "COOKE TWP",
      "DK CUMB", "DICKINSON TWP",
      "EP CUMB", "EAST PENNSBORO TWP",
      "HM CUMB", "HAMPDEN TWP",
      "HW CUMB", "HOPEWELL TWP",
      "LA CUMB", "LOWER ALLEN TWP",
      "LB CUMB", "LEMOYNE",
      "LF CUMB", "LOWER FRANKFORD TWP",
      "LM CUMB", "LOWER MIFFLIN TWP",
      "MB CUMB", "MECHANICSBURG",
      "MN CUMB", "MONROE TWP",
      "MH CUMB", "MT HOLLY SPRINGS",
      "MX CUMB", "MIDDLESEX TWP",
      "NB CUMB", "NEWBURG",
      "NC CUMB", "NEW CUMBERLAND",
      "NM CUMB", "NORTH MIDDLETON TWP",
      "NN CUMB", "NORTH NEWTON TWP",
      "NV CUMB", "NAVAL SUPPORT ACTIVITY MECHANCISBURG",
      "NW CUMB", "NEWVILLE",
      "PN CUMB", "PENN TWP",
      "SB CUMB", "SHIPPENSBURG",
      "SF FRAN", "SHIPPENSBURG FRANKLIN",
      "SH CUMB", "SOUTHAMPTON TWP",
      "SM CUMB", "SOUTH MIDDLETON TWP",
      "SN CUMB", "SOUTH NEWTON TWP",
      "SR CUMB", "SHIREMANSTOWN",
      "SS CUMB", "SILVER SPRING TWP",
      "ST CUMB", "SHIPPENSBURG TWP",
      "UA CUMB", "UPPER ALLEN TWP",
      "UF CUMB", "UPPER FRANKFORD TWP",
      "UM CUMB", "UPPER MIFFLIN TWP",
      "WB CUMB", "WORMLEYSBURG",
      "WP CUMB", "WEST PENNSBORO TWP",

      // York County codes
      "MON",             "MONAGHAN TWP"
  });

  private static final String[] CITY_LIST = new String[]{
      // Perry County
      "TOBOYNE TWP",
      "JACKSON TWP",
        "BLAIN",
        "BLAIN BORO",
      "SOUTHWEST MADISON TWP",
      "TYRONE TWP",
        "LANDISBURG",
      "SPRING TWP",
      "CARROLL TWP",
      "RYE",
      "MARYSVILLE",
      "MARYSVILLE BORO",

      // Dauphin County
      "MIDDLE PAXTON TWP",
        "DAUPHIN",
        "DAUPHIN BORO",
      "SUSQUEHANNA TWP",
        "HARRISBURG",
        "PENNBROOK",
        "PENNBROOK BORO",
      "SWATARA TWP",
        "STEELTON",
        "STEELTON BORO",

      // York County
      "FRANKLIN TWP",
        "FRANKLINTOWN",
        "FRANKLINTOWN BORO",
      "CARROLL TWP",
        "DILLSBURG",
        "DILLSBURG BORO",
      "MONAGHAN TWP",
      "FAIRVIEW TWP",

      // Adams County
      "FRANKLIN TWP",
      "MENALLEN TWP",
        "BENDERSVILLE",
        "BENDERSVILLE BORO",
        "STATION ASPERS",
      "TYRONE TWP",
      "HUNTINGTON TWP",
        "YORK SPRINGS",
        "YORK SPRINGS BORO",
      "LATIMORE TWP",

      // Franklin County
      "LURGAN TWP",
      "SOUTHAMPTON TWP",
        "SHIPPENSBURG",
        "SHIPPENSBURG BORO",
        "ORRSTOWN",
        "ORRSTOWN BORO"
  };

  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "AC", "ADAMS COUNTY",
      "DC", "DAUPHIN COUNTY",
      "FC", "FRANKLIN COUNTY",
      "PC", "PERRY COUNTY",
      "YC", "YORK COUNTY",
  });
}
