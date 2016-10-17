package net.anei.cadpage.parsers.NY;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYRocklandCountyBParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("([^ ]+) +- +(.*?) +CROSS: *(.*?) +\\d\\d:\\d\\d \\d\\d.*");
  private static final Pattern ADDR_PTN = Pattern.compile("(.*?)   (?:(\\d+) - )?+(.*?)");
  
  public NYRocklandCountyBParser() {
    super(CITY_CODES, "ROCKLAND COUNTY","NY");
    setFieldList("UNIT CALL ID PLACE ADDR APT CITY X");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "44_Control@verizon.net,paging@44-control.net,9300";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1);
    String sAddr = match.group(2).trim();
    data.strCross = match.group(3).trim();
    if (data.strCross.equals("NO CROSS STREETS FOUND")) data.strCross = "";
    
    match = ADDR_PTN.matcher(sAddr);
    if (match.matches()) {
      data.strCall = match.group(1);
      data.strCallId = getOptGroup(match.group(2));
      sAddr = match.group(3);
      
      Result res = parseAddress(StartType.START_PLACE, FLAG_AT_SIGN_ONLY | FLAG_ANCHOR_END, sAddr);
      if (!res.isValid()) {
        data.strAddress = sAddr;
      } else {
        res.getData(data);
      }
    }
    
    else {
      parseAddress(StartType.START_CALL, FLAG_AT_SIGN_ONLY | FLAG_ANCHOR_END, sAddr, data);
    }
    return true;
  }
  
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    
    // For now, they only have one set of coordinates for the Thruway on-ramp
    if (address.startsWith("TWAY ")) return "TWAY";
    return null;
  }


  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "TWAY", "41.154582,-74.188689"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CAN", "Nanuet",
      "CBA", "Bardonia",
      "CCN", "Central Nyack",
      "CCO", "Congers",
      "CNC", "New City",
      "CPR", "Pearl River",
      "CRL", "Rockland Lake",
      "CSV", "Spring Valley",
      "CUN", "Central Nyack",
      "CVC", "Valley Cottage",
      "CWN", "West Nyack",
      "HBM", "Bear Mountain",
      "HGA", "Garnerville",
      "HHA", "Haverstraw",
      "HPO", "Pomona",
      "HST", "Stony Point",
      "HTH", "Thiells",
      "HWH", "West Haverstraw",
      "OBL", "Blauvelt",
      "OGR", "Grandview-on-Hudson",
      "ONA", "Nanuet",
      "ONY", "Nyack",
      "OOR", "Orangeburg",
      "OPA", "Palisades",
      "OPI", "Piermont",
      "OPR", "Pearl River",
      "OSN", "South Nyack",
      "OSP", "Sparkill",
      "OTP", "Tappan",
      "OUG", "Upper Grandview",
      "OWM", "West Nyack",
      "RAR", "Airmont",
      "RCR", "Chestnut Ridge",
      "RHC", "Hillcrest",
      "RHI", "Hillburn",
      "RKA", "Kaser",
      "RLA", "Ladentown",
      "RMO", "Montebello",
      "RMS", "Monsey",
      "RNC", "New City",
      "RNH", "New Hempstead",
      "RNS", "New Square",
      "RPO", "Pomona",
      "RRA", "Ramapo",
      "RSG", "Sloatsburg",
      "RSL", "Sloatsburg",
      "RSP", "Suffern",
      "RSU", "Suffern",
      "RSV", "Spring Valley",
      "RTA", "Tallman",
      "RVI", "Viola",
      "RWE", "Wesley Hills",
      "SBM", "Bear Mountain",
      "SST", "Stony Point",
      "STC", "Tomkins Cove"

  });
}
