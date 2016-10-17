package net.anei.cadpage.parsers.WV;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVWoodCountyParser extends SmartAddressParser {
  
  public WVWoodCountyParser() {
    super(CITY_LIST, "WOOD COUNTY", "WV");
    setFieldList("CODE CALL ADDR APT PLACE CITY ST X");
    addRoadSuffixTerms("HL", "RDG");
  }
  
  @Override
  public String getFilter() {
    return "ctc@Woodcounty911.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:(\\d\\d-\\d\\d) )?(.*)(?:\n(.*))?");
  private static final Pattern COMMA_PTN = Pattern.compile(" *[,;]+ *");
  private static final Pattern SLASH_PTN = Pattern.compile("(?!<\\d)/|/(?!\\d)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Wood County CTC Notification")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = getOptGroup(match.group(1));
    data.strCall = match.group(2).trim();
    
    // Break the main address part by comma/semicolons
    String[] parts = COMMA_PTN.split(getOptGroup(match.group(3)));
    
    // First part is parsed as an address
    // A slash complicates things.  We have to parser each section individually
    // then put them back together as either parts of an intersection, or as
    // a full address followed by a cross street
    String addr = parts[0];
    String firstAddress = "";
    boolean goodAddress = false;
    match = SLASH_PTN.matcher(addr);
    if (match.find()) {
      parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_CROSS_FOLLOWS, addr.substring(0,match.start()).trim(), data);
      goodAddress = getStatus() > STATUS_STREET_NAME;
      firstAddress = data.strAddress;
      parseExtra(getLeft(), data);
      data.strAddress = "";
      addr = addr.substring(match.end()).trim();
    }
    
    parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS, addr, data);
    if (goodAddress) {
      data.strCross = append(data.strCross, "/", data.strAddress);
      data.strAddress = firstAddress;
    } else {
      data.strAddress = append(firstAddress, " & ", data.strAddress);
    }
    parseExtra(getLeft(), data);

    // OK, no on to the remaining comma/semicolon delimited sections.
    for (int ndx = 1; ndx<parts.length; ndx++) {
      String part = parts[ndx];
      
      // Check for standard city name
      if (data.strCity.length() == 0 && isCity(part)) {
        data.strCity = part;
        continue;
      }
      
      // Check for county
      match = COUNTY_PTN.matcher(part);
      if (match.matches()) {
        parseCountyMatch(match, data);
        continue;
      }
      
      // Or State
      if (part.equals("WV") || part.equals("OH")) {
        data.strState = part;
        continue;
      }
      
      // If part contains a slash or is a valid address, turn it into a cross street
      if (part.indexOf('/') >= 0 || isValidAddress(part)) {
        part = stripFieldStart(part, "/");
        part = stripFieldEnd(part, "/");
        String sep = (data.strCross.indexOf('/')>=0 || part.indexOf('/')>=0 ? "," : "/");
        data.strCross = append(data.strCross, sep, part);
        continue;
      }
      
      // Otherwise treat it as a place name
      data.strPlace = append(data.strPlace, " - ", part);
    }
    return true;
  }

  private void parseExtra(String extra, Data data) {
    Matcher match = COUNTY_PTN.matcher(extra);
    if (match.lookingAt()) {
      if (parseCountyMatch(match, data)) {
        extra = extra.substring(match.end());
      }
    }
    if (extra.startsWith("#")) {
      Parser p = new Parser(extra.substring(2).trim());
      String apt = p.get(' ');
      if (apt.equals("APT")) {
        apt = p.get(' ');
      } else if (apt.equals("LOT")) {
        apt = append(apt, " ", p.get(' '));
      }
      data.strApt = append(data.strApt, "-", apt);
      extra = p.get();
    }
    
    if (isValidAddress(extra)) {
      data.strCross = append(data.strCross, "/", extra);
    } else {
      data.strPlace = append(data.strPlace, " - ", extra);
    }
  }

  private static final Pattern COUNTY_PTN = Pattern.compile("(ATHENS|JACKSON|MEIGS|RITCHIE|PLEASANTS|WASHINGTON|WIRT|WOOD) (OH|WV|CO(?:UNTY)?)\\b *");

  public boolean parseCountyMatch(Matcher match, Data data) {
    String county = match.group(1);
    String state = match.group(2);
    if (data.strCity.length() == 0) data.strCity = county + " COUNTY";
    if (!state.startsWith("CO")) {
      data.strState = state;
    } else if (OHIO_COUNTIES.contains(county)) {
      data.strState = "OH";
    }
    
    return (!state.equals("COUNTY") || match.hitEnd());
  }
  
  private static final Set<String> OHIO_COUNTIES = new HashSet<String>(Arrays.asList(new String[]{
      "ATHENS",
      "MEIGS",
      "WASHINGTON"
  }));
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "PARKERSBURG",
    "VIENNA",
    "WILLIAMSTOWN",

    // Town
    "NORTH HILLS",

    // Census-designated places
    "BLENNERHASSETT",
    "BOAZ",
    "LUBECK",
    "MINERALWELLS",
    "MINERAL WELLS",
    "WASHINGTON",
    "WAVERLY",

    // Unincorporated communities
    "BELLEVILLE",
    "BONNIVALE",
    "CEDAR GROVE",
    "CENTRAL",
    "DALLISON",
    "DAVISVILLE",
    "DEERWALK",
    "FORT NEAL",
    "KANAWHA",
    "NEW ENGLAND",
    "OGDEN",
    "PETTYVILLE",
    "ROCKPORT",
    "SLATE",
    "SPRING VALLEY",
    "VOLCANO",
    "WALKER",
    
    // Wirt County
    
    //  Cities
    "ELIZABETH",
    
    // Unincorporated communities
    "BEAVERDAM",
    "BEULAH HILL",
    "BROHARD",
    "BURNING SPRINGS",
    "CHERRY",
    "CRESTON",
    "ENTERPRISE",
    "FREEPORT",
    "GARFIELD",
    "GREENCASTLE",
    "HILBERT",
    "IVAN",
    "LUCILE",
    "MCCLAIN",
    "MORRISTOWN",
    "MUNDAY",
    "NEWARK",
    "PALESTINE",
    "PEEWEE",
    "ROVER",
    "SANOMA",
    "STANDING STONE",
    "TWO RUN",
    "WINDY",
    "ZACKVILLE",
    
    // Jackson County
    "LEROY"
  };
}
