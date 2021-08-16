package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHMedinaCountyDParser extends MsgParser {

  public OHMedinaCountyDParser() {
    super("MEDINA COUNTY", "OH");
    setFieldList("ADDR APT CITY ST PLACE CALL UNIT INFO");
  }

  @Override
  public String getFilter() {
    return "donotreply@medinaco.org";
  }

  private static final Pattern LOG_HEADER_PTN = Pattern.compile(";? +\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log - ");
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("([^,]+)(?:, *([A-Z ]+))??(?:, *([A-Z]{2})(?: +(\\d{5}))?)?");
//  private static final Pattern PLACE_CALL_PTN = Pattern.compile("(?:(.*?) )?(Assist other Agency|Deceased Person DOA Dead|Flooded Roadways|Medical Alarm|Mental|Open Burn Complaint|Suicide/Attempted Suicide/Suicide Threats/Psych/Abnormal Behavior|Traffic Stop|Trees or Branches Down|(?:Fire Alarm|MVC w|\\S+) - .*)");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("; *");
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d+[A-Z]?|[A-Z]|(?:#|APARTMENT|APT|LOT|RM|SUITE|UNIT)[# ]*(.*)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() == 0) return false;
    data.strCall = subject;

    String[] flds = LOG_HEADER_PTN.split(body);
    if (flds.length > 1) {
      body = flds[0].trim();
      for (int jj = 1; jj < flds.length; jj++) {
        data.strSupp = append(data.strSupp, "\n", flds[jj].trim());
      }
    } else {
      int pt = body.lastIndexOf(" None;");
      if (pt < 0) return false;
      data.strSupp = body.substring(pt+6).trim();
      body = body.substring(0,pt).trim();
    }

    int pt = body.indexOf("; ");
    if (pt < 0)  return false;
    String call = body.substring(pt+2).trim();
    body = body.substring(0, pt).trim();

    if (!body.isEmpty()) {
      Matcher match = ADDR_ST_ZIP_PTN.matcher(body);
      if (!match.matches()) return false;
      parseAddress(match.group(1).trim(), data);
      data.strCity = getOptGroup(match.group(2));
      data.strState = getOptGroup(match.group(3));
      String zip = match.group(4);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }

    pt = call.indexOf(';');
    if (pt < 0) pt = call.length();
    pt = call.lastIndexOf(' ', pt-1);
    if (pt < 0) return false;
    data.strUnit = UNIT_DELIM_PTN.matcher(call.substring(pt+1)).replaceAll(",");
    call = call.substring(0,pt).trim();

    // Really hope the call in the subject matches the call in the alert text!!!
    if (!call.endsWith(data.strCall)) return false;

    String place = call.substring(0, call.length() - data.strCall.length()).trim();

    if (place != null && !place.equals("None")) {
      Matcher match = APT_PTN.matcher(place);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = place;
        if (!apt.equals(data.strApt)) data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = place;
      }
    }

    return true;
  }
}
