package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAHenricoCountyParser extends MsgParser {

  private static final Pattern MASTER = 
      Pattern.compile("([CM]\\d{4}) (\\d{2})(\\d{2})hrs (.*?)(?:\\((.*?)\\))? +fd:(\\d+) re:(.*)");

  public VAHenricoCountyParser() {
    super("HENRICO COUNTY", "VA");

    setFieldList("MAP TIME ADDR APT PLACE UNIT CALL INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    String b1 = body.replace("\n", "//");
    Matcher match = MASTER.matcher(b1);
    if (!match.matches()) return false;
    data.strMap = match.group(1);
    data.strTime = match.group(2) + ':' + match.group(3);
    parseAddress(match.group(4).trim(), data);
    data.strPlace = getOptGroup(match.group(5));
    data.strUnit = match.group(6);
    Parser p1 = new Parser(match.group(7).trim());
    String blah = p1.get().replace("//", "/");
    int bi = blah.indexOf("/");
    if (bi >= 0) {
      data.strCall = blah.substring(0, bi).trim();
      data.strSupp = blah.substring(bi + 1).trim();
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = INN_INTERSECTION_PTN.matcher(addr);
    if (match.matches()) addr = match.replaceAll("$1 $2");
    return addr;
  }
  private static final Pattern INN_INTERSECTION_PTN = 
      Pattern.compile("(I[- ]*\\d+).*( & .*)", Pattern.CASE_INSENSITIVE);
}