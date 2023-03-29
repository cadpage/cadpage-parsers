package net.anei.cadpage.parsers.PA;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA7BaseParser;
import net.anei.cadpage.parsers.dispatch.DispatchA7Parser;

public class PAChesterCountyBaseParser extends DispatchA7Parser {

  public PAChesterCountyBaseParser(String programStr) {
    super(0, CITY_LIST, CITY_CODES, "CHESTER COUNTY", "PA", programStr);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = PA_TPK_PTN.matcher(addr).replaceAll("PENNSYLVANIA TURNPIKE");
    addr = addr.replace("SCHOOL HOUSE", "SCHOOLHOUSE");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PA_TPK_PTN = Pattern.compile("\\bPA TPK?\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    if (!super.parseFields(fields, data)) return false;
    String state = OOC_CITIES.getProperty(data.strCity);
    if (state != null) data.strState = state;
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDRPL")) return new AddressPlaceField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("ADDRCITY2")) return new AddressCity2Field();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("X2")) return new Cross2Field();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("PLACE_DASH")) return new BasePlaceDashField();
    if (name.equals("DATE")) return new BaseDateField();
    if (name.equals("TIME")) return new BaseTimeField();
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("PLACE_PHONE")) return new BasePlacePhoneField();
    return super.getField(name);
  }

