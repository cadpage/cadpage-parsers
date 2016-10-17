package net.anei.cadpage.parsers.MD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDSaintMarysCountyParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("\\b\\d\\d:\\d\\d:\\d\\d\\*");
  private static final Pattern PLACE = Pattern.compile("\\*\\*([^*]+)\\*\\*");
  
  public MDSaintMarysCountyParser() {
    super("SAINT MARYS COUNTY", "MD");
    setFieldList("TIME CALL ADDR APT X PLACE CITY UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "mplus@co.saint-marys.md.us,mplus@STMARYSMD.COM,777,888";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.start()).trim();
    if (body.endsWith(" stop")) body = body.substring(0,body.length()-5).trim();
    
    // Special case, field delimited by double starts is a place name
    // that should be removed from the message string
    match = PLACE.matcher(body);
    if (match.find()) {
      data.strPlace = body.substring(match.start(1), match.end(1));
      body = body.substring(0, match.start()+1) + body.substring(match.end());
    }
    
    String[] flds = body.split("\\*+");
    if (flds.length < 4) return false;
    
    Result lastResult = null;
    String lastFld = null;
    boolean mutualAid = true;
    int ndx = 0;
    for (String fld : flds) {
      fld = fld.trim();
      
      switch (ndx++) {
      
      case 0:
        data.strTime = fld;
        break;
      
      case 1:
        // Call description
        data.strCall = fld;
        mutualAid = fld.startsWith("Mutual Aid");
        break;
        
      case 2:
        // Address line
        
        // If line ends with intersection, it is positively the
        // address field.  Any previously found field goes into the place
        // field, and we process the next intersecting address field.
        if (fld.endsWith(" INTERSECTN")) {
          if (lastFld != null) data.strPlace = lastFld;
          parseAddress(StartType.START_ADDR, fld.substring(0, fld.length()-11), data);
          data.strApt = append(data.strApt, "-", getLeft());
          break;
        }
        
        // If mutual aid call, this is the only address
        // don't bother looking for a place field
        if (mutualAid) {
          parseAddress(fld, data);
          break;
        }
        
        // Otherwise parse the address.  We always parse the first two
        // fields to see which one has the best address
        Result result = parseAddress(StartType.START_ADDR, fld);
        if (lastResult == null) {
          lastFld = fld;
          lastResult = result;
          ndx--;
          break;
        }
        
        // If this field looks better than the previous one
        // treat the prev field as a place and and parse this an address;
        if (lastResult.getStatus() < result.getStatus()) {
          data.strPlace = lastFld;
          result.getData(data);
          data.strApt = append(data.strApt, "-", result.getLeft());
          break;
        }
        
        // If the previous field looks like the better than this one
        // parse the previous address and drop through to treat this
        // one as the first cross street
        lastResult.getData(data);
        data.strApt = append(data.strApt, "-", lastResult.getLeft());
        ndx++;
        
      case 3:
        // Cross streets * City

        // If identified city, process city field and progress to next UNIT field
        fld = fld.toUpperCase();
        if (CITY_LIST.contains(fld)) {
          data.strCity = fld;
          String newCity = CITY_CHANGES.getProperty(fld);
          if (newCity != null) {
            if (!newCity.equals("CHAR HALL") && data.strPlace.length() == 0) {
              data.strPlace = data.strCity;
            }
            data.strCity = newCity;
          }
          break;
        }
        
        // If identified unit field, drop through to next field
        if (isUnitField(fld)) {
          ndx++;
        }
        
        // Otherwise accumulate cross street and repeat this field
        else {
          data.strCross = append(data.strCross, " / ", fld);
          ndx--;
          break;
        }
        
      case 4:
        // Units
        data.strUnit = fld;
        break;
        
      case 5:
        // Description
        data.strSupp = append(data.strSupp, " / ", fld);
        ndx--;
        break;
      }
    }
    
    return true;
  }
  
  /*
   * Determine if field is city or unit field
   */
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+[0-9]+|ALS");
  private boolean isUnitField(String field) {
    for (String unit : field.split(" +")) {
      if (!UNIT_PTN.matcher(unit).matches()) return false;
    }
    return true;
  }
  
  private static Set<String> CITY_LIST = new HashSet<String>(Arrays.asList(new String[]{
      "CALIFORNIA",
      "CEDAR COVE",
      "CEDARCOVE",
      "CHAR HALL",
      "CHARLOTTE HALL",
      "CHESTNUT HILLS",
      "GOLDEN BEACH",
      "LEXINGTON PARK",
      "ABELL",
      "AVENUE",
      "BAREFOOT ACRES",
      "BEACHVILLE-ST INIGOES",
      "BUSHWOOD",
      "CALLAWAY",
      "CHAPTICO",
      "CLEMENTS",
      "COLTONS POINT",
      "COUNTRY LAKES",
      "COMPTON",
      "DAMERON",
      "DRAYDEN",
      "ESPERANZA FARMS",
      "FIRST COLONY",
      "GLEN FOREST NAWC",
      "GREAT MILLS",
      "HELEN",
      "HERMANVILLE",
      "HOLLYWOOD",
      "LAUREL GROVE",
      "LEONARDTOWN",
      "LEXINGTON PARK",
      "LORD CALVERT TRLPK",
      "LOVEVILLE",
      "MADDOX",
      "MECHANICSVILLE",
      "MEDLEYS NECK",
      "MORGANZA",
      "NEW MARKET",
      "OAKVILLE",
      "ORAVILLE",
      "PARK HALL",
      "PINEY POINT",
      "REDGATE",
      "RIDGE",
      "SAN SOUCI",
      "SCOTLAND",
      "SOUTH HAMPTON",
      "SPRING RIDGE",
      "ST INIGOES",
      "ST JAMES",
      "ST MARYS CITY",
      "TALL TIMBERS",
      "TOWN CREEK",
      "VALLEY LEE",
      "WILDEWOOD",
      
      "CALVERT COUNTY"
  }));
  
  private static final Properties CITY_CHANGES = buildCodeTable(new String[]{
      "CHAR HALL", "CHARLOTTE HALL",
      
      "BAREFOOT ACRES", "CALIFORNIA",
      "ESPERANZA FARMS","CALIFORNIA",
      "FIRST COLONY",   "CALIFORNIA",
      "SAN SOUCI",      "CALIFORNIA",
      "TOWN CREEK",     "CALIFORNIA",

      "CEDAR COVE",   "LEXINGTON PARK",
      "CEDARCOVE",    "LEXINGTON PARK",
      "GLEN FOREST NAWC", "LEXINGTON PARK",
      "LORD CALVERT TRLPK", "LEXINGTON PARK",
      "HERMANVILLE",  "LEXINGTON PARK",
      "SOUTH HAMPTON","LEXINGTON PARK",
      "SPRING RIDGE", "LEXINGTON PARK",
      "ST JAMES",     "LEXINGTON PARK",
      
      "MEDLEYS NECK", "LEONARDTOWN"
  });
}
