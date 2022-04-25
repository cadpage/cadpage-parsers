package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class WIRockCountyAParser extends DispatchH03Parser {

  public WIRockCountyAParser() {
    super(CITY_CODES, "ROCK COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "RCCC@co.rock.wi.us";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]{2,4})-(?:Active911|IAMResp|eRespond)");

  @Override
  public boolean parseHtmlMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1);

    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final Pattern CTY_TK_PTN = Pattern.compile("\\bCTY TK\\b", Pattern.CASE_INSENSITIVE);
  @Override
  public String adjustMapAddress(String addr) {
    addr = CTY_TK_PTN.matcher(addr).replaceAll("COUNTY ROAD");
    return super.adjustMapAddress(addr);

  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AT", "AVON TOWN",
      "BE", "BELOIT",
      "BH", "BRODHEAD",
      "BR", "BROOKLYN",
      "BT", "BELOIT TOWN",
      "CL", "CLINTON",
      "CN", "CENTER TOWN",
      "CT", "CLINTON TOWN",
      "DC", "DANE COUNTY",
      "ED", "EDGERTON",
      "EV", "EVANSVILLE",
      "FO", "FOOTVILLE",
      "FT", "FULTON TOWN",
      "HT", "HARMONY TOWN",
      "JN", "JOHNSTOWN TOWN",
      "JT", "JANESVILLE TOWN",
      "JV", "JANESVILLE",
      "LP", "LAPRARIE TOWN",
      "LT", "LIMA TOWN",
      "MG", "MAGNOLIA TOWN",
      "ML", "MILTON",
      "MT", "MILTON TOWN",
      "NT", "NEWARK TOWN",
      "OR", "ORFORDVILLE",
      "PL", "PLYMOUTH TOWN",
      "PT", "PORTER TOWN",
      "RT", "ROCK TOWN",
      "SP", "SPRING VALLEY TOWN",
      "TU", "TURTLE TOWN",
      "UT", "UNION TOWN",
      "WN", "WINNEBEGAO COUNTY",

      "GRCO", "GREEN COUNTY",
      "JECO", "JEFFERSON COUNTY",
      "WNCO", "WINNEBEGAO COUNTY"


  });
}
