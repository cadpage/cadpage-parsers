package net.anei.cadpage.parsers.TN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class TNSumnerCountyBParser extends FieldProgramParser {

  public TNSumnerCountyBParser() {
    super(TNSumnerCountyParser.CITY_LIST, "SUMNER COUNTY", "TN",
          "ADDR APT CITY CALL ID!  END");
  }

  @Override
  public String getFilter() {
    return "2083399423";
  }

  private static final Pattern MASTER = Pattern.compile("CALL ALERT:? (.*?) CALL TYPE:? (.*?)(?: ([A-Z]{3,5}-\\d{2}-\\d{6}))?");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Sumner County ECC:")) return false;
    body = body.substring(18).trim();
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR CITY CALL ID");
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(1).trim(), data);
      data.strCall = match.group(2).trim();
      data.strCallId = getOptGroup(match.group(3));
    } else {
      if (!parseFields(body.split(" :-"), data)) return false;
    }
    data.strCity = convertCodes(data.strCity, FIX_CITY_TABLE);
    return true;
  }

  private static final Pattern PVT_PTN = Pattern.compile(" *\\bPVT\\b *", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = PVT_PTN.matcher(addr).replaceAll(" ").trim();
    return super.adjustMapAddress(addr);
  }

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "HENDERSONVIL", "HENDERSONVILLE"
  });

}
