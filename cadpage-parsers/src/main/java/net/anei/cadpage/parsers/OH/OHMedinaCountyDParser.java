package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHMedinaCountyDParser extends MsgParser {

  public OHMedinaCountyDParser() {
    super("MEDINA COUNTY", "OH");
    setFieldList("ADDR APT CITY ST PLACE CALL UNIT INFO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
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
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "MM 202 I 71",                          "+40.990065,-82.030725",
      "MM 203 I 71",                          "+40.996317,-82.013867",
      "MM 204 I 71",                          "+40.997435,-81.994831",
      "MM 205 I 71",                          "+40.998696,-81.975843",
      "MM 206 I 71",                          "+41.004312,-81.958283",
      "MM 207 I 71",                          "+41.010879,-81.941246",
      "MM 208 I 71",                          "+41.017494,-81.924209",
      "MM 209 I 71",                          "+41.023919,-81.907172",
      "MM 210 I 71",                          "+41.030724,-81.890135",
      "MM 211 I 71",                          "+41.040289,-81875858.000000",
      "MM 212 I 71",                          "+41.051616,-81.864103",
      "MM 213 I 71",                          "+41.062989,-81.852349",
      "MM 214 I 71",                          "+41.075220,-81.841927",
      "MM 215 I 71",                          "+41.087546,-81.832028",
      "MM 216 I 71",                          "+41.099919,-81.822035",
      "MM 217 I 71",                          "+41.112244,-81.812041",
      "MM 218 I 71",                          "+41.124618,-81.802047",
      "MM 219 I 71",                          "+41.137705,-81.793814",
      "MM 220 I 71",                          "+41.151410,-81.787961",
      "MM 221 I 71",                          "+41.165782,-81.785449",
      "MM 222 I 71",                          "+41.180011,-81.788056",
      "MM 223 I 71",                          "+41.194431,-81.791006",
      "MM 224 I 71",                          "+41.208803,-81.793433",
      "MM 225 I 71",                          "+41.223222,-81.793814",
      "MM 226 I 71",                          "+41.237356,-81.797954",
      "MM 227 I 71",                          "+41.251348,-81.802761",
      "MM 228 I 71",                          "+41.265720,-81.804902",
      "MM 229 I 71",                          "+41.280091,-81.807187",

      "MM 0 I 76",                            "+41.023919,-81.907172",
      "MM 1 I 76",                            "+41.029725,-81.890991",
      "MM 2 I 76",                            "+41.033056,-81.872574",
      "MM 3 I 76",                            "+41.032865,-81.853491",
      "MM 4 I 76",                            "+41.032294,-81.834360",
      "MM 5 I 76",                            "+41.031961,-81.815277",
      "MM 6 I 76",                            "+41.031723,-81.796194",
      "MM 7 I 76",                            "+41.031438,-81.777015",
      "MM 8 I 76",                            "+41.034341,-81.758741",
      "MM 9 I 76",                            "+41.039147,-81.741276",
      "MM 10 I 76",                           "+41.046619,-81.726380",
      "MM 11 I 76",                           "+41.047095,-81.707345",
      "MM 12 I 76",                           "+41.043050,-81.689832",
      
      "MM 0 I 271",                           "+41.159596,-81.785248",
      "MM 1 I 271",                           "+41.163974,-81.768973",
      "MM 2 I 271",                           "+41.176633,-81.760549",
      "MM 3 I 271",                           "+41.188720,-81.750746",
      "MM 4 I 271",                           "+41.194764,-81.733566",
      "MM 5 I 271",                           "+41.195716,-81.714435",
      "MM 6 I 271",                           "+41.197572,-81.695542"
  });
}
