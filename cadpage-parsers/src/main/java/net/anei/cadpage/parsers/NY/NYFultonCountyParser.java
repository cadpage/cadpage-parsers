package net.anei.cadpage.parsers.NY;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYFultonCountyParser extends FieldProgramParser {

  public NYFultonCountyParser() {
    super(CITY_LIST, "FULTON COUNTY", "NY",
        "SRC CALL ADDR/Z+? ( ADDRCITY | ADDR_END CITY ) PLACE? DATE TIME! ID? ( GPS | GPS1 GPS2 | ) INFO+");
  }

  @Override
  public String getFilter() {
    return "impactpaging@co.fulton.ny.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile(",|\n-?");
  private static final Pattern MASTER = Pattern.compile("([A-Z]+) +(.*?) +(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d)(\\d\\d)(?: +(?:([-+]?\\d+\\.\\d+ +[-+]?\\d+\\.\\d+)|-1 +-1))?");
  private static final Pattern STRIP_CODE_PTN = Pattern.compile("(.*?) +[CVT]");
  
  // Address line accumulator
  List<String> addressLines = new ArrayList<String>();

  @Override
  protected boolean parseMsg(String body, Data data) {

    // See if this is a new comma delimited page
    String flds[] = DELIM.split(body);
    if (flds.length >= 7) {
      flds[0] = stripFieldStart(flds[0], "-");
      addressLines.clear();
      if (!parseFields(flds, data)) return false;
    } else { 

      // No such luck.  Have to parse it the old way
      setFieldList("SRC CALL ADDR APT CITY PLACE DATE TIME GPS");
      Matcher match = MASTER.matcher(body);
      if (!match.matches()) return false;
      data.strSource = match.group(1);
      String addr = match.group(2);
      data.strDate = match.group(3);
      data.strTime = match.group(4) + ':' + match.group(5);
      String gps = match.group(6);
      if (gps != null) setGPSLoc(gps, data);

      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, addr, data);
      data.strPlace = getLeft();
      
      match = STRIP_CODE_PTN.matcher(data.strAddress);
      if (match.matches()) data.strAddress = match.group(1);
    }
    
    data.strCity = convertCodes(data.strCity, FIX_CITY_TABLE);

    return true;
  }

  private static final String GPS_PTN_S = "(?:[-+]?\\d{2,3}\\.\\d{2,}|0|-1)";
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("|(?:T )?[A-Z]{1,6}", true);
    if (name.equals("ADDR")) return new MyAddressField(false);
    if (name.equals("ADDR_END")) return new MyAddressField(true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{2}", true);
    if (name.equals("GPS")) return new GPSField(GPS_PTN_S + " +" + GPS_PTN_S);
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN_S);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN_S);
    return super.getField(name);
  }
  
  private static final Pattern BOX_ADDR_PTN = Pattern.compile("BOX (\\d+)[- ](.*?)(?: (\\d+(?:-\\d+)?))?");
  private static final Pattern STREET_NO_PTN = Pattern.compile("\\d+(?:-\\d+)?");
  
  private class MyAddressField extends Field {
    
    private boolean end;
    
    public MyAddressField(boolean end) {
      this.end = end;
    }

    @Override
    public void parse(String field, Data data) {
      
      // We won't be able to figure out what goes where until we
      // reach the last address line.  Until then just accumulate them
      addressLines.add(field);
      if (!end) return;
      
      // Once we have everything, the real work starts.
      
      // If we didn't find anything, call it quits
      if (addressLines.isEmpty()) abort();
      
      // Start of dummy loop to calculate start and end of address lines
      int st, nd;
      do {
        
        // Look for a box number.  If found, that identifies the address field
        st = -1;
        for (int j = 0; j<addressLines.size(); j++) {
          Matcher match = BOX_ADDR_PTN.matcher(addressLines.get(j));
          if (match.matches()) {
            data.strBox = match.group(1);
            data.strPlace = match.group(2);
            addressLines.set(j, getOptGroup(match.group(3)));
            st = j;
            break;
          }
        }
        if (st >= 0) {
          nd = st + 2;
          break;
        }
        
        // If there is only one line, that makes thing easy
        if (addressLines.size() == 1) {
          st = 0;
          nd = 1;
          break;
        }
      
        // Otherwise Start by looking for a street number
        // If we found one, it and the next line constitute the address
        st = -1;
        for (int j = addressLines.size()-2; j>=0; j--) {
          if (STREET_NO_PTN.matcher(addressLines.get(j)).matches()) {
            st = j;
            break;
          }
        }
  
        if (st >= 0) {
          nd = st + 2;
          break;
        }
        
        // OK, now things get complicated
        // Default is to consider the last line to be the address line
        nd = addressLines.size();
        
        // Check address status of last line.  If
        // it is not something better than an simple street name and either
        //   the line preceding it is a valid address or
        //   the last line is valid street name
        // then make the last line a cross street and use the line in front of it as the address
        int stat2 = checkAddress(addressLines.get(nd-1));
        if (stat2 <= STATUS_STREET_NAME) {
          String prev = addressLines.get(nd-2);
          if (stat2 == STATUS_STREET_NAME && prev.length() > 0 ||
              isValidAddress(prev)) nd--;
        }
        st = nd-1;
        break;
        
      } while (false);

      // Now that we figured out where everything is, construct the proper fields
      // Everything in front of the address goes in info, anything behind it goes
      // into cross streets
      for (int j = 0; j<st; j++) {
        data.strSupp = append(data.strSupp, ", ", addressLines.get(j));
      }
      
      String addr = "";
      for (int j = st; j < nd; j++) {
        addr = append(addr, " ", addressLines.get(j));
      }
      parseAddress(addr, data);
      
      for (int j = nd; j < addressLines.size(); j++) {
        data.strCross = append(data.strCross, " / ", addressLines.get(j));
      }
      
      // And release any saved address lines
      addressLines.clear();
    }

    @Override
    public String getFieldNames() {
      return "INFO BOX PLACE ADDR APT X CITY";
    }
    
  }
  
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*) [CVT] ([A-Z]+)");
  private class MyAddressCityField extends MyAddressField {
    
    public MyAddressCityField() {
      super(true);
    }

    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Another dummy loop, checking for multiple
      // conditions that would identify the last address/city field
      do {
        
        // Check for the standard address city signature
        Matcher match = ADDR_CITY_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
          data.strCity = match.group(2);
          break;
        }
      
        // No go, see if the last word in this line is a
        // legitimate city
        int pt = field.lastIndexOf(' ');
        String city = field.substring(pt+1).trim();
        if (isCity(city)) {
          
          // This might be place name ending in a city :(
          // so we better check ahead and make sure there is
          // a date in one of the next two fields before accepting this
          if (!DATE_PTN.matcher(getRelativeField(+1)).matches() && 
              !DATE_PTN.matcher(getRelativeField(+2)).matches()) return false;
          
          // OK, go for it
          data.strCity = city;
          field = field.substring(0,pt).trim();
          break;
        }
        
        // Very occasionally, there just isn't a city field at all.  Which we
        // identify by checking to see if the next field is a date field
        if (DATE_PTN.matcher(getRelativeField(+1)).matches()) break;
        
        // Nothing works, bail out
        return false;
      } while (false);
      
      // All is well, parse the address portion of the field
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern SPEC_CITY_PTN = Pattern.compile("[CVT] [A-Z]+");
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (SPEC_CITY_PTN.matcher(field).matches()) {
        super.parse(field.substring(2).trim(), data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern DATE_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4}");
  private class MyDateField extends DateField {
    public MyDateField() {
      setPattern(DATE_PTN, true);
    }
  }

  private static final Pattern TIME_PTN = Pattern.compile("(\\d{4})(?:Hrs)?");
  private class MyTimeField extends TimeField {
    public MyTimeField() {
      setPattern(TIME_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.substring(0,2) + ':' + field.substring(2,4);
      super.parse(field, data);
    }
  }


  private static final String[] CITY_LIST = new String[]{
    "BLEECKER",
    "BROADALBIN",
    "BROADALBIN",
    "CAROGA",
    "DOLGEVILLE",
    "EPHRATAH",
    "GALWAY",
    "GLOVERSVILLE",
    "JOHNSTOWN",
    "JOHNSTOWN",
    "MAYFIELD",
    "MAYFIELD",
    "NORTHAMPTON",
    "NORTHVILLE",
    "NORTHVILLE VILLAGE",
    "OPPENHEIM",
    "PERTH",
    "STRATFORD",
    
    // Hamilton County
    "BENSON",
    "HOPE",
    "LAKE PLEASANT",
    "WELLS",
    
    // Herkimer County
    "LITTLE FALLS",
    
    // Montgomery County
    "AMSTERDAM",
    "HAGAMAN",
    "MOHAWK",
    
    // Saratoga County
    "EDINURG",
    "EDINBURG",
    "EDINBURGH",
    "ENDINBURG",
    "PROVIDENCE"
    
  };
  
  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "EDINURG",    "EDINBURG",
      "EDINBURGH",  "EDINBURG",
      "ENDINBURG",  "EDINBURG",
  });
}
