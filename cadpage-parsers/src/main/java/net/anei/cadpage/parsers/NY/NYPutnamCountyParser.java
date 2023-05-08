package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYPutnamCountyParser extends MsgParser {

  private static final Pattern STA_MARKER = Pattern.compile("[\\| ]STA ");
  private static final Pattern TSP_PATTERN = Pattern.compile("\\bTSP\\b");


  public NYPutnamCountyParser() {
    super("PUTNAM COUNTY", "NY");
    setFieldList("CALL PLACE ADDR CITY APT SRC X INFO");
  }


@Override
  public String getFilter() {
    return "911@putnamcountyny.gov,messaging@iamresponding.com,777,888,7127390583";
  }

@Override
  protected boolean parseMsg(String body, Data data) {

    if (body.indexOf('|') < 0) {
      body = body.replace("=20\n", "\n");
      body = body.replace('\n', '|');
      body = stripFieldEnd(body, "=20");
    }

    Matcher match = STA_MARKER.matcher(body);
    if (!match.find()) return false;
    String sPart1 = body.substring(0,match.start()).trim();
    String sPart2 = body.substring(match.end()).trim();

    Parser p = new Parser(sPart1);
    data.strCall = p.get('|');
    data.strPlace = p.get('|');
    String sAddr = p.get(',');
    sAddr = TSP_PATTERN.matcher(sAddr).replaceAll("TACONIC STATE PKWY");
    data.strCity = p.get("|APT");
    data.strApt = p.get();
    p = new Parser(sPart2);
    data.strSource = p.get(' ');
    String sCross = p.get('|');
    if (sCross.equals("XS")) sCross = "";
    if (sCross.startsWith("XS ")) sCross = sCross.substring(3).trim();
    data.strSupp = p.get();
    if (data.strSupp.startsWith("NARR ")) data.strSupp = data.strSupp.substring(5).trim();

    if (sAddr.length() == 0) {
      sAddr = sCross;
      sCross = "";
    }
    parseAddress(sAddr, data);
    data.strCross = sCross;
    return true;
  }

}