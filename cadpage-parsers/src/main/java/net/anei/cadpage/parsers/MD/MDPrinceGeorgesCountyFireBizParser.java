package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Prince Georges County, MD
 */
public class MDPrinceGeorgesCountyFireBizParser extends MDPrinceGeorgesCountyBaseParser {
  
  public MDPrinceGeorgesCountyFireBizParser() {
    super("MAPCALL! ( Date_Time:DATETIME! ( Dispatch:SKIP! | Backup:SKIP! ) Assignment:UNIT! Location:ADDR! Printout:URL! | UNIT! ADDR! EXTRA+? URL! ) END");
    addExtendedDirections();
  }
  
  @Override
  public String getFilter() {
    return "@fireblitz.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAPCALL")) return new MapCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("URL")) return new MyInfoUrlField();
    return super.getField(name);
  }
  
  private class MapCallField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      
      // First line contains a station ID & map page followed by a colon and call description
      Parser p = new Parser(field);
      data.strCall = p.getLastOptional(':');
      if (data.strCall.length() == 0) abort();
      String source = p.get('-');
      data.strMap = p.get();
      
      // First two characters *MIGHT* be a county code
      if (source.length() >= 2) {
        String code = source.substring(0,2);
        String county = COUNTY_CODES.getProperty(code);
        if (county != null) {
          data.strCity = county;
          if (code.equals("DC")) data.strState = "DC";
          source = source.substring(2);
        }
      }
      data.strSource = source;
    }
    
    @Override
    public String getFieldNames() {
      return "SRC CITY ST MAP CALL";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strUnit = p.get(',');
      data.strSupp = p.get();
    }
  }
  
  private static final Pattern CITY_CODE_PTN = Pattern.compile("-([A-Z]{2})\\b");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(?!7 11)([A-Z]\\d*|\\d{1,3}) +(.*)");
  private static final Pattern DIR_AT_PTN = Pattern.compile("([NSEW]B) (AT .*)");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
    
      // Third line is address, possibly with cross streets in parens
      // or square brackets
      if (field.startsWith("PG ")) field = field.substring(3).trim();
      int pt1 = field.indexOf('(');
      if (pt1 < 0) pt1 = field.indexOf('[');
      String sAddress;
      if (pt1 < 0) {
        sAddress = field;
      } else {
        sAddress = field.substring(0, pt1).trim();
        char cEnd = (field.charAt(pt1)=='(' ? ')' : ']');
        int pt2 = field.indexOf(cEnd, pt1+1);
        if (pt2 < 0) pt2 = field.length();
        String sCross = field.substring(pt1+1, pt2);
        if (checkAddress(sCross) >= STATUS_FULL_ADDRESS) {
          data.strPlace = sAddress;
          parseAddress(sCross, data);
          return;
        }
        data.strCross = sCross;
      }
      pt1 = sAddress.indexOf(',');
      if (pt1 >= 0) sAddress = sAddress.substring(0, pt1).trim();

      Matcher match = CITY_CODE_PTN.matcher(sAddress);
      if (match.find()) {
        data.strCity = convertCodes(match.group(1), CITY_CODES);
        data.strPlace = sAddress.substring(match.end()).trim();
        sAddress = sAddress.substring(0,match.start()).trim();
        super.parse(sAddress, data);
      }
      
      else {
        parseAddress(StartType.START_ADDR, sAddress, data);
        String left = getLeft();
        if (left.startsWith("/")) {
          data.strAddress = append(data.strAddress, " & ", left.substring(1).trim());
        } else if (left.length() <= 5) {
          data.strApt = append(data.strApt, "-", left);
        } else {
          match = APT_PLACE_PTN.matcher(left);
          if (match.matches()) {
            data.strApt = append(data.strApt, "-", match.group(1));
            left = match.group(2);
          }
          else if ((match = DIR_AT_PTN.matcher(left)).matches()) {
            data.strAddress = append(data.strAddress, " ", match.group(1));
            left = match.group(2);
          }
          data.strPlace = left;
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE X";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d) (\\d\\d:\\d\\d)");
  private class MyExtraField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
      } else {
        if (field.startsWith("Notes:")) field = field.substring(6).trim();
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
  
  private class MyInfoUrlField extends InfoUrlField {
    public MyInfoUrlField() {
      super("http://fireblitz.com/.*", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field.substring(21));
      String county = COUNTY_CODES.getProperty(p.get('/'));
      if (county != null) data.defCity = county;
      super.parse(field, data);
    }
  }
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "PG", "PRINCE GEORGES COUNTY",
      "CH", "CHARLES COUNTY",
      "AA", "ANNE ARUNDEL COUNTY",
      "HO", "HOWARD COUNTY",
      "DC", "WASHINGTON"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BP", "BROOKLYN PARK",
      "GB", "GLEN BERNIE",
      "HA", "HANOVER",
      "LH", "LINTHICUM HEIGHTS",
      "MV", "MILLERSVILLE",
      "PA", "PASSADENA"
  });
}
