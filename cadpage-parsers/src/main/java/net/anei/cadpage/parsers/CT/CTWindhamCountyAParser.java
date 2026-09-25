package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CTWindhamCountyAParser extends SmartAddressParser {

  public CTWindhamCountyAParser() {
    this("WINDHAM COUNTY", "CT");
    setFieldList("SRC UNIT CH PRI CALL ADDR CITY ST PLACE APT X DATE TIME");
  }

  CTWindhamCountyAParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "CTWindhamCountyA";
  }

  @Override
  public String getFilter() {
    return "qvecpaging@qvec.org,messaging@iamresponding.com";
  }

  private static final Pattern MASTER =
      Pattern.compile("([-/A-Z0-9,]*) ?\\|(\\S+)\\|(?: PRI (\\d) \\|)? (.+?) \\* ([^\\*,]+)(?:, *([A-Za-z ]*))? \\* +(.*?) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");

  private static final Pattern PLACE_X_PTN = Pattern.compile("(.*[^- ].*)[- ] (.*)");
  private static final Pattern CLEAN_PLACE_PTN = Pattern.compile("[-*/| ]*(.*?)[-*/| ]*");
  private static final Pattern APT_PTN = Pattern.compile("(.*?)[-*/| ]*\\b(?:APT|ROOM|RM|^LOT|UNIT)\\.?[ #]+(.+)", Pattern.CASE_INSENSITIVE);
  private static final Pattern PLACE_APT_X_PTN = Pattern.compile("(.*) *\\b(?:APT|ROOM|RM|^LOT|UNIT)\\.?[ #](\\S+)\\b *(.*)", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // rule out CTNewLondonCountyE alerts
    if (body.startsWith("COMPLAINT TYPE:")) return false;

    if (subject.length() != 0) {
      subject = stripFieldEnd(subject, " Page");
      data.strSource = subject;
    }

    Matcher  match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1);
    data.strChannel = match.group(2);
    data.strPriority = getOptGroup(match.group(3));
    data.strCall =  stripFieldStart(match.group(4), "/");
    parseAddress(match.group(5).trim(), data);
    data.strCity = getOptGroup(match.group(6));
    String cross = match.group(7).trim();
    data.strDate = match.group(8);
    data.strTime = match.group(9);

    String place = null;
    cross = stripFieldStart(cross, "/");
    match = PLACE_X_PTN.matcher(cross);
    if (match.matches()) {
      place = match.group(1).trim();
      cross = match.group(2).trim();
      match = CLEAN_PLACE_PTN.matcher(place);
      if (match.matches()) place = match.group(1);
    }
    else {
      match = CLEAN_PLACE_PTN.matcher(cross);
      if (match.matches()) {
        place = match.group(1);
        if (place.equals(cross)) {
          if (cross.equalsIgnoreCase("Area of")) {
            place = cross;
            cross = "";
          } else {
            place = null;
          }
        } else {
          cross = "";
        }
      }
    }
    if (place != null) {
      match = APT_PTN.matcher(place);
      if (match.matches()) {
        place = match.group(1).trim();
        data.strApt = match.group(2);
      }
      data.strPlace = place;
    }
    else {
      match = PLACE_APT_X_PTN.matcher(cross);
      if (match.matches()) {
        data.strPlace = match.group(1).trim();
        data.strApt = match.group(2);
        cross = match.group(3);
      }
    }
    data.strCross = cross;

    if (data.strCity.equalsIgnoreCase("WEBSTER")) data.strState = "MA";
    return true;
  }
}
