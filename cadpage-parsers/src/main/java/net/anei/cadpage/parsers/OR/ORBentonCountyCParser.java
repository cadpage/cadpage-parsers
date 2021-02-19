package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Benton County, OR fallback parser
 */
public class ORBentonCountyCParser extends ORBentonCountyBaseParser {

  public ORBentonCountyCParser() {
    this("BENTON COUNTY");
  }

  public ORBentonCountyCParser(String defCity) {
    super(defCity, null);
  }

  public String getAliasCode() {
    return "ORBentonCountyC";
  }

  @Override
  public String getFilter() {
    return "Corvallis Alerts,alerts@corvallis.ealertgov.com";
  }

  private static final Pattern MP_PTN = Pattern.compile("[/ ]*(?:MP|Milepost) (\\d+)\\b *", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!isPositiveId()) return false;
    setFieldList("CALL ADDR APT CITY INFO");

    data.defCity = "";

    if (body.startsWith(subject)) subject = "";
    if (subject.length() > 0) data.strCall = subject;
    for (String line : body.split("\n")) {
      if (data.strAddress.length() == 0) {
        Result res = parseAddress(StartType.START_OTHER, line);
        if (res.isValid()) {
          res.getData(data);
          String prefix = res.getStart();
          if (data.strCall.length() == 0) data.strCall = prefix;
          else data.strSupp = append(data.strSupp, "\n", prefix);
          line = res.getLeft();
          Matcher match = MP_PTN.matcher(line);
          if (match.lookingAt()) {
            data.strAddress = data.strAddress + " MP " + match.group(1);
            line = line.substring(match.end());
          }
          line = stripFieldStart(line, ",");
        }
      }
      if (data.strCall.length() == 0) data.strCall = line;
      else data.strSupp = append(data.strSupp, "\n", line);
    }
    if (data.strAddress.length() == 0) return false;

    fixAddress(data);
    return true;
  }
}
