package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class NCEdgecombeCountyBParser extends MsgParser {

  public NCEdgecombeCountyBParser() {
    super("EDGECOMBE COUNTY", "NC");
    setFieldList("CALL ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "csnoreply@tarboro-nc.com";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) None (.*)");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    data.strCall = subject;

    body = stripFieldEnd(body, "{current_command_messsage}");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    String addr = match.group(1).trim();
    String info = match.group(2).trim();

    Parser p =  new Parser(addr);
    String city = p.getLastOptional(',');
    if ((match = ST_ZIP_PTN.matcher(city)).matches()) {
      data.strState = match.group(1);
      String zip =  match.group(2);
      city = p.getLastOptional(',');
      if (city.isEmpty() && zip != null) city = zip;
    }
    parseAddress(p.get(), data);
    data.strCity = city;

    if (!info.startsWith("None")) {
      data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n").trim();
    }

    return true;
  }
}

