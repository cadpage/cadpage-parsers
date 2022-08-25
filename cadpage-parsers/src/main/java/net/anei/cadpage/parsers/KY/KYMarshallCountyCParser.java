package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class KYMarshallCountyCParser extends DispatchA48Parser {
  
  public KYMarshallCountyCParser() {
    super(CITY_LIST, "MARSHALL COUNTY", "KY", FieldType.PLACE_PHONE_NAME, A48_NO_CODE);
  }
  
  @Override
  public String getFilter() {
    return "@MarshallCountyKY.gov";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*) [AP]M");
  private static final Pattern INFO_DATE_PTN = Pattern.compile("(\n\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d) [AP]M\\b");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf(':');
    if (pt >= 0 && pt < body.indexOf('\n')) body = body.substring(pt+1).trim();
    
    // They have misconfigured the time with a spuruous AM/PM indicator
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      subject = match.group(1).trim();
      body = INFO_DATE_PTN.matcher(body).replaceAll("$1");
    }
    
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BENTON",
      "CALVERT CITY",
      "HARDIN",

      // Census-designated place
      "GILBERTSVILLE",

      // Other unincorporated communities
      "AURORA",
      "BIG BEAR AREA",
      "BREWERS",
      "BRIENSBURG",
      "DRAFFENVILLE",
      "FAIRDEALING",
      "HARVEY",
      "MOORS CAMP AREA",
      "OAK LEVEL",
      "OLIVE",
      "PALMA",
      "POSSUM TROT",
      "SHARPE",
      "TATUMSVILLE"
  };

}
