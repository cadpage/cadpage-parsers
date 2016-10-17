package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CTNewHavenCountyBParser extends SmartAddressParser {
  
  private static final String FIELD_LIST = "ID CALL PRI ADDR APT PLACE CITY MAP X UNIT DATE TIME";
  
  private Properties cityCodes = null;
  
  public CTNewHavenCountyBParser() {
    this(CITY_LIST, CITY_CODES, "NORTH BRANFORD", "CT");
  }
  
  public CTNewHavenCountyBParser(String defCity, String defState) {
    super(defCity, defState);
    setup();
  }
  
  public CTNewHavenCountyBParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setup();
  }
  
  public CTNewHavenCountyBParser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setup();
  }
  
  public CTNewHavenCountyBParser(String[] cityList, Properties cityCodes, String defCity, String defState) {
    super(cityList, defCity, defState);
    this.cityCodes = cityCodes;
    setup();
  }
  
  private void setup() {
    setFieldList(FIELD_LIST);
  }
  
  @Override
  public String getFilter() {
    return "paging@nbpolicect.org,paging@mail.nbpolicect.org,paging@easthavenfire.com,pdpaging@farmington-ct.org,noreply@whpd.com";
  }
  
  private static final Pattern MARKER = Pattern.compile("^(\\d{10}) +");
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" +(\\d{6}) (\\d\\d:\\d\\d)(?:[ ,]|$)"); 
  private static final Pattern TRUNC_DATE_TIME_PTN = Pattern.compile(" +\\d{6} [\\d:]+$| +\\d{1,6}$"); 
  private static final Pattern PRI_MARKER = Pattern.compile(" - PRI (\\d) - ");
  private static final Pattern ADDR_ST_MARKER = Pattern.compile("(.*) (\\d{5} .*)");
  private static final Pattern I_NN_HWY_PTN = Pattern.compile("\\b(I-?\\d+) +HWY\\b");
  private static final Pattern ADDR_END_MARKER = Pattern.compile("Apt ?#:|(?=(?:Prem )?Map -)");
  private static final Pattern MAP_PFX_PTN =Pattern.compile("^(?: *(?:Prem )?Map -*)+");
  private static final Pattern MAP_PTN = Pattern.compile("^\\d{1,2}(?: *[A-Z]{2} *\\d{1,3})?\\b");
  private static final Pattern MAP_EXTRA_PTN = Pattern.compile("\\(Prem Map (.*?)\\)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end());
    
    match =  DATE_TIME_PTN.matcher(body);
    if (match.find()) {
      String date = match.group(1);
      data.strDate = date.substring(2,4) + "/" + date.substring(4,6) + "/" + date.substring(0,2);
      data.strTime = match.group(2);
      data.strSupp = body.substring(match.end()).trim();
      body = body.substring(0,match.start());
    } else {
      match = TRUNC_DATE_TIME_PTN.matcher(body);
      if (match.find()) body = body.substring(0,match.start());
    }
    
    String sExtra = null;
    boolean noCross = false;
    match = ADDR_END_MARKER.matcher(body);
    if (match.find()) {
      sExtra = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
      String mark = match.group();
      if (mark.length() > 0) {
        Parser p = new Parser(sExtra);
        String token = p.get(' ');
        sExtra = p.get();
        data.strApt = token;
      }
    }
    
    body = cleanCity(body, data);
    
    StartType st = StartType.START_CALL;
    match = PRI_MARKER.matcher(body);
    if (match.find()) {
      st = StartType.START_ADDR;
      data.strCall = body.substring(0,match.start()).trim();
      data.strPriority = match.group(1);
      body = body.substring(match.end()).trim();
    }
    else if ((match = ADDR_ST_MARKER.matcher(body)).matches()) {
      st = StartType.START_ADDR;
      data.strCall = match.group(1).trim();
      body = match.group(2);
    }
    
    // Remove I-nn HWY construct that causes problems
    body = I_NN_HWY_PTN.matcher(body).replaceAll("$1");
    
    int flags = FLAG_PAD_FIELD | FLAG_CROSS_FOLLOWS;
    if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
    if (sExtra != null) flags |= FLAG_ANCHOR_END;
    parseAddress(st, flags, body, data);
    if (sExtra == null) {
      sExtra = getLeft();
      noCross = isMBlankLeft();
    }
    
    // If there is a pad field, treat it as a place or cross street
    String pad = getPadField();
    if (pad.contains("/") || isValidAddress(pad)) {
      data.strCross = append(data.strCross, " / ", stripFieldStart(pad, "/"));
    }
    else data.strPlace = pad;
    
    match = MAP_PFX_PTN.matcher(sExtra);
    if (match.find()) {
      sExtra = sExtra.substring(match.end());
      noCross = sExtra.startsWith("   ");
      sExtra = sExtra.trim();
      match = MAP_PTN.matcher(sExtra);
      if (match.find()) {
        data.strMap = match.group();
        sExtra = sExtra.substring(match.end());
        noCross = sExtra.startsWith("  ");
        sExtra = sExtra.trim();
      }
    }
    
    // Now we have to split what is left into a cross street and unit
    // If there is a premium map marker between them, things get easy
    if (sExtra.startsWith("/")) sExtra = sExtra.substring(1).trim();
    match = MAP_EXTRA_PTN.matcher(sExtra);
    if (match.find()) {
      data.strCross = append(data.strCross, " / ", sExtra.substring(0, match.start()).trim());
      sExtra = sExtra.substring(match.end()).trim();
      if (data.strMap.length() == 0) data.strMap = match.group(1).trim();
    }
    
    // If not, our best approach is to looking for the first multiple blank delimiter.
    // which is a heck of a lot easier to do now that double blanks are preserved by
    // the getLeft() method.
    else {
      if (!noCross) {
        int pt = sExtra.indexOf("  ");
        if (pt >= 0) {
          String cross = sExtra.substring(0,pt);
          if (data.strCity.length() == 0) {
            parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, cross, data);
            cross = getStart();
          }
          data.strCross = append(data.strCross, " / ", cross);
          sExtra = sExtra.substring(pt+2).trim();
        }
        
        // If we didn't find one, we will have to use the smart address parser to figure out where
        // the cross street information ends
        else {
          flags = FLAG_ONLY_CROSS;
          if (data.strCity.length() == 0) flags |= FLAG_ONLY_CITY;
          Result res = parseAddress(StartType.START_ADDR, flags, sExtra);
          if (res.isValid()) {
            res.getData(data);
            sExtra = res.getLeft();
          }
        }
      }
    }
    
    // If we have not found a city, see if there is one here
    String city = data.strCity;
    data.strCity = "";
    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, sExtra, data);
    if (data.strCity.length() > 0) sExtra = getLeft();
    if (city.length() > 0) data.strCity = city;
    
    // Whatever is left becomes the unit
    data.strUnit = sExtra.replaceAll("  +", " ");
    
    if (cityCodes != null) data.strCity = convertCodes(data.strCity, cityCodes);
    return true;
  }
  
  private String cleanCity(String addr, Data data) {
    addr = SR_PTN.matcher(addr).replaceAll("SQ");
    Matcher match = CITY_CODE_PTN.matcher(addr);
    if (match.find()) {
      if (cityCodes != null) data.strCity = convertCodes(match.group(1), cityCodes);
      addr = match.replaceAll(" ").trim();
    }
    return addr;
  }
  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b");
  private static final Pattern CITY_CODE_PTN = Pattern.compile(" *: *(FARM|UNVL)\\b *");
  
  private static final String[] CITY_LIST = new String[]{
    "BURLINGTON",
    "BRANFORD",
    "BRISTOL",
    "CANTON",
    "EAST HAVEN",
    "FARMINGTON",
    "GUILFORD",
    "HAMDEN",
    "NORTH BRANFORD",
    "NORTH HAVEN",
    "UNIONVILLE",
    "WALLINGFORD",
    "WEST HARTFORD",
    "WLFD"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
     "FARM", "FARMINGTON",
     "UNVL", "UNIONVILLE",
     "WLFD", "WALLINGFORD"
  });

}
