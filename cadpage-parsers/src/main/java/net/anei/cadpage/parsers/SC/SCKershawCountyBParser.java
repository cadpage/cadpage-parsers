package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class SCKershawCountyBParser extends MsgParser {

  public SCKershawCountyBParser() {
    super("KERSHAW COUNTY", "SC");
    setFieldList("CALL ADDR APT CITY ST UNIT DATE TIME CODE INFO");
  }

  @Override
  public String getFilter() {
    return "cadreports@kershaw.sc.gov";
  }

  private static final Pattern MASTER = Pattern.compile(" *\\b((?:[A-Z0-9]+; *)*[A-Z0-9]+) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) Respond Immediately\\. (CARDIAC ARREST|\\S+) +");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    data.strCall = subject;

    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.find()) return false;
    String addr = body.substring(0,match.start());
    data.strUnit = match.group(1).replace("; ", ",");
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCode = match.group(4);
    String info = body.substring(match.end());

    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    String zip = null;
    match = ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      zip =  match.group(2);
      city = p.getLastOptional(',');
    }
    parseAddress(p.get(), data);
    if (city.isEmpty() && zip != null) city = zip;
    data.strCity = city;

    data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n").trim();
    return true;
  }
}
