package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class VABuckinghamCountyParser extends MsgParser {

  public VABuckinghamCountyParser() {
    super("BUCKINGHAM COUNTY", "VA");
    setFieldList("SRC ID CALL ADDR CITY ST");
  }

  private static final Pattern MASTER = Pattern.compile("#(\\d{5,}-\\d+) : (.*?) @ (.*)");
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCall = match.group(2).trim();
    Parser p = new Parser(match.group(3).trim());

    String zip = null;
    String city = p.getLastOptional(',');
    if (ZIP_PTN.matcher(city).matches()) {
      zip = city;
      city = p.getLastOptional(',');
    }

    if (city.equalsIgnoreCase("Virginia")) {
      data.strState = "VA";
      city = p.getLastOptional(',');
    }

    if (city.length() == 0 && zip != null) city = null;
    data.strCity = city;

    parseAddress(p.get(), data);
    return true;
  }
}
