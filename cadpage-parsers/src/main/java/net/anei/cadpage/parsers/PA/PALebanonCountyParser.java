package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lebanon County, PA
 */
public class PALebanonCountyParser extends SmartAddressParser {

  public PALebanonCountyParser() {
    super("LEBANON COUNTY", "PA");
    setFieldList("SRC TIME DATE CITY ADDR APT PLACE X PRI CALL UNIT BOX");
    removeWords("ALY");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY;
  }
  
  @Override
  public String getFilter() {
    return "km911alert@gmail.com,km911@fastmail.fm,7176798487";
  }
  
  private static final Pattern DATE_TIME_PREFIX_PTN = Pattern.compile("(\\d{7}) +(\\d\\d:\\d\\d:\\d\\d) +(\\d\\d-\\d\\d-\\d\\d) +[-A-Z0-9]+ +ALPHA +\\d+ +");
  private static final Pattern SUBJECT_PTN = Pattern.compile("([ A-Za-z0-9]+)@(\\d\\d:\\d\\d)");
  private static final Pattern SRC_TIME_PFX_PTN = Pattern.compile("([ A-Za-z0-9]+)@(\\d\\d:\\d\\d) / +");
  private static final Pattern CALL_PREFIX_PTN = Pattern.compile("(?:Med Class(\\d) |((?:MED|MVA|TRAF|TRANSFER|Land Search&Rescue) )|(?<=[ a-z]|^|PLAZA|AUTOMOTIVE)((?!APT)[A-Z]{2,6} ?-(?!Box) ?))");
  private static final Pattern BOX_PTN =  Pattern.compile(" (?:(?:Box|BOX) ?([-0-9]+)|Fire-Box (?:([-0-9]+) )?EMS-Box(?: ([-0-9]+))?)\\b");
  private static final Pattern TAIL_CLASS_PTN = Pattern.compile("\\bClass (\\d) [Ff]or EMS\\b");
  private static final Pattern UNIT_PTN = Pattern.compile("(?: +|^)([A-Z]+[0-9]+(?:-[0-9]+){0,2}|[0-9]+[A-Z]+|FG[ -]?\\d+)$", Pattern.CASE_INSENSITIVE);
  private static final Pattern FG_UNIT_PTN = Pattern.compile("(FG) (\\d+)");

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // Remove date/time prefix
    Matcher match = DATE_TIME_PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strSource = match.group(1);
      data.strTime = match.group(2);
      data.strDate = match.group(3).replace('-', '/');
      body = body.substring(match.end());
    }
    
    else if ((match = SUBJECT_PTN.matcher(subject)).matches()) {
      data.strSource = match.group(1).toUpperCase().replace(" ", "");
      data.strTime = match.group(2);
    }
    
    else if ((match = SRC_TIME_PFX_PTN.matcher(body)).lookingAt()) {
      data.strSource = match.group(1).toUpperCase().replace(" ", "");
      data.strTime = match.group(2);
      body = body.substring(match.end());
    }
    
    body = stripFieldStart(body, "[");
    
    // Try to parser city and county from beginning of text
    body = parseCityAndCountyPrefix(body, data);
    
    // Then look for a priority/call prefix pattern that marks the
    // end of the address
    match = CALL_PREFIX_PTN.matcher(body);
    if (!match.find()) return false;
    String sAddress = body.substring(0,match.start()).trim();
    data.strPriority = getOptGroup(match.group(1));
    String sCallPfx = match.group(2);
    if (sCallPfx == null) sCallPfx = match.group(3);
    String sTail = body.substring(match.end()).trim();
    
    // If we did not find a city/county at beginning of text, see if
    // we can find one at the end of the identified address
    if (data.strCity.length() == 0) {
      sAddress = parseCityAndCountySuffix(sAddress, data);
    }

    pt = sAddress.indexOf('@');
    if (pt < 0) pt = sAddress.indexOf('=');
    if (pt >= 0) {
      data.strPlace = sAddress.substring(pt+1).trim();
      sAddress = sAddress.substring(0,pt).trim();
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, sAddress, data);
    } else {
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, sAddress, data);
      data.strPlace = append(data.strPlace, " - ", getLeft());
    }
    if (data.strPlace.startsWith ("AT ")) {
      String cross = data.strPlace.substring(3).trim();
      if (cross.startsWith("MM ")) {
        data.strAddress = append(data.strAddress, " ", cross);
      } else {
        data.strCross = cross;
      }
      data.strPlace = "";
    } else  {
      data.strPlace = stripFieldStart(data.strPlace, "* ");;
    }

    String sCall;
    match = BOX_PTN.matcher(sTail);
    if (! match.find()) {
      sCall = sTail;
      sTail = "";
    } else {
      sCall = sTail.substring(0,match.start()).trim();
      String sBox = match.group(1);
      if (sBox == null) {
        String fireBox = match.group(2);
        String emsBox =  match.group(3);
        fireBox = (fireBox != null ? "Fire:"+fireBox : "");
        emsBox = (emsBox != null ? "EMS:" + emsBox : "");
        sBox = append(fireBox, " ", emsBox);
      }
      data.strBox = sBox;
      sTail = sTail.substring(match.end()).trim();
      sTail = stripFieldStart(sTail, "-");
    }

    // Class priority and units can be found before or after the box fields :(
    if (sTail.length() > 0) {
      match = TAIL_CLASS_PTN.matcher(sTail);
      if (match.lookingAt()) {
        data.strPriority = match.group(1);
        sTail = sTail.substring(match.end()).trim();
      }
      data.strUnit = FG_UNIT_PTN.matcher(sTail.toUpperCase()).replaceAll("$1-$2");
    }
    
    else {
      match = TAIL_CLASS_PTN.matcher(sCall);
      if (match.find()) {
        data.strPriority = match.group(1);
        data.strUnit = sCall.substring(match.end()).trim().toUpperCase();
        sCall = sCall.substring(0,match.start()).trim();
      }
      
      else {
        while (true) {
          match = UNIT_PTN.matcher(sCall);
          if (!match.find()) break;
          data.strUnit = append(match.group(1).toUpperCase().replace(' ', '-'), " ", data.strUnit);
          sCall = sCall.substring(0,match.start()).trim();
        }
      }
    }
    
    data.strCall = ((sCallPfx == null ? "" : sCallPfx) + sCall).trim();
    if (data.strCall.length() == 0) data.strCall = "Med";
    
    // Make some validity checks to require **SOMETHING** beyond a simple call prefix match
    return data.strCity.length() > 0 || data.strBox.length() > 0 || data.strUnit.length() > 0;
  }
  
  private String parseCityAndCountyPrefix(String address, Data data) {
    
    CityParser p = new CityParser(address);
    
    // Look for a county prefix and city prefixs
    String county = p.getCounty(0);
    String city = p.getCity(0);
    
    // If we did not find anything, return failure
    if (county == null && city == null) return address;

    // If we found a city, but no county, see if the county follows the city
    if (city != null && county == null) {
      county = p.getCounty(0);
      // Sometimes there is a different township behind the county, which we ignore
      if (county != null) p.getCity(0);
    }
    
    // if we did not find a city, but there was a county prefix in front of it
    // we get a bit less strict about what qualifies as a city
    else if (county != null && city == null) {
      city = p.getSpecialCity(0);
    }
    
    buildCityName(city, county, data);
    
    // And return whatever is left
    return p.getAddress();
  }
  
  private String parseCityAndCountySuffix(String address, Data data) {
    
    CityParser p = new CityParser(address);
    
    String county = p.getCounty(1);
    String city = p.getCity(1);
    
    if (county == null && city == null) return address;
    
    if (county != null && city ==  null) {
      city = p.getSpecialCity(1);
    }
    
    buildCityName(city, county, data);
    
    // And return whatever is left
    return p.getAddress();
  }

  public void buildCityName(String city, String county, Data data) {
    // Finally put it all together
    if (city == null) city = "";
    if (county != null) {
      city = append(city, ", ", county + " COUNTY");
    }
    data.strCity = city.toUpperCase();
  }
  
  // Extracting the city from front or back of an identified address line is complicated enough to
  // rate it's own class

  private static final String DELIM = "\\W+";
  private static final String COUNTY_PTN_S = "(BERKS|DAUPHIN|LANCASTER|SCHUYLKILL)(?: CO(?:UNTY|UTNY)?)?";
  private static final Pattern[] COUNTY_PTNS = new Pattern[]{
    Pattern.compile("^"+COUNTY_PTN_S+DELIM, Pattern.CASE_INSENSITIVE),
    Pattern.compile(DELIM+COUNTY_PTN_S+"$", Pattern.CASE_INSENSITIVE)
  };
  
  private static final String CITY_PTN_S = "(?:City of ([A-Z]+)|((?:(?:[NO]ORTH|SOUTH|EAST|WEST|UPPER|LOWER) )?(?:(?:LITTLE|MOUNT|MT|NEW|PORT|ST) )?(?:[A-Z]+|COLD SPRING|DEER LAKE|PALO ALTO|SCHUYLKILL HAVEN|PINE GROVE|SINKING SPRING|TERRE HILL) (?:BORO(?:UGH)?|TWP|TOWNSHIP|CITY)))";
  private static final Pattern[] CITY_PTNS = new Pattern[]{
    Pattern.compile("^"+CITY_PTN_S+DELIM, Pattern.CASE_INSENSITIVE),
    Pattern.compile(DELIM+CITY_PTN_S+"$", Pattern.CASE_INSENSITIVE)
  };
  private static final String SPECIAL_CITY_PTN_S = "((?:(?:[NO]ORTH|SOUTH|EAST|WEST|UPPER|LOWER) )?(?:(?:LITTLE|MOUNT|MT|NEW|PORT|ST) )?[A-Z]+)";
  private static final Pattern[] SPECIAL_CITY_PTNS = new Pattern[]{
    Pattern.compile("^"+SPECIAL_CITY_PTN_S+DELIM, Pattern.CASE_INSENSITIVE),
    Pattern.compile(DELIM+SPECIAL_CITY_PTN_S+"$", Pattern.CASE_INSENSITIVE)
  };
  
  private static class CityParser {
    private String address;
    
    public CityParser(String address) {
      this.address = address;
    }
    
    public String getCounty(int ndx) {
      Matcher match =  COUNTY_PTNS[ndx].matcher(address);
      if (!match.find()) return null;
      trimAddress(match);
      return match.group(1).toUpperCase();
    }
    
    public String getCity(int ndx) {
      Matcher match = CITY_PTNS[ndx].matcher(address);
      if (!match.find()) return null;
      trimAddress(match);
      String city = match.group(1);
      if (city != null) return city.toUpperCase();
      city = match.group(2).toUpperCase();
      city = stripFieldEnd(city, "BORO");
      city = stripFieldEnd(city, "BOROUGH");
      return city;
    }
    
    public String getSpecialCity(int ndx) {
      Matcher match = SPECIAL_CITY_PTNS[ndx].matcher(address);
      if (!match.find()) return null;
      trimAddress(match);
      return match.group(1).toUpperCase();
    }
    
    public String getAddress() {
      return address;
    }

    private void trimAddress(Matcher match) {
      if (match.start() == 0) address = address.substring(match.end());
      else if (match.end() == address.length()) address = address.substring(0,match.start());
    }
  }
}
