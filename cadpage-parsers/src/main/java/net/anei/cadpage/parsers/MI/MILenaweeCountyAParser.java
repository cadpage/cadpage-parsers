package net.anei.cadpage.parsers.MI;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MILenaweeCountyAParser extends FieldProgramParser {

  private static final Pattern MASTER_PTN1 = Pattern.compile("([-/A-Z0-9 ]+) (\\d\\d:\\d\\d) +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CROSS_DELIM_PTN = Pattern.compile(" *[,/ ] |$ *");
  private static final Pattern MBLANK_PTN = Pattern.compile("  +");
  
  public MILenaweeCountyAParser() {
    super(CITY_LIST, "LENAWEE COUNTY", "MI",
          "CALL_ADDR! Common_Name:INFO");
    setupMultiWordStreets(
        "CLINTON MACON",
        "POSEY LAKE"
    );
    addCrossStreetNames("FULTON COUNTY FAIRGROUNDS");
    setAllowDirectionHwyNames();
  }
  
  @Override
  public String getFilter() {
    return "Lenawee@Lenawee.mi.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    
    // There seem to be a number of different formats.  Try the first one
    Matcher match = MASTER_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("CALL TIME ADDR X CITY INFO");
      
      data.strCall = match.group(1).trim();
      data.strTime = match.group(2);
      body = match.group(3);
      
      // Beyond this things get difficult.  Page starts with a address that usually 
      // does not include a street suffix, followed by cross streets, usually with 
      // the proper street suffix, followed by a city name.  Sheesh!!
      parseAddress(StartType.START_ADDR, FLAG_NO_STREET_SFX | FLAG_CROSS_FOLLOWS | FLAG_NO_CITY, body, data);
      body = getLeft();
      
      // OK, it gets better, sometimes there are multiple cross streets separated by comma or
      // slash separators.  We will handle each street name separately
      String lastDelim = "";
      while (body.length() > 0 && !MBLANK_PTN.matcher(lastDelim).matches()) {
        match = CROSS_DELIM_PTN.matcher(body);
        if (! match.find()) break;  // Can't possibly happen, but we will check anyway
        String term = body.substring(0,match.start());
        if (!isValidCrossStreet(term)) break;
        data.strCross = append(data.strCross, lastDelim, term);
        body = body.substring(match.end()).trim();
        lastDelim = match.group();
      }
      
      // Now look for a city in what is left
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, body, data);
      
      // Check for a last remaining cross street in front of the city
      String pad = cleanInfo(getStart());
      String left = cleanInfo(getLeft());
      if (isValidCrossStreet(pad)) {
        data.strCross = append(data.strCross, lastDelim, pad);
        data.strSupp = left;
      } else {
        data.strSupp = append(pad, " / ", left);
      }
      if (data.strCross.length() == 0) return false;
    } else {
      if (!super.parseMsg(body, data)) return false;
      if (data.strCity.length() == 0) return false;
    }
    
    if (data.strCross.length() == 0) return false;
    if (data.strCity.equalsIgnoreCase("OUT OF COUNTY")) {
      data.strCity = data.defCity = "";
    }
    return true;
  }
  
  private String cleanInfo(String info) {
    int pt = info.indexOf("E911 Info");
    if (pt >= 0)  info = info.substring(0,pt).trim();
    return info;
  }
  
  private class CallAddressField extends AddressField {
    @Override 
    public void parse(String field, Data data) {
      StartType st = StartType.START_CALL;
      int flags = FLAG_START_FLD_REQ;
      int pt = field.indexOf("  ");
      if (pt >= 0) {
        st = StartType.START_ADDR;
        flags = 0;
        data.strCall = field.substring(0,pt);
        field = field.substring(pt+2).trim();
      }
      field = setGPSLoc(field, data);
      
      parseAddress(st, FLAG_PAD_FIELD | FLAG_CROSS_FOLLOWS | FLAG_ANCHOR_END | flags, field, data);
      data.strCross = getPadField();
    }
    
    @Override
    public String getFieldNames() {
      return "CALL ADDR APT X CITY GPS";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_ADDR")) return new CallAddressField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "ADRIAN",
    "HUDSON",
    "MORENCI",
    "TECUMSEH",

    // Villages
    "ADDISON",
    "BLISSFIELD",
    "BRITTON",
    "CEMENT CITY",
    "CLAYTON",
    "CLINTON",
    "DEERFIELD",
    "ONSTED",

    // Unincorporated
    "CANANDAIGUA",
    "FAIRFIELD",
    "JASPER",
    "MADISON CENTER",
    "MEDINA",
    "MANITOU BEACH-DEVILS LAKE",
    "NORTH MORENCI",
    "PALMYRA",
    "SAND CREEK",
    "TIPTON",
    "WESTON",

    // Townships
    "ADRIAN CHARTER TWP",
    "BLISSFIELD TWP",
    "CAMBRIDGE TWP",
    "CLINTON TWP",
    "DEERFIELD TWP",
    "DOVER TWP",
    "FAIRFIELD TWP",
    "FRANKLIN TWP",
    "HUDSON TWP",
    "MACON TWP",
    "MADISON CHARTER TWP",
    "MEDINA TWP",
    "OGDEN TWP",
    "PALMYRA TWP",
    "RAISIN CHARTER TWP",
    "RIDGEWAY TWP",
    "RIGA TWP",
    "ROLLIN TWP",
    "ROME TWP",
    "SENECA TWP",
    "TECUMSEH TWP",
    "WOODSTOCK TWP",
    
    // Other
    "OUT OF COUNTY"
  };
}
