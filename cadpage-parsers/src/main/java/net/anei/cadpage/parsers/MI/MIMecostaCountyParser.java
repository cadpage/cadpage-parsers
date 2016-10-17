package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


/**
 * Mecosta County, MI
 */
public class MIMecostaCountyParser extends DispatchA3Parser {
  
  private static final String MARKER = "Meceola Dispatch:";
  private static final Pattern ADDR_L_CITY_PTN = Pattern.compile("([A-Z0-9 ]+)/L +CITY +", Pattern.CASE_INSENSITIVE);
  private static final Pattern NAME_COUNTY_PTN = Pattern.compile("(.*?)[ /]*\\b([^/ ]+ CO)(?:UNTY)?(?: DISPATCH)?", Pattern.CASE_INSENSITIVE);
  
  private static final String UNIT_SUBPTN = "(?:\\d{3,4}|\\d*[A-Z]+\\d+|\\d+[A-Z]+|[BCEFHLMTW][RF]|BR[RF]P?|CT[RF]|LT[RF]|M[AO][RF]|RC[RF]|MOTF|BR[FT][RF]|MARE|POSSE|TEST|70)";
  private static final Pattern NAME_UNIT_PTN = Pattern.compile("(?:(?!MR )|([^:]+?) )("+UNIT_SUBPTN+"(?:,"+UNIT_SUBPTN+")*(?: OR)?)(?: |$)(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLEAN_CROSS_PTN = Pattern.compile("[-/ ]*(.*?)[-/ ]*");
  
// This was a failed attempt to come up with a Name/Unit expression that was not quite a rigid as the above.  It failed to properly identify distinguish "DIKE,BEV" as a non-unit.  
//  private static final Pattern NAME_UNIT_PTN = Pattern.compile("(?:(?!MR )|([^:]+?) )((?:[A-Z0-9]+,[A-Z0-9]+,[A-Z0-9,]+[A-Z0-9]|(?:[A-Z0-9]{1,4}|POSSE|[A-Z]+\\d+),(?:[A-Z0-9]{1,4}|POSSE|[A-Z]+\\d+)|"+UNIT_SUBPTN+")(?: OR)?)(?: |$)(.*)", Pattern.CASE_INSENSITIVE);

  
  private Field infoField;
  
  public MIMecostaCountyParser() {
    this("MECOSTA COUNTY", "MI");
  }
  
  public MIMecostaCountyParser(String defCity, String defState) {
    super(MARKER, CITY_LIST, defCity, defState, "");
    infoField = getField("INFO");
    setFieldList("ADDR APT CITY CALL NAME UNIT " + infoField.getFieldNames());
    setupSaintNames("CLAIR", "ONGE");
  }
  
  @Override
  public String getFilter() {
    return "Meceola Dispatch@MCD911.org";
  }

  @Override
  public String getAliasCode() {
    return "MIMceolaCounty";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Check the alert marker
    if (!body.startsWith(MARKER)) return false;
    body = body.substring(MARKER.length()).trim();
    
    // First problem.  We have to replace double slashes with a single slash
    // in the address, but not in the info section where a double slash marks
    // the end of a cross street.  We don't know for certain where the info
    // section starts, but we will do the best we can by making the replacement
    // only up to the the first cross street marker.
    int pt = body.toUpperCase().indexOf("CROSS STREETS:");
    if (pt < 0) pt = body.length();
    body = body.substring(0,pt).replace("//","/") + body.substring(pt);
    
    // Check for special address construct
    Matcher match = ADDR_L_CITY_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strAddress = match.group(1).trim();
      body = body.substring(match.end());
    }
    
    // Otherwise, parse address from start of message.  We absolutely count
    // on a city field terminating the address.  But if we do not find one
    // on the first pass, try again looking for a city in isolation from the
    // surrounding text
    else {
      parseAddress(StartType.START_ADDR, body, data);
      if (data.strCity.length() == 0) {
        data.strAddress = data.strApt = "";
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, body, data);
        if (data.strCity.length() == 0) return false;
        parseAddress(getStart(), data);
      }
      body = getLeft();

      // Make any necessary city adjustments
      data.strCity = stripFieldEnd(data.strCity, " VLG");
      if (data.strCity.equals("CITY")) data.strCity = "";
    }
    
