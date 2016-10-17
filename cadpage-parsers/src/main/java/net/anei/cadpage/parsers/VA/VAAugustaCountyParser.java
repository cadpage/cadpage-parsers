package net.anei.cadpage.parsers.VA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Augusta County, VA
 */
public class VAAugustaCountyParser extends DispatchOSSIParser {
  
  private static final Pattern DELIM_PTN = Pattern.compile("(?<!CAD|DIST|FYI|Update):");
  
  
  public VAAugustaCountyParser() {
    this("AUGUSTA COUNTY", "VA");
  }
  
  public VAAugustaCountyParser(String defCity, String defState) {
    super(defCity, defState,
           "FYI? CALL! ( ADDR/SZ! END | PLACE? ADDR/S! MAP? INFO+ )");
    removeWords("MALL");
  }
  
  @Override
  public String getFilter() {
    return "cad@co.augusta.va.us";
  }
  
  @Override
  public String getAliasCode() {
    return "VAAugustaCounty";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // This may bite us someday, but it is a convenient way to identify
    // VAWaynesboroB calls
    if (subject.length() > 0) return false;
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = DELIM_PTN.matcher(body).replaceAll(";");
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Anything starting with a digit is assumed to be an address
      if (field.length() == 0) return false;
      if (Character.isDigit(field.charAt(0))) {
        parse(field, data);
        return true;
      }
      
      return super.checkParse(field, data);
    }
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        String city = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
        if (CITY_SET.contains(city.toUpperCase())) {
          data.strCity = city;
        } else {
          data.strPlace = append(data.strPlace, " - ", city);
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  // Map field recognizes and isolates a trailing map pattern
  private static final Pattern MAP_PTN = Pattern.compile("\\b\\d{3}-?\\d{2}$");
  private class MyMapField extends MapField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.find()) return false;
      field = match.group();
      if (field.indexOf('-') < 0) {
        field = field.substring(0,3) + '-' + field.substring(3);
      }
      parse(field, data);
      return true;
    }
  }
  
  // Info field contains all kinds of sloppy stuff
  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:ROOM|RM|APT) *(.*)");
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("CNTY-.*|SEOC|WEOC");
  private static final Pattern INFO_MAP_PTN = Pattern.compile("\\d{2,3}-\\d{2}");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      
      // Info field are frequently broken up by slashes :(
      for (String fld : field.split("/")) {
        fld = fld.trim();
        
        if (fld.equals(data.strAddress)) continue;
        
        String tmp = fld.toUpperCase();
        if (tmp.startsWith("PO ") || tmp.startsWith("P O ")) continue;
        Matcher match = INFO_APT_PTN.matcher(fld);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          continue;
        }
        
        if (INFO_MAP_PTN.matcher(fld).matches()) {
          data.strMap = fld;
          continue;
        }
        
        if (data.strChannel.length() == 0 && INFO_CHANNEL_PTN.matcher(fld).matches()) {
          data.strChannel = fld;
          continue;
        }
        
        int pt = fld.indexOf('(');
        if (pt >= 0) {
          String city = fld.substring(0,pt).trim();
          if (CITY_SET.contains(city.toUpperCase())) {
            data.strCity = city;
            continue;
          }
        }
        
        int addrStat = checkAddress(fld);
        if (addrStat > STATUS_MARGINAL) {
          // If this is better than a naked road, see if
          // we previously misidentified a place name as an
          // address
          if (addrStat > STATUS_STREET_NAME && data.strPlace.length() == 0 && checkAddress(data.strAddress) == STATUS_STREET_NAME) {
            data.strPlace = data.strAddress;
            data.strAddress = "";
            parseAddress(fld, data);
            continue;
          }
          data.strCross = append(data.strCross, " & ", fld);
          continue;
        }
        
        if (data.strPlace.length() == 0) {
          data.strPlace = fld;
          continue;
        }
        
        data.strSupp = append(data.strSupp, " / ", fld);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "APT MAP PLACE X INFO CH";
    }
  }

  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(
      "FISHERSVILLE",
      "STAUNTON",
      "WAYNSBORO"
  ));
  
}
