package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class PACumberlandCountyBParser extends FieldProgramParser {

  public PACumberlandCountyBParser() {
    super(CITY_CODES, "CUMBERLAND COUNTY", "PA",
          "CALL! Alarm:PRI! Loc:ADDR/S X:X! Box:BOX! Lat/Lon:GPS! Time:DATETIME! MI#:ID! Disp:UNIT! END");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "ep911@ccpa.net,dispatch@cgfrems.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean revMsgOrder() { return true;   }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 300; }
      @Override public int splitBreakPad() { return 1; }
    };
  }



  private static final Pattern RUN_REPORT_PTN1 = Pattern.compile("([EF]\\d{8});(\\S+) AVAILABLE;(.*)");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("(\\S+) AVAILABLE;([EF]\\d{8});(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith("IPS I/Page Notification")) return false;

    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = RUN_REPORT_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strSupp = match.group(3).replace(';', '\n').trim();
      return true;
    }

    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      data.strSupp = match.group(3).replace(';', '\n').trim();
      return true;
    }

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

  private static final Pattern OOC_PFX_PTN = Pattern.compile("([A-Z]{2}): @[A-Z]{3}[,:; ] *");
  private static final Pattern ADDR_DELIM = Pattern.compile("[:@,;]");
  private static final Pattern APT_PTN = Pattern.compile("(?:A\\b|APT|RM|LOT) *(.*)|([A-Z]?\\d{1,5}|[A-Z]|.* FL)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      field = field.replace("_TOWNSHIP", " TWP").replace("_BOROUGH", "");

      String countyCode = null;
      Matcher match = OOC_PFX_PTN.matcher(field);
      if (match.lookingAt()) {
        countyCode = match.group(1);
        field = field.substring(match.end());
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
        field = getLeft();
      }

      for (String part : ADDR_DELIM.split(field)) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          super.parse(part, data);
        } else if ((match = APT_PTN.matcher(part)).matches()) {
          String apt = match.group(1);
          if (apt == null) apt = match.group(2);
          data.strApt = append(data.strApt, "-", apt);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }

      if (data.strCity.length() > 0) {
        data.strCity = stripFieldEnd(data.strCity, " BORO");
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, data.strAddress, data);
      } else if (countyCode != null) {
        String city = COUNTY_CODES.getProperty(countyCode);
        if (city != null) data.strCity = city;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
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
      "YORK", "YORK COUNTY",

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

      "FC FR",   "SOUTHAMPTON TWP",

      "CARROLL TWP PERY", "CARROL TWP",

      // York County codes
      "MON",               "MONAGHAN TWP"
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
      "RYE TWP",
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
      "FANNETT TWP",
      "HAMILTON TWP",
      "LETTERKENNY TWP",
      "LURGAN TWP",
      "METAL TWP",
      "GREENE TWP",
      "GUILFORD TWP",
        "CHAMBERSBURG",
        "FAYETTEVILLE",
      "QUINCY TWP",
        "MONT ALTO",
      "SOUTHAMPTON TWP",
        "SHIPPENSBURG",
        "SHIPPENSBURG BORO",
        "ORRSTOWN",
        "ORRSTOWN BORO",
      "WASHINGTON TWP",
        "ROUZERVILLE",
        "WAYNESBORO"
  };

  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "AC", "ADAMS COUNTY",
      "DC", "DAUPHIN COUNTY",
      "FC", "FRANKLIN COUNTY",
      "PC", "PERRY COUNTY",
      "YC", "YORK COUNTY",
  });
}