    // Next identify the call code/description.  Usually one word, unless
    // it is in our list of multi word call descriptions
    String call = MWORD_CALL_LIST.getCode(body, true);
    if (call != null) {
      data.strCall = call;
      body = body.substring(call.length()).trim();
    } else {
      pt = body.indexOf(' ');
      if (pt < 0) return false;
      data.strCall = body.substring(0,pt);
      body = body.substring(pt+1).trim();
    }
    
    //   Next are name and unit fields
    match = NAME_UNIT_PTN.matcher(body);
    if (!match.matches()) return false;
    String name = stripFieldEnd(getOptGroup(match.group(1)), "-");
    data.strUnit = match.group(2);
    body = match.group(3).trim();
    
    // See if the name includes a county
    match = NAME_COUNTY_PTN.matcher(name);
    if (match.matches()) {
      name = match.group(1);
      String city = match.group(2).toUpperCase() + "UNTY";
      if (!data.strCity.equals(city)) {
        data.strCity = append(data.strCity, ", ", city);
      }
    }
    data.strName = cleanWirelessCarrier(name);
    
    // Check for some things that can not possibly be units
    if (data.strUnit.startsWith("pos") || data.strUnit.startsWith("Line")) return false;
    
    // That was the hard part
    // We use the superclass field processing logic to handle the info section
    infoField.parse(body, data);
    
    // Clean up cross street info
    match = CLEAN_CROSS_PTN.matcher(data.strCross);
    if (match.matches()) data.strCross = match.group(1);
    
    return true;
  }
  
  private static final CodeSet MWORD_CALL_LIST = new CodeSet(
      "ALARM F",
      "ALARM G",
      "ALARM M",
      "ALARM M MEDICAL ALERT",
      "ASSIST D",
      "ASSIST M",
      "FIRE GRASS",
      "FIRE STRUCT",
      "FIRE VEHICLE",
      "HOT SHEET"
  );
  
  private static final String[] CITY_LIST = new String[]{

    // Mecosta County
    // Cities
    "BIG RAPIDS",

    // Villages
    "BARRYTON VLG",
    "MECOSTA VLG",
    "MORLEY VLG",
    "STANWOOD VLG",

    //Unincorporated communities
    "ALTONA",
    "CANADIAN LAKES",

    // Townships
    "AETNA TWP",
    "AUSTIN TWP",
    "BIG RAPIDS TWP",
    "CHIPPEWA TWP",
    "COLFAX TWP",
    "DEERFIELD TWP",
    "FORK TWP",
    "GRANT TWP",
    "GREEN TWP",
    "HINTON TWP",
    "MARTINY TWP",
    "MECOSTA TWP",
    "MILLBROOK TWP",
    "MORTON TWP",
    "SHERIDAN TWP",
    "WHEATLAND TWP",
    
    // Osceola County
    // Cities
    "EVART CITY",
    "REED CITY",

    // Villages
    "HERSEY VLG",
    "LEROY VLG",
    "MARION VLG",
    "TUSTIN VLG",

    // Unincorporated community
    "SEARS",

    // Townships
    "BURDELL TWP",
    "CEDAR TWP",
    "EVART TWP",
    "HARTWICK TWP",
    "HERSEY TWP",
    "HIGHLAND TWP",
    "LEROY TWP",
    "LINCOLN TWP",
    "MARION TWP",
    "MIDDLE BRANCH TWP",
    "ORIENT TWP",
    "OSCEOLA TWP",
    "RICHMOND TWP",
    "ROSE LAKE TWP",
    "SHERMAN TWP",
    "SYLVAN TWP",
    
    "CITY",
    "CLARE COUNTY",
    "NEWAYGO COUNTY",
       "NORWICH",
    "LAKE COUNTY",
       "DOVER TWP",
    "WEXFORD COUNTY"
  };
}
