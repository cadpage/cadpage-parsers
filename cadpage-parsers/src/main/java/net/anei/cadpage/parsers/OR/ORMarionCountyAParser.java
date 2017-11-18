package net.anei.cadpage.parsers.OR;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Marion County, OR
 */
public class ORMarionCountyAParser extends FieldProgramParser {
  
  public ORMarionCountyAParser() {
    super("MARION COUNTY", "OR",
          "( SELECT/1 INFO:CALL! STARS LOC:ADDRCITY1! CFS:SKIP! DATETIME! Units:UNIT! ID! INFO/N+ " +
          "| CALL2 ( ADDRCITY2 ( UNIT! ( MAP | INFO MAP | ) | PLACE? MAP! ( CH | ALRM | ) EMPTY+? UNIT ) " +
                  "| CALL CH/Z ADDRCITY2 MAP UNIT " +
                  "| CALL CH ADDRCITY2 MAP UNIT" +
                  "| ADDRCITY2 ( UNIT! EMPTY? ( MAP | INFO MAP | ) | PLACE? MAP! ( CH | ALRM | ) UNIT ) ) INFO+ )");
    setupSaintNames("PAUL");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@ci.woodburn.or.us";
  }
  
  private static final Pattern MAP_PTN = Pattern.compile(":MAP::(\\d+[A-Z]?):");
  private static final Pattern DELIM = Pattern.compile("(?<!LAT|LON):");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    do {
      if (subject.equals("Incident") || subject.equals("!")) break;
      
      if (body.startsWith("Incident / ")) {
        body = body.substring(11).trim();
        break;
      }
      
      if (body.startsWith("! / ")) {
        body = body.substring(4).trim();
        break;
      }
      
      return false;
    } while (false);
    
    if (body.startsWith("INFO:")) {
      setSelectValue("1");
      return parseFields(body.split("\n"), data);
    }
    
    setSelectValue("2");
    
    body = body.replace('\n', ' ');
    
    // And a MAP::<code> construct
    body = MAP_PTN.matcher(body).replaceFirst(":MAP-$1:");
    
    String[] flds = DELIM.split(body, -1);
    if (flds.length == 1) {
      setFieldList("ADDR APT CALL");
      parseAddress(StartType.START_ADDR, body, data);
      data.strCall = getLeft();
      return isValidAddress();
    }
    return parseFields(flds, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("STARS")) return new SkipField("\\*{4}", true);
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("CH")) return new ChannelField("N\\d|", true);
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MapField("MAP-?(.*)", true);
    if (name.equals("ALRM")) return new MyAlarmField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCall2Field extends CallField {
    
    public MyCall2Field() {
      super();
    }
    
    public MyCall2Field(String pattern, boolean hard) {
      super(pattern, hard);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private static final Pattern VALID_ADDR1_PTN = Pattern.compile("(.*?),([ A-Z]*)-(.*)");
  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = VALID_ADDR1_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = match.group(2).trim();
        data.strPlace = match.group(3).trim();
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
    
  }
  
  private static final Pattern VALID_ADDR2_PTN = Pattern.compile("[-@/ A-Z0-9]+,([A-Z ]+)|.*\\bMP\\b.*|.* COMPLEX|LAT:.* LON:.*", Pattern.CASE_INSENSITIVE);
  private class MyAddressCity2Field extends AddressCityField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = VALID_ADDR2_PTN.matcher(field);
      if (!match.matches())  return false;
      String city = match.group(1);
      if (city != null && !CITY_SET.contains(city.trim().toUpperCase())) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      if (field.contains("I5") || field.contains(" MP ")) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?:[A-Z]+[0-9]+(?:-[A-Z]+)?|\\d{3}|MCSO|RCO|[A-Z]*TONE|Respond-[A-Z0-9]+)(?:,.*)?");
  private static final Pattern STATION_PTN = Pattern.compile("\\bSTA\\d+$");
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      setPattern(UNIT_PTN);
    }
    
    @Override
    public void parse(String fld, Data data) {
      fld = fld.replace("Respond-", "");
      Matcher match = STATION_PTN.matcher(fld);
      if (match.find()) {
        data.strSource = match.group();
        fld = fld.substring(0,match.start()).trim();
        fld = stripFieldEnd(fld, ",");
      }
      super.parse(fld, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT SRC";
    }
  }
  
  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("(.*?) +(\\d{3}[- ]?\\d{3}[- ]?\\d{4})");
  private class MyPlaceField extends PlaceField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("MAP")) return false;
      parse(field, data);
      return true;
    }
    
    @Override 
    public void parse(String field, Data data) {
      if (field.equals(data.strAddress)) return;
      Matcher match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE PHONE";
    }
  }
  
  private class MyAlarmField extends MyCall2Field {
    public MyAlarmField() {
      super("\\.(.* ALRM)", true);
    }
  }
  
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{10}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
        return;
      }
      if (data.strPlace.length() == 0) {
        Matcher match = PLACE_PHONE_PTN.matcher(field);
        if (match.matches()) {
          data.strPlace = match.group(1);
          data.strPhone = match.group(2);
          return;
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE PHONE";
    }
  }
  
  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(
    
    // Counties
    "CLACKAMAS COUNTY",
    "JEFFERSON COUNTY",
    "LINN COUNTY",
    "MARION COUNTY",
    "POLK COUNTY",
    "WASCO COUNTY",
    "YAMHILL COUNTY",

    // Cities
    "AUMSVILLE",
    "AURORA",
    "DETROIT",
    "DONALD",
    "GATES",
    "GERVAIS",
    "HUBBARD",
    "IDANHA",
    "JEFFERSON",
    "KEIZER",
    "MILL CITY",
    "MT ANGEL",
    "ST PAUL",
    "SALEM",
    "SCOTTS MILLS",
    "SILVERTON",
    "STAYTON",
    "SUBLIMITY",
    "TURNER",
    "WOODBURN",

    // Unincorporated communities and CDPs
    "BREITENBUSH",
    "BROOKS",
    "BUTTEVILLE",
    "CHAMPOEG",
    "CHEMAWA",
    "CLEAR LAKE",
    "FOUR CORNERS",
    "HAYESVILLE",
    "LABISH VILLAGE",
    "MACLEAY",
    "MARION",
    "MCKEE",
    "MEHAMA",
    "MIDDLE GROVE",
    "MONITOR",
    "NIAGARA",
    "NORTH HOWELL",
    "PRATUM",
    "ROSEDALE",
    "SAINT BENEDICT",
    "SAINT LOUIS",
    "SHAW",
    "TALBOT",
    "WACONDA",
    "WEST STAYTON",
    
    // Clackamas County
    "MOLALLA",
    
    // Linn County
    "LYONS"
  ));
}