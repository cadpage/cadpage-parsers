package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ORClackamasCountyDParser extends SmartAddressParser {

  public ORClackamasCountyDParser() {
    super("GRESHAM", "OR");
    setFieldList("CH ID CODE CALL TIME APT ADDR PLACE MAP X UNIT INFO SRC");
  }

  @Override
  public String getFilter() {
    return "@portlandoregon.gov";
  }

  private static final Pattern MASTER
    = Pattern.compile("(OPS\\d R[PG]) #(\\d+) (.*?) (\\d\\d:\\d\\d:\\d\\d) (.*?) (F[A-Z] [A-Z]?\\d{4} \\d{4}[A-Z]) (?:(.*?) )?DISPATCHED: (.*) REMARKS: *(.*?)(?: Sent by: (\\S+))?");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body =  stripFieldEnd(body, " ...");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strChannel = match.group(1);
    data.strCallId = match.group(2);
    parseCodeCall(match.group(3).trim(), data);
    data.strTime = match.group(4);
    parseLocation(match.group(5).trim(), data);
    data.strMap = match.group(6);
    data.strCross = getOptGroup(match.group(7));
    data.strUnit = match.group(8).trim();
    data.strSupp = match.group(9).trim();
    data.strSource = getOptGroup(match.group(10));
    return true;
  }

  private void parseCodeCall(String field, Data data) {
    int pt = field.indexOf(" - ");
    if (pt >= 0) {
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+3).trim();
    } else {
      data.strCall = field;
    }
  }

  private static final Pattern APT_ADDR_PTN = Pattern.compile("(\\S+)-(\\d+ .*)");

  private void parseLocation(String field, Data data) {
    String apt = "";
    Matcher match = APT_ADDR_PTN.matcher(field);
    if (match.matches()) {
      apt = match.group(1);
      field = match.group(2);
    }
    int pt = field.indexOf('[');
    if (pt >= 0) {
      data.strPlace = field.substring(pt+1).trim();
      parseAddress(field.substring(0,pt).trim(), data);
    } else {
      parseAddress(StartType.START_ADDR, field, data);
      data.strPlace = getLeft();
      if (data.strPlace.startsWith("&")) {
        data.strAddress = append(data.strAddress, " ", data.strPlace);
        data.strPlace = "";
      }
    }
    data.strApt = append(apt, "-", data.strApt);
  }
}
