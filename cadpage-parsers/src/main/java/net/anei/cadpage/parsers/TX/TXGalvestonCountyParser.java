package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class TXGalvestonCountyParser extends DispatchOSSIParser {
  
  public TXGalvestonCountyParser() {
    super(CITY_CODES, "GALVESTON COUNTY", "TX",
          "( CANCEL ADDR CITY! | FYI CALL ( ADDR! | PLACE ADDR! | ADDR! ) CITY? ID? PRI? DATETIME? ) INFO/N+");
    setupCityValues(CITY_CODES);
    setupCities(CITY_LIST);
  }
  
  @Override
  public String getFilter() {
    return "iammessaging.com,777,888,410,CAD@ci.dickinson.tx.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0) body = subject + ": " + body;
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern NUMBER_HWY_PTN = Pattern.compile("\\b(\\d+)(US|FT|TX|FM)\\b");
  private static final Pattern HWY_ST_PTN = Pattern.compile("\\b(HWY \\d+) (ST|RD)\\b");
  private static final Pattern NUMBER_DASH_PTN = Pattern.compile("^(\\d+)-(?![A-Z] |BLK )");
  private static final Pattern NUMBER_HALF_PTN = Pattern.compile("\\b(\\d+)-HALF\\b");
  private static final Pattern NUMBER_HALF_PTN2 = Pattern.compile("\\b(\\d+)\\.5\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      int saveLen = field.length();
      field = NUMBER_HALF_PTN.matcher(field).replaceAll("$1.5");
      boolean repHalf = field.length() != saveLen;

      field = NUMBER_HWY_PTN.matcher(field).replaceAll("$1 $2");
      
      field = HWY_ST_PTN.matcher(field).replaceAll("$1");
      
      super.parse(field, data);
      
      data.strAddress = NUMBER_DASH_PTN.matcher(data.strAddress).replaceFirst("$1 ");
      if (repHalf) data.strAddress = NUMBER_HALF_PTN2.matcher(data.strAddress).replaceAll("$1-HALF");
      if (data.strAddress.length() == 0) abort();
    }
  }
  
  private static final Pattern INFO_UNIT_PTN = Pattern.compile("[A-Z]{1,3}FD");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_UNIT_PTN.matcher(field).matches()) {
        data.strUnit = append(data.strUnit, " ", field);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT " + super.getFieldNames();
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return NUMBER_HALF_PTN.matcher(addr).replaceAll("$1");
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAYO",   "BAYO VISTA",
      "GALV",   "GALVESTON",
      "JAMA",   "JAMAICA BEACH",
      "SANL",   "SAN LEON",
      "TIKI",   "TIKI ISLAND",
      
      "SANLEON",    "SAN LEON"
  });
  
  private static final String[] CITY_LIST = new String[]{
      "CLEAR LAKE SHORES",
      "DICKINSON",
      "FRIENDSWOOD",
      "HITCHCOCK",
      "KEMAH",
      "LA MARQUE",
      "LEAGUE CITY",
      "SANTA FE",
      "TEXAS CITY",
      
      "BACLIFF",
      "BOLIVAR PENINSULA",
      
      "ALGOA",
      "CAPLEN",
      "CRYSTAL BEACH",
      "GILCHRIST",
      "HIH ISLAND",
      "PORTR BOLIVAR",
      
      "GALVESTON COUNTY"
  };
}
