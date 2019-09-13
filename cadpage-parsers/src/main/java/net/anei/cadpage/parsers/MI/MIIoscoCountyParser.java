package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

/**
 * Iosco County, MI
 */
public class MIIoscoCountyParser extends DispatchB3Parser {
  
  private static final String MARKER = "IOSCO_COUNTY_911:";
  private static final Pattern SUBJECT_UNIT_PTN = Pattern.compile("(.*?)(?:-\\W*(\\d+)[^ \\w]*)?(?:   +([^ >].*))?");
  private static final Pattern BODY_PHONE_PTN = Pattern.compile(MARKER + "(\\d{10}) +(.*?)( Cad: .*)");
  
  public MIIoscoCountyParser() {
    super(MARKER, CITY_LIST, "IOSCO COUNTY", "MI", B2_OPT_CALL_CODE);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "IOSCO_COUNTY_911@iosco911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_UNIT_PTN.matcher(subject);
    if (match.matches()) {
      subject = match.group(1).trim();
      String unit = match.group(2);
      if (unit != null) data.strUnit = unit;
      String addr = match.group(3);
      if (addr != null) {
        match = BODY_PHONE_PTN.matcher(body);
        if (!match.matches()) return false;
        body = MARKER + addr + ' ' + match.group(2) + ' ' + match.group(1) + match.group(3);
      }
    }
    return super.parseMsg(subject, body, data);
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "6690 N F 41",                          "+44.475743,-83.388967",
      "3191 N M 65",                          "+44.369041,-83.805047",
      "5155 N US 23",                        "+44.430301,-83.330577",
      "5226 N US 23",                        "+44.431831,-83.329657",
      "5665 N US 23",                        "+44.444239,-83.330572",
      "5939 N US 23",                        "+44.452649,-83.329287",
      "3140 S M 65",                          "+44.186485,-83.804062",
      "3999 S M 65",                          "+44.162127,-83.805766",
      "1504 S US 23",                        "+44.422680,-83.330044",
      "1970 S US 23",                        "+44.422784,-83.329937",
      "2344 S US 23",                        "+44.422836,-83.330001"
  });

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "EAST TAWAS",
    "TAWAS CITY",
    "WHITTEMORE",

    // Unincorporated
    "AU SABLE",
    "HALE",
    "LONG LAKE",
    "NATIONAL CITY",
    "OSCODA",

    // Townships
    "ALABASTER TWP",
    "AU SABLE CHARTER TWP",
    "BALDWIN TWP",
    "BURLEIGH TWP",
    "GRANT TWP",
    "OSCODA CHARTER TWP",
    "PLAINFIELD TWP",
    "RENO TWP",
    "SHERMAN TWP",
    "TAWAS TWP",
    "WILBER TWP"
  };
}