  protected class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "*");
      super.parse(field, data);
    }
  }

  // ADDRPL: address - place
  protected class AddressPlaceField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE CITY";
    }
  }

  // ADDRCITY: address, citycode
  private static final Pattern ADDR_STATION_PTN = Pattern.compile("STATION +\\d+(?: *\\([A-Z]+\\))?");
  protected class BaseAddressCityField extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",") && !ADDR_STATION_PTN.matcher(field).matches()) return false;
      parseChesterAddress(field, data);
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

  // ADDRCITY2: address, city
  protected class AddressCity2Field extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt < 0) return false;
      parseAddress(field.substring(0,pt).trim(), data);
      data.strCity = field.substring(pt+1).trim();
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

  protected class BaseCityField extends DispatchA7BaseParser.CityField {
    @Override
    public void parse(String field, Data data) {
      // DOn't overwrite previous contents if this field is empty
      if (field.length() > 0) super.parse(field, data);
    }
  }

  // X2: must contain &
  protected class Cross2Field extends CrossField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("&")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public String getFieldNames() {
      return "X";
    }
  }

  // APT: must start with APT or be <= 4 characters
  protected class BaseAptField extends AptField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      if (field.startsWith("APT ")) {
        field = field.substring(4).trim();
      } else if (field.length() > 4) return false;

      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("-")) field = field.substring(1).trim();
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      if (field.startsWith("APT ")) {
        field = field.substring(4).trim();
      }
      if (field.length() > 6) data.strPlace = field;
      else super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern PLACE_DASH_PTN = Pattern.compile("-.*|.*-|.*[^ ]-[^ ].*");
  protected class BasePlaceDashField extends  PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = PLACE_DASH_PTN.matcher(field);
      if (!match.matches()) return false;
      field = stripFieldStart(field, "-");
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern DATE_PATTERN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)?");
  private class BaseDateField extends DateField {
    public BaseDateField() {
      setPattern(DATE_PATTERN, true);
    }
  }

  private static final Pattern TIME_PATTERN = Pattern.compile("\\d\\d:\\d\\d(?::\\d\\d)?");
  protected class BaseTimeField extends TimeField {
    public BaseTimeField() {
      setPattern(TIME_PATTERN, true);
    }
  }


  protected class BaseDateTimeField extends DateTimeField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) {
        if (TIME_PATTERN.matcher(field).matches()) {
          data.strTime = field;
          return true;
        }
        if (DATE_PATTERN.matcher(field).matches()) {
          data.strDate = field;
          return true;
        }
        return false;
      }

      else {
        String tmp = field.replaceAll("\\d", "N");
        return "NN/NN/NNNN".startsWith(tmp) ||
                "NN:NN:NN".startsWith(tmp);
      }
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }


  private static final Pattern NOT_PLACE_PHONE_PTN = Pattern.compile("\\d{4}");
  private static final Pattern APT_PTN = Pattern.compile("^(?:APT|RM) *([^ \\-]*)[- ]*");
  private static final Pattern PHONE_PTN = Pattern.compile("(.*?)[-#/ ]*\\b((?:CP)?\\d{3}[-\\.]?\\d{3}[-\\.]?\\d{4})\\b[-#/ ]*(.*?)");

  protected class BasePlacePhoneField extends PlaceField {

    @Override
    public void parse(String field, Data data) {

      // First check to make sure this isn't something else
      // in particular a 4 digit box number
      if (NOT_PLACE_PHONE_PTN.matcher(field).matches()) abort();

      Matcher match = APT_PTN.matcher(field);
      if (match.find()){
        data.strApt = getOptGroup(match.group(1));
        field = match.replaceFirst("");
      }

      match = PHONE_PTN.matcher(field);
      if (match.matches()){
        data.strPhone = match.group(2);
        field = append(match.group(1), " ", match.group(3));
      }

      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      if (field.startsWith("-")) field = field.substring(1,field.length()).trim();
      data.strPlace = append(data.strPlace, " - ", field).trim();
    }

    @Override
    public String getFieldNames(){
      return "APT PLACE PHONE";
    }
  }

  public void parseChesterAddress(String field, Data data) {
    parseAddressA7(field, data);
  }

  public String convertCityCode(String city) {
    return convertCodes(city, CITY_CODES);
  }

  @Override
  public String adjustMapCity(String city) {
    String tmp = CITY_MAP_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) return tmp;
    return city;
  }

  private static final String[] CITY_LIST = new String[]{
    /* 00 */ "",
    /* 01 */ "WEST CHESTER",
    /* 02 */ "MALVERN",
    /* 03 */ "KENNETT SQUARE",
    /* 04 */ "AVONDALE",
    /* 05 */ "WEST GROVE",
    /* 06 */ "OXFORD",
    /* 07 */ "ATGLEN",
    /* 08 */ "PARKESBURG",
    /* 09 */ "SOUTH COATESVILLE",
    /* 10 */ "MODENA",
    /* 11 */ "DOWNINGTOWN",
    /* 12 */ "HONEY BROOK",
    /* 13 */ "ELVERSON",
    /* 14 */ "SPRING CITY",
    /* 15 */ "PHOENIXVILLE",
    /* 16 */ "COATESVILLE",
    /* 17 */ "NORTH COVENTRY TWP",
    /* 18 */ "EAST COVENTRY TWP",
    /* 19 */ "WARWICK",
    /* 20 */ "POTTSTOWN",
    /* 21 */ "EAST VINCENT TWP",
    /* 22 */ "HONEY BROOK TWP",
    /* 23 */ "WEST NANTMEAL TWP",
    /* 24 */ "EAST NANTMEAL TWP",
    /* 25 */ "WEST VINCENT TWP",
    /* 26 */ "EAST PIKELAND TWP",
    /* 27 */ "SCHUYLKILL TWP",
    /* 28 */ "WEST CALN TWP",
    /* 29 */ "WEST BRANDYWINE TWP",
    /* 30 */ "EAST BRANDYWINE TWP",   // or BRANDYWINE REGIONAL POLICE??
    /* 31 */ "WALLACE TWP",
    /* 32 */ "UPPER UWCHLAN TWP",
    /* 33 */ "UWCHLAN TWP",
    /* 34 */ "WEST PIKELAND TWP",
    /* 35 */ "CHARLESTOWN TWP",
    /* 36 */ "WEST SADSBURY TWP",
    /* 37 */ "SADSBURY TWP",
    /* 38 */ "VALLEY TWP",
    /* 39 */ "CALN TWP",
    /* 40 */ "EAST CALN TWP",
    /* 41 */ "WEST WHITELAND TWP",
    /* 42 */ "EAST WHITELAND TWP",
    /* 43 */ "TREDYFFRIN TWP",
    /* 44 */ "WEST FALLOWFIELD TWP",
    /* 45 */ "HIGHLAND TWP",
    /* 46 */ "LONDONDERRY TWP",
    /* 47 */ "EAST FALLOWFIELD TWP",
    /* 48 */ "WEST MARLBOROUGH TWP",
    /* 49 */ "NEWLIN TWP",
    /* 50 */ "WEST BRADFORD TWP",
    /* 51 */ "EAST BRADFORD TWP",
    /* 52 */ "WEST GOSHEN TWP",
    /* 53 */ "EAST GOSHEN TWP",
    /* 54 */ "WILLISTOWN TWP",
    /* 55 */ "EASTTOWN TWP",
    /* 56 */ "LOWER OXFORD TWP",
    /* 57 */ "UPPER OXFORD TWP",
    /* 58 */ "PENN TWP",
    /* 59 */ "LONDON GROVE TWP",
    /* 60 */ "NEW GARDEN TWP",
    /* 61 */ "EAST MARLBOROUGH TWP",
    /* 62 */ "KENNETT TWP",
    /* 63 */ "POCOPSON TWP",
    /* 64 */ "PENNSBURY TWP",
    /* 65 */ "BIRMINGHAM TWP",
    /* 66 */ "THORNBURY TWP",
    /* 67 */ "WESTTOWN TWP",
    /* 68 */ "WEST NOTTINGHAM TWP",
    /* 69 */ "EAST NOTTINGHAM TWP",
    /* 70 */ "ELK TWP",
    /* 71 */ "NEW LONDON TWP",
    /* 72 */ "FRANKLIN TWP",
    /* 73 */ "LONDON BRITAIN",
    /* 74 */ "",
    /* 75 */ "WEST CHESTER",    // WEST CHESTER UNIVERSITY PD
    /* 76 */ "OXFORD",          // LINCOLN UNIVERSITY
    /* 77 */ "COATESVILLE",     // COATESVILLE VA MEDICAL CENTER POLICE
    /* 78 */ "WEST CHESTER",    // WEST CHESTER AREA SCHOOL DISTRICT SECURITY
    /* 79 */ "COATESVILLE",     // COATESVILLE AREA SCHOOL DISTRICT POLICE
    /* 80 */ "",
    /* 81 */ "",                // PENNSYLVANIA FISH AND BOAT COMMISSION
    /* 82 */ "DOWNINGTON",      // MARSH CREEK STATE PARK RANGERS
    /* 83 */ "POTTSTOWN",       // WARWICK COUNTY PARK
    /* 84 */ "COATESVILLE",     // HIBERNIA COUNTY PARK
    /* 85 */ "NOTTINGHAM",      // NOTTINGHAM COUNTY PARK
    /* 86 */ "GLENMOORE",       // SPRINGTON MANOR COUNTY PARK
    /* 87 */ "",
    /* 88 */ "NEW CASTLE COUNTY",
    /* 89 */ "",
    /* 90 */ "",
    /* 91 */ "RADNOR"
  };

  protected static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AMTY",         "AMITY TWP",
      "ATGLN",        "ATGLEN",
      "AVNDAL",       "AVONDALE",
      "BRDSBORO",     "BIRDSBORO",
      "BIRMHM",       "BIRMINGHAM TWP",
      "BYRTWN",       "BOYERTOWN",
      "CALN",         "CALN TWP",
      "CARNVON",      "CAERNARVON TWP",
      "CARVNB",       "CAERNARVON TWP",
      "CHARLS",       "CHARLESTOWN TWP",
      "CHDS FRD",     "CHADDS FORD",
      "CHRISTNA",     "CHRISTIANA",
      "CLN",          "CALN TWP",
      "COATVL",       "COATESVILLE",
      "COLBRKDLE",    "COLEBROOKDALE",
      "DELCO",        "DELAWARE COUNTY",
      "DGLSS",        "DOUGLASS TWP",
      "DNGTWN",       "DOWNINGTOWN",
      "EASTW",        "EASTTOWN TWP",
      "EASTWN",       "EASTTOWN TWP",
      "EBRAD",        "EAST BRADFORD TWP",
      "EBRAND",       "EAST BRANDYWINE TWP",
      "ECALN",        "EAST CALN TWP",
      "ECVNTY",       "EAST COVENTRY TWP",
      "EFALLO",       "EAST FALLOWFIELD TWP",
      "EGOSHN",       "EAST GOSHEN TWP",
      "ELKTP",        "ELK TWP",
      "ELVRSN",       "ELVERSON",
      "EMARLB",       "EAST MARLBOROUGH TWP",
      "ENANT",        "EAST NANTMEAL TWP",
      "ENOTT",        "EAST NOTTINGHAM TWP",
      "EPIKEL",       "EAST PIKELAND TWP",
      "EVCNTY",       "EAST COVENTRY TWP",
      "EVINCT",       "EAST VINCENT TWP",
      "EWHITE",       "EAST WHITELAND TWP",
      "EXTR",         "EXETER",
      "FRNKLN",       "FRANKLIN TWP",
      "HBBORO",       "HONEY BROOK",
      "HBTWP",        "HONEY BROOK TWP",
      "HGHLND",       "HIGHLAND TWP",
      "HKSN",         "HOCKESSIN",
      "KNTSQR",       "KENNETT SQUARE",
      "KNTTWP",       "KENNETT TWP",
      "LANCA",        "LANCASTER",
      "LANCO",        "LANCASTER COUNTY",
      "LDNBRT",       "LONDON BRITAIN",
      "LGROVE",       "LONDON GROVE TWP",
      "LMRCK",        "LIMERICK TWP",
      "LONDER",       "LONDONERRY TWP",
      "LWPRVDNCE",    "LOWER PROVIDENCE",
      "LWPTSG",       "LOWER POTTSGROVE TWP",
      "LWPTSGRV",     "LOWER POTTSGROVE TWP",
      "LWROXF",       "LOWER OXFORD TWP",
      "MALVR",        "MALVERN",
      "MALVRN",       "MALVERN",
      "MLCRK",        "MILL CREEK",
      "MLCRK NEW C DE","MILL CREEK",
      "MODNA",        "MODENA",
      "NCC",          "NEW CASTLE COUNTY",
      "NCVNTY",       "NORTH COVENTRY TWP",
      "NEWLN",        "NEWLIN TWP",
      "NEWLIN",       "NEWLIN TWP",
      "NEWLN",        "NEWLIN TWP",
      "NEWLND",       "NEW LONDON TWP",
      "NEWLON",       "NEW LONDON TWP",
      "NGARDN",       "NEW GARDEN TWP",
      "NRTHES",       "NORTH EAST",
      "NWHNVR",       "NEW HANOVER TWP",
      "NWMRGAN",      "NEW MORGAN",
      "OXFORD",       "OXFORD",
      "OXFRD",        "OXFORD",
      "PENN",         "PENN TWP",
      "PENNTP",       "PENN TWP",
      "PHNXVL",       "PHOENIXVILLE",
      "PNSBRY",       "PENNSBURY TWP",
      "POCOPS",       "POCOPSON TWP",
      "PRKSBG",       "PARKESBURG",
      "PTTSTWN",      "POTTSTOWN",
      "QURYVI",       "QUARRYVILLE",
      "ROBSN",        "ROBINSON TWP",
      "ROYRFRD",      "ROYERSFORD",
      "RSNGSUNMCD",   "RISING SUN",
      "SADS",         "SADSBURY TWP",
      "SCHYKL",       "SCHUYLKILL TWP",
      "SCOATV",       "SOUTH COATESVILLE",
      "SCVNTY",       "SOUTH COVENTRY TWP",
      "SLISBURY",     "SALISBURY",
      "SPRCTY",       "SPRING CITY",
      "THORNB",       "THORNBURY TWP",
      "TREDY",        "TREDYFFRIN TWP",
      "TREDYF",       "TREDYFFRIN TWP",
      "UNON",         "UNION TWP",
      "UPPPRO",       "UPPER PROVIDENCE TWP",
      "UPRO",         "UPPER PROVIDENCE TWP",
      "UPROXF",       "UPPER OXFORD TWP",
      "UPUWCH",       "UPPER UWCHLAN TWP",
      "UWCHLN",       "UWCHLAN TWP",
      "VALLEY",       "VALLEY TWP",
      "VALLY",        "VALLEY TWP",
      "WALLAC",       "WALLACE TWP",
      "WARWCK",       "WARWICK",
      "WASHNTN",      "WASHINGTON TWP",
      "WBRAD",        "WEST BRADFORD TWP",
      "WBRAND",       "WEST BRANDYWINE TWP",
      "WCALN",        "WEST CALN TWP",
      "WCHEST",       "WEST CHESTER",
      "WESTWN",       "WESTTOWN TWP",
      "WFALLO",       "WEST FALLOWFIELD TWP",
      "WGOSHN",       "WEST GOSHEN TWP",
      "WGROVE",       "WEST GROVE",
      "WILLIS",       "WILLISTOWN TWP",
      "WILSTN",       "WILLISTOWN TWP",
      "WMARLB",       "WEST MARLBOROUGH TWP",
      "WNANT",        "WEST NANTMEAL TWP",
      "WNOTT",        "WEST NOTTINGHAM TWP",
      "WPIKEL",       "WEST PIKELAND TWP",
      "WSADS",        "WEST SADSBURY TWP",
      "WVINCT",       "WEST VINCENT TWP",
      "WWHITE",       "WEST WHITELAND TWP",

      // Lancaster County
      "SLISBRY",      "SALISBURY",

      // Mongtomery county
      "MONT",         "MONTGOMERY COUNTY",
      "MONTC",        "MONTGOMERY COUNTY",
      "ROYRFRD",      "ROYERSFORD",
      "UPPMERON",     "UPPER MERION TWP",
      "UPPER POTTS",  "UPPER POTTSGROVE TWP",
      "UPPER PROV",   "UPPER PROVIDENCE TWP",
      "UPPPROVDNCE",  "UPPER PROVIDENCE TWP",
      "WPTTSGRVE",    "WEST POTTSGROVE"
  });

  private static final Properties OOC_CITIES = buildCodeTable(new String[]{
      "NEW CASTLE COUNTY",      "DE",
      "NORTH EAST",             "MD",
      "HOCKESSIN",              "DE",
      "MILL CREEK",             "DE",
      "RISING SUN",             "MD"
  });

  private static final Properties CITY_MAP_TABLE = buildCodeTable(new String[]{
      "MILL CREEK",             "HOCKESSIN"
  });
}