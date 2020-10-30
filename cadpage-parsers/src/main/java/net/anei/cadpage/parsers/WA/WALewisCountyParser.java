package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class WALewisCountyParser extends FieldProgramParser {
  
  private static final Pattern TRAIL_ID_PTN = Pattern.compile("; \\[(D\\d{8})\\] - AlarmMessenger$");
  private static final Pattern DELIM = Pattern.compile(" *[;\n] *");
  private static final Pattern MASTER = Pattern.compile("(.*) - [A-Z][a-z]+, +[A-Z][a-z]+", Pattern.DOTALL);
  
  public WALewisCountyParser() {
    super(CITY_CODES, "LEWIS COUNTY", "WA",
          "PREFIX? SRC ( FIPO DATETIME | TIMEDATE ) CALL/SDS ADDR/S! ( CITY | PLACE_APT CITY | PLACE APT CITY | PLACE_CITY | PLACE APT_CITY | ) INFO+? ID GPS1 GPS2");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lewiscountywa.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = TRAIL_ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(0,match.start()).trim();
    }
    if (super.parseFields(DELIM.split(body, -1), 5, data)) return true;
    
    // See if we can identify and interpret some free format pages
    
    // The one thing they always have in common is a dispatch signature
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    body = body.replace('\n', ' ');
    setFieldList("CALL ADDR APT INFO");
    
    parseAddress(StartType.START_CALL, FLAG_IGNORE_AT, body, data);
    data.strAddress = cleanTerm(data.strAddress);
    if (data.strAddress.length() == 0) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = cleanTerm(body);
    } else {
      data.strCall = cleanTerm(data.strCall);
      String left = cleanTerm(getLeft());
      if (data.strCall.length() == 0) {
        data.strCall = left;
      } else {
        data.strSupp = left;
      }
      
      int pt = data.strAddress.indexOf(';');
      if (pt >= 0) {
        data.strApt = append(data.strApt, "-", data.strAddress.substring(pt+1).trim());
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
    }
    return true;
  }
  
  private String cleanTerm(String term) {
    Matcher match = CLEAN_END_PTN.matcher(term);
    if (match.matches()) term = match.group(1);
    return term;
  }
  private static final Pattern CLEAN_END_PTN = Pattern.compile("[-; ]*(.*?)[-; ]*");

  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new MyPrefixField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("FIPO")) return new SkipField("fipo", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("PLACE_APT")) return new MyPlaceAptField();
    if (name.equals("PLACE_CITY")) return new MyPlaceCityField();
    if (name.equals("APT_CITY")) return new MyAptCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("D\\d{8}", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }
  
  private class MyPrefixField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("Callback")) {
        data.strCall = field;
        return true;
      } else {
        return field.equals("Assigned");
      }
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return null;
    }
  }
  
  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "_");
      super.parse(field, data);
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]");
  private class MyPlaceAptField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (APT_PTN.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private class MyPlaceCityField extends MyPlaceAptField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String tmp = parseCity(field, data);
      if (tmp == null) return false;
      super.parse(tmp, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyAptCityField extends AptField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String tmp = parseCity(field, data);
      if (tmp == null) return false;
      super.parse(tmp, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private static final Pattern FIELD_CITY_PTN = Pattern.compile("(.*) (?!DOT|MIN)([A-Z]{3})");

  private String parseCity(String field, Data data) {
    if (!data.strCity.isEmpty()) return null;
    Matcher match = FIELD_CITY_PTN.matcher(field);
    if (!match.matches()) return null;
    String city = CITY_CODES.getProperty(match.group(2));
    if (city == null) return null;
    data.strCity = city;
    return match.group(1).trim();
  }
  
  private static final Pattern INFO_TIME_DATE_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4}) - .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_TIME_DATE_PTN.matcher(field);
      if (match.matches()) {
        data.strTime = match.group(1);
        data.strDate = match.group(2);
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{8,9}");
  private static final Pattern GPS_PTN2 = Pattern.compile("[-+]?\\d*");
  private class MyGPSField extends GPSField {
    
    public MyGPSField(int type) {
      super(type);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (GPS_PTN.matcher(field).matches()) {
        int pt = field.length()-6;
        field = field.substring(0,pt) + '.' + field.substring(pt);
        super.parse(field, data);
      } else {
        if (!GPS_PTN2.matcher(field).matches()) abort();
      }
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ABD", "ABERDEEN",
      "ADN", "ADNA",
      "AMS", "AMSTERDAM",
      "ASH", "ASHFORD",
      "BOI", "BOISTFORT",
      "BOW", "BOW",
      "BUC", "BUCODA",
      "CAS", "CASTLE ROCK",
      "CDA", "COEUR D'ALENE",
      "CEN", "CENTRALIA",
      "CHE", "CHEHALIS",
      "CIN", "CINEBAR",
      "CIS", "CISPUS",
      "CLE", "CLEARWATER",
      "CUR", "CURTIS",
      "DOT", "DOTY",
      "DRY", "DRYAD",
      "EIN", "EINDHOVEN",
      "ELB", "ELBE",
      "ELM", "ELMA",
      "ETH", "ETHEL",
      "FOP", "FORDS PRAIRIE",
      "FOR", "FORKS",
      "GAL", "GALVIN",
      "GLE", "GLENOMA",
      "HOQ", "HOQUIAM",
      "KAL", "KALAMA",
      "KEL", "KELSO",
      "LAC", "LACEY",
      "LEW", "LEWIS COUNTY",
      "LOG", "LOGAN",
      "LON", "LONGVIEW",
      "MAC", "MARY'S CORNER",
      "MAR", "MARYSVILLE",
      "MAY", "MAYFIELD",
      "MIN", "MINERAL",
      "MLC", "MOSES LAKE",
      "MON", "MONTESANO",
      "MOR", "MORTON",
      "MOS", "MOSSY ROCK",
      "NA5", "CHEHALIS",
      "NAP", "NAPAVINE",
      "OAK", "OAKVILLE",
      "OHA", "OHANAPECOSH",
      "OLY", "OLYMPIA",
      "ONA", "ONALASKA",
      "PAC", "PACKWOOD",
      "PEE", "PE ELL",
      "PIE", "PIERCE COUNTY",
      "PLU", "PLUVIUS",
      "RAN", "RANDALL",
      "RAY", "RAYMOND",
      "REN", "RENTON",
      "ROC", "ROCHESTER",
      "RYD", "RYDERWOOD",
      "SAL", "SALKUM",
      "SEA", "SEATTLE",
      "SIL", "SILVER CREEK",
      "TAC", "TACOMA",
      "TEN", "TENINO",
      "TOL", "TOLEDO",
      "TOU", "TOUTLE",
      "TUM", "TUMWATER",
      "VAD", "VADER",
      "VAN", "VANCOUVER",
      "WEN", "WENATCHEE",
      "WHI", "WHITE PASS",
      "WIN", "WINLOCK",
      "YAK", "YAKIMA",
      "YEL", "YELM"
  });
}
