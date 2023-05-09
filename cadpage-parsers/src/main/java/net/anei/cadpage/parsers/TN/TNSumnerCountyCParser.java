package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class TNSumnerCountyCParser extends SmartAddressParser {

  public TNSumnerCountyCParser() {
    super(TNSumnerCountyParser.CITY_LIST, "SUMNER COUNTY", "TN");
    setFieldList("ADDR APT CITY CALL INFO");
  }

  @Override
  public String getFilter() {
    return "service@sumnerecc.org";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) CALL TYPE [- ]*(.*?) *(?:\\.--- *(.*))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ECC TEXT-")) return false;
    body = stripFieldEnd(body, "/");
    if (body.startsWith("Comment:")) {
      String[] parts = body.substring(9).split(" // ");
      if (parts.length < 3) return false;
      parseAddress(parts[0].trim(), data);
      data.strCall = parts[1].trim();
      for (int j = 2; j<parts.length; j++) {

        data.strSupp = append(data.strSupp, "\n", parts[j].trim());
      }
    }
    else {
      Matcher match = MASTER.matcher(body);
      if (!match.matches()) return false;
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(1).trim(), data);
      data.strCall = match.group(2).trim();
      data.strSupp = getOptGroup(match.group(3));
    }
    return true;
  }
}
