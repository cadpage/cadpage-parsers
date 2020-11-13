package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Bethlehem, PA
 */
public class PABethlehemParser extends SmartAddressParser {

  public PABethlehemParser() {
    super("BETHLEHEM", "PA");
    setFieldList("DATE TIME SRC PRI CALL ID ADDR APT CITY INFO");
  }

  @Override
  public String getFilter() {
    return "kc-mailer@padoh-kc.org";
  }

  private static final Pattern MASTER_PATTERN
    = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d) [A-Z]{2}T +-- +\\((.*?) - (\\S*?)\\)(.*), ID (\\d+), (?:Exercise)?\n.*?\n(.*?)\n(.*)",
        Pattern.DOTALL);
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("KC Alert")) return false;
    body = cleanInfo(body);
    Matcher m = MASTER_PATTERN.matcher(body);
    if (m.matches()) {
      data.strDate = m.group(1);
      data.strTime = m.group(2);
      data.strSource = m.group(3).trim();
      data.strPriority = m.group(4).trim();
      data.strCall = m.group(5).trim();
      data.strCallId = m.group(6);
      parseLocation(m.group(7).trim(), data);
      data.strSupp = append(data.strSupp, "\n", m.group(8).trim());

      return true;
    }
    return false;
  }

  private static final Pattern LOCATION_PATTERN
    = Pattern.compile("(?i)(?:(?:(.{0,35}), +\\b((?:(?:east|west|north|south) +)?[a-z]{2,15}(?: +[a-z]{4,15})?) +pa\\b(?: +\\d{5})?|(.{0,25}) +(\\d{5}))(.*)|((?:(?:east|west|north|south) +)?[a-z]{5,15}(?: +[a-z]{5,15})?)(?: +pa)?)");
  private void parseLocation(String field, Data data) {
    Matcher m = LOCATION_PATTERN.matcher(field);
    if (m.matches()) {
      parseAddress(getOptGroup(m.group(1)).trim()+getOptGroup(m.group(3)).trim(), data);
      parseCity(getOptGroup(m.group(2))+getOptGroup(m.group(4))+getOptGroup(m.group(6)).trim(), data);
      data.strSupp = getOptGroup(m.group(5)).trim();
      return;
    }
    Result r = parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD|FLAG_NO_IMPLIED_APT, field);
    if (r.getStatus() > STATUS_STREET_NAME) {
      r.getData(data);
      data.strApt = r.getPadField();
      field = r.getLeft().trim();
    }
    data.strSupp = field;
  }

  @Override
  public void parseAddress(String a, Data data) {
    int ndx = a.indexOf(',');
    if (ndx > -1 && ndx < a.length()) {
      data.strApt = a.substring(ndx+1).trim();
      a = a.substring(0, ndx).trim();
    }
    super.parseAddress(a, data);
  }

  private String cleanInfo(String s) {
    int ndx = s.indexOf("<a href=");
    if (ndx > -1) s = s.substring(0, ndx);
    return s;
  }

  private void parseCity(String field, Data data) {
    int ndx = field.indexOf(',');
    if (ndx > -1) {
      data.strApt = field.substring(0, ndx).trim();
      if(ndx+1 == field.length())
        field = "";
      else
      field = field.substring(ndx+1).trim();
    }
    data.strCity = field;
  }
}
