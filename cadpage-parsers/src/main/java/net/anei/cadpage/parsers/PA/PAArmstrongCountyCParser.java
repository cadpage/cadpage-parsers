package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAArmstrongCountyCParser extends MsgParser {

  public PAArmstrongCountyCParser() {
    super("ARMSTRONG COUNTY", "PA");
    setFieldList("CALL UNIT ADDR APT CITY X URL PLACE DATE TIME ID CH");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Dispatch")) {
      String[] parts = body.split("\n");
      if (parts.length != 6) return false;
      parseLine1(parts[0].trim(), data);
      parseLine2(parts[1].trim(), data);
      parseLine3(parts[2].trim(), data);
      parseLine4(parts[3].trim(), data);
      if (!parseLine5(parts[4].trim(), data)) return false;
      if (!parseLine6(parts[5].trim(), data)) return false;
      return true;
    }

    return parseMaster(body, data);
  }

  private static final Pattern LINE1_PTN = Pattern.compile("(.*?) +(E\\d+)");
  private void parseLine1(String field, Data data) {
    field = stripFieldStart(field, "/");
    field = stripFieldEnd(field, "/");
    Matcher match = LINE1_PTN.matcher(field);
    if (match.matches()) {
      field = match.group(1);
      data.strUnit = match.group(2);
    }
    data.strCall = field;
  }

  private static final Pattern LINE2_PTN = Pattern.compile("(.*?) +(EMS\\d+|FT\\d+|CW\\d+)", Pattern.CASE_INSENSITIVE);
  private void parseLine2(String field, Data data) {
    if (!data.strUnit.isEmpty()) field = stripFieldStart(field, data.strUnit+' ');
    Matcher match = LINE2_PTN.matcher(field);
    if (match.matches()) {
      field = match.group(1);
      data.strUnit = append(data.strUnit, ",", match.group(2));
    }
    data.strCall = append(data.strCall, " / ", field);
  }

  private void parseLine3(String field, Data data) {
    Parser p = new Parser(field);
    String apt = p.getLastOptional(" APT ");
    String cross = p.getLastOptional(";");
    if (cross.startsWith("http:") || cross.startsWith("https:")) {
      data.strInfoURL = cross;
    } else {
      data.strCross = cross;
    }

    data.strCity = stripFieldEnd(p.getLastOptional(","), " BORO").replace("KITTG", "KITTANNING");
    parseAddress(p.get(), data);
    data.strApt = append(data.strApt, "-", apt);
  }

  private void parseLine4(String field, Data data) {
    data.strPlace = stripFieldEnd(field, "/");
  }

  private static final Pattern LINE5_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) (CFS\\d+)");
  private boolean parseLine5(String field, Data data) {
    Matcher match = LINE5_PTN.matcher(field);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strCallId = match.group(3);
    return true;
  }

  private static final Pattern LINE6_PTN = Pattern.compile("FIRE (.*) /EMS *\\b(.*)");
  private boolean parseLine6(String field, Data data) {
    Matcher match = LINE6_PTN.matcher(field);
    if (!match.matches()) return false;
    data.strChannel = append(match.group(1), " / ", match.group(2));
    return true;
  }

  private static final Pattern MASTER =
      Pattern.compile("Dispatch:(.*?)  / (.* (?:FT\\d+|EMS\\d+|CW\\d+)) (.*? (?:APT \\S+|https?:\\S+)?) /(.*?) (\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d CFS\\d+) (.*)", Pattern.CASE_INSENSITIVE);
  private boolean parseMaster(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseLine1(match.group(1).trim(), data);
    parseLine2(match.group(2).trim(), data);
    parseLine3(match.group(3).trim(), data);
    parseLine4(match.group(4).trim(), data);
    if (!parseLine5(match.group(5).trim(), data)) return false;
    if (!parseLine6(match.group(6).trim(), data)) return false;
    return true;
  }
}
