package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MSHarrisonCountyParser extends DispatchB2Parser {
  
  public MSHarrisonCountyParser() {
    super(CITY_LIST, "HARRISON COUNTY", "MS");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "SWEET CAROLYN", 
        "WHITE STAR"
    );
  }
  
  @Override
  public String getFilter() {
    return "hcsoDispatch@harrisoncountysheriff.com";
  }
  
  private static final Pattern MARKER = Pattern.compile("hcsoDispatch:(\\S+) +HCSO911 +");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strUnit = match.group(1);
    body = body.substring(match.end());
    return super.parseMsg(body, data);
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "FIRE ALARM",
      "MEDICAL EMERGENCY",
      "WOODS FIRE"
 );

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BILOXI",
      "D'IBERVILLE",
      "DIBERVILLE",
      "GULFPORT",
      "LONG BEACH",
      "PASS CHRISTIAN",

      // Census-designated places
      "DELISLE",
      "HENDERSON POINT",
      "LYMAN",
      "SAUCIER",

      // Unincorporated communities
      "CUEVAS",
      "HOWISON",
      "LIZANA"
  };
}
