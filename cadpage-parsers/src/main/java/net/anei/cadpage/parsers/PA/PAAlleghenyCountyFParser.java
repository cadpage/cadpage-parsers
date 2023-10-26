package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class PAAlleghenyCountyFParser extends MsgParser {

  public PAAlleghenyCountyFParser() {
    super("ALLEGHENY COUNTY", "PA");
    setFieldList("CALL ADDR APT CITY ST NAME PHONE INFO URL");
  }

  @Override
  public String getFilter() {
    return "qaesincident@AlleghenyCounty.us";
  }

  public interface Parser {
    public void parse(String field);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith("- Allegheny County Emergency Services")) return false;
    for (String line : body.split("\n")) {
      line = line.trim();
      if (line.endsWith("*** CLOSED ***")) {
        data.msgType = MsgType.RUN_REPORT;
        continue;
      }
      if (parseLine("Problem Type:", line, field -> data.strCall = field)) continue;
      if (parseLine("Problem Sub-Type:", line, field -> data.strCall = append(data.strCall, " - ", field))) continue;
      if (parseLine("Location:", line, field -> parseAddress(field, data))) continue;
      if (parseLine("Sent by:", line, field -> data.strName = field)) continue;
      if (parseLine("Call Back #:", line, field -> data.strPhone = field)) continue;
      if (line.startsWith("***** Click here to Reply")) break;
      line = stripFieldStart(line, "Description:");
      data.strSupp = append(data.strSupp, "\n", line);
    }
    data.strInfoURL = "https://acesqaes.alleghenycounty.us";
    return true;
  }

  private boolean parseLine(String key, String line, Parser parser) {
    if (!line.startsWith(key)) return false;
    parser.parse(line.substring(key.length()).trim());
    return true;
  }

  private static final Pattern TRAIL_ZIP_PTN = Pattern.compile("(.*?)[, ]+\\d{5}");

  @Override
  public void parseAddress(String field, Data data) {
    Matcher match = TRAIL_ZIP_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    if (field.endsWith(" PA")) {
      data.strState = "PA";
      field = field.substring(0, field.length()-3).trim();
    }
    int pt = field.lastIndexOf(',');
    if (pt >= 0) {
      data.strCity = field.substring(pt+1).trim();
      if (data.strCity.equals("PGH")) data.strCity = "PITTSBURGH";
      field = field.substring(0,pt);
    }
    super.parseAddress(field, data);
  }
}
