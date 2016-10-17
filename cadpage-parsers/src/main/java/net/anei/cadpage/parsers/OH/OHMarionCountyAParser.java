package net.anei.cadpage.parsers.OH;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class OHMarionCountyAParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("UNIT +([0-9A-Z ]+)  (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) +(\\d\\d-\\d{6}) +(.*)");
  private static final Pattern CALL_NUMBER_PTN = Pattern.compile(".*(?:(?:SECTION|ZONE|#) \\d+|\\d+ (?:Y/?O/?[AMF]?|FEM|MALE|YEAR|YR|ILL)) ");
  private static final Pattern APT_PTN = Pattern.compile("(.*?) +UNIT +0*([^ ]+) +(.*)");

  public OHMarionCountyAParser() {
    super(CITY_CODES, "MARION COUNTY", "OH");
    setFieldList("UNIT DATE TIME ID CALL ADDR CITY APT X");
  }
  
  @Override
  public String getFilter() {
    return "MCSO@CO.MARION.OH.US";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("CAD Page")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1).trim();
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCallId = match.group(4);
    String addr = match.group(5);
    
    addr = expandAbbreviations(addr);
    int pt = addr.indexOf('@');
    if (pt >= 0) {
      int pte = pt+1;
      int pt2 = addr.lastIndexOf(' ', pt);
      if (pt2 >= 0) {
        String city = CITY_CODES.getProperty(addr.substring(pt2+1, pt));
        if (city != null) {
          data.strCity = city;
          pt = pt2;
        }
      }
      addr = addr.substring(0,pt) + " & " + addr.substring(pte);
    }
    
    // Things get ugly really fast now.
    // The call descriptions are no fixed, so a call code list doesn't help us
    // We will make an attempt to identify numbers that look like part of the
    // call description so they will not get mistaken for house numbers.
    String callPrefix = null;
    match = CALL_NUMBER_PTN.matcher(addr);
    if (match.lookingAt()) {
      int brk = match.end();
      callPrefix = addr.substring(0, brk);
      addr = addr.substring(brk);
    }
    
    // An apt field is a distinctive marker between the address and cross street fields
    // if we are lucky enough to have one, use it.
    String cross;
    match = APT_PTN.matcher(addr);
    if (match.matches()) {
      parseAddress(StartType.START_CALL, FLAG_PREF_TRAILING_BOUND | FLAG_ANCHOR_END, match.group(1), data);
      data.strApt = append(data.strApt, "-", match.group(2));
      cross = match.group(3);
    }
    
    // No such luck, do what we can with the address parser
    else {
      parseAddress(StartType.START_CALL, FLAG_PREF_TRAILING_BOUND | FLAG_CROSS_FOLLOWS, addr, data); 
      cross = getLeft();
    }
    
    // Append call prefix if found
    if (callPrefix != null) data.strCall = (callPrefix + data.strCall).trim();

    // They usually have city codes separating the different cross streets, so we will try
    // that first.
    if (cross.length() > 0) {
      String city = data.strCity;
      do {
        data.strCity = "";
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, cross, data);
        if (city.length() == 0) city = data.strCity;
        data.strCross = append(data.strCross, " / ", getStart());
        cross = getLeft();
      } while (cross.length() > 0);
      data.strCity = city;
      
      // If we did not find street break, see if the SAP will find it
      if (!data.strCross.contains(" / ")) {
        cross = data.strCross;
        data.strCross = "";
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_NO_CITY | FLAG_IMPLIED_INTERSECT | FLAG_PREF_TRAILING_BOUND | FLAG_ANCHOR_END, cross, data);
      }
    }

    return true;
  }
  
  private String expandAbbreviations(String address) {
    for (ExpandElement exp : EXPAND_TABLE) {
      address = exp.expand(address);
    }
    return address;
  }
  
  private static ExpandElement[] EXPAND_TABLE;
  
  
  private static class ExpandElement {
    
    private Pattern pattern;
    private String replacement;
    
    public ExpandElement(String pattern, String replacement) {
      this.pattern = Pattern.compile("\\b" + pattern + "\\b");
      this.replacement = replacement;
    }
    
    public String expand(String text) {
      return pattern.matcher(text).replaceAll(replacement);
    }
  }
  
  static private void setupExpandTable(String ... args) {
    List<ExpandElement> expandList = new ArrayList<ExpandElement>();
    for (int ndx = 0; ndx < args.length; ndx += 2) {
      expandList.add(new ExpandElement(args[ndx], args[ndx+1]));
    }
    EXPAND_TABLE = expandList.toArray(new ExpandElement[expandList.size()]);
  }
  
  static {
    setupExpandTable(
        "CAL MUD PIKE",           "CALEDONIA MUD PIKE",
        "CEN GREEN CAM",          "CENTERVILLE GREEN CAMP RD",
        "CEN GRN CAM",            "CENTERVILLE GREEN CAMP RD",
        "CEN NEWMANS",            "CENTERVILLE NEWMAN RD",
        "LARUEGRN CAM",           "LARUE GREEN CAMP RD",
        "MRN WMPORT",             "MARION-WILLIAMSPORT RD",
        "POLE LANE",              "POLE LANE RD",
        "PATTEN RILEY",           "PATTEN RILEY RD",
        "POL LN",                 "POLE LANE RD",
        "WHETS RIV",              "WHETSTONE RIVER RD"
//    "EAST WOOD VALL"          No idea what this is :(
    );
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CL",  "CALEDONIA",
      "GAL", "GALION",
      "MO",  "MONTGOMERY TWP",
      "PL",  "PLEASANT TWP",
      "PP",  "PP"
  });
}
