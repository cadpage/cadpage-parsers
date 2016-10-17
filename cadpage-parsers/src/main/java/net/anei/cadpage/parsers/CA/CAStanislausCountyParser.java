package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAStanislausCountyParser extends FieldProgramParser {

  public CAStanislausCountyParser() {
    super(CITY_CODES, "STANISLAUS COUNTY", "CA", 
          "Address:ADDR ADDR2? ( CITY ZIP_X? | CITY_MAP_X | ) EMPTY SIMS_Info:CH? Alarm_Level:PRI? Handling_Unit:UNIT? Agency:SRC! Response_Type:CALL! Dispatch_Time:TIME! END");
  }
  
  @Override
  public String getFilter() {
    return "vipercad@stanislaussheriff.com,noreply@tiburoninc.com,@modestopd.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern DELIM = Pattern.compile("[,;]");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(DELIM.split(body), data)) return false;
    if (data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("CITY_MAP_X")) return new MyCityMapCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZIP_X")) return new MyZipCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static Pattern ADDR_APT_PTN = Pattern.compile("^(APT?|RM|ROOM|UNIT|SPC?|SPACE)[- ]*");
  private static Pattern ADDR_PTN = Pattern.compile("([^\\(\\)]*?)(?:\\(([^\\(\\)]*?)\\))");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Identify place format
      Matcher mat;
      if (field.startsWith("@")) {
        field = field.substring(1).trim();
        if (field.endsWith(")")) {
          int pt = field.lastIndexOf('(');
          if (pt >= 0) {
            parseAddress(field.substring(pt+1, field.length()-1).trim(), data);
            field = field.substring(0,pt).trim();
          }
        }
        int pt = field.lastIndexOf('#');
        if (pt >= 0) {
          String apt = cleanApt(field.substring(pt+1).trim());
          data.strApt = append(data.strApt, "-", apt);
          field = field.substring(0,pt).trim();
        }
        data.strPlace = field;
      }
      else if ((mat = ADDR_PTN.matcher(field)).matches()) {
        parseAddress(mat.group(1).trim(), data);
        data.strPlace = mat.group(2).trim();
      }
      else parseAddress(field, data);
    }
    

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT";
    }
  }
  
  private static Pattern ADDR2_PTN = Pattern.compile("((?:CO )?[A-Z]{2}) *(?:/(.*)|\\((?!MapBook:)(.*)\\)|#BLK)");

  private class MyAddress2Field extends AddressField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ADDR2_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCity = getCityCode(match.group(1));
      String addr = match.group(2);
      if (addr == null) addr = match.group(3);
      if (addr != null) {
        data.strAddress = append(data.strAddress, " & ", addr.trim());
      } else {
        data.strAddress = append(data.strAddress, " ", "BLK");
      }
      return true;
    }
   
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY? ADDR APT";
    }
  }
  
  private class MyCityField extends CityField {
    
    public MyCityField() {
      super("(?:CO )?[A-Z]{2}", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCity = getCityCode(field);
    }
  }

  private static final Pattern CITY_MAP_X_PTN = Pattern.compile("((?:CO )?[A-Z]{2}) *(?:#(.*?) *)?\\(MapBook:(.*?)\\)(?: *\\((.*)\\))?");
  
  private class MyCityMapCrossField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CITY_MAP_X_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCity = getCityCode(match.group(1));
      String apt = match.group(2);
      if (apt != null) {
        data.strApt = append(data.strApt, "-", cleanApt(apt));
      }
      data.strMap = match.group(3).trim();
      String cross = match.group(4);
      if (cross != null) {
        cross = cross.trim();
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY MAP X";
    }
  }

  private static final Pattern ZIP_X_PTN = Pattern.compile("(\\d{5})(?: *\\((.*)\\))?");
  
  private class MyZipCrossField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ZIP_X_PTN.matcher(field);
      if (!match.matches()) return false;
      if (data.strCity.length() == 0) data.strCity = match.group(1);
      String cross = match.group(2);
      if (cross != null) {
        cross = cross.trim();
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY MAP X";
    }
  }
  
  private String cleanApt(String apt) {
    return ADDR_APT_PTN.matcher(apt.trim()).replaceFirst("");
  }
  
  private String getCityCode(String code) {
    code = stripFieldStart(code, "CO ");
    return convertCodes(code, CITY_CODES);
  }


  private static Properties CITY_CODES = buildCodeTable(new String[] {
      "CE", "CERES",
      "CL", "CROWS LANDING",
      "DE", "DENAIR",
      "DG", "DIABLO GRANDE",
      "EP", "EMPIRE",
      "GR", "GRAYSON",
      "HK", "HICKMAN",
      "HU", "HUGHSON",
      "KE", "KEYES",
      "KF", "KNIGHTS FERRY",
      "LG", "LA GRANGE",
      "MO", "MODESTO",
      "NM", "NEWMAN",
      "OD", "OAKDALE",
      "PS", "PATTERSON",
      "RB", "RIVERBANK",
      "SA", "SALIDA",
      "TU", "TURLOCK",
      "VN", "VERNALIS",
      "VH", "VALLEY HOME",
      "WE", "WESTLEY",
      "WF", "WATERFORD"
  });
  
}
