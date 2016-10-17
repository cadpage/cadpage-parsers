package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHEnglewoodParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("([^:]+?):(\\d\\d:\\d\\d:\\d\\d)-\nCode:([A-Z0-9]+:[-A-Z0-9]*)\nCall: *([^\n]+?)(?:, *(?:APT|RM|ROOM) *([^,]*?))?,\n([^,]*?),([^,]+?),([A-Z ]*?):\nCross: *(.*?)\nInfo: *(.*)", Pattern.DOTALL);
  
  public OHEnglewoodParser() {
    super("ENGLEWOOD", "OH");
    setFieldList("UNIT TIME CODE CALL PLACE ADDR APT CITY X INFO");
  }
  
  @Override
  public String getFilter() {
    return "CAD@englewood.oh.us";
  }
  
  @Override
  public String getLocName() {
    return "Englewood Regional Communications, OH";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    int ndx = 1;
    data.strUnit = match.group(ndx++).trim();
    data.strTime =match.group(ndx++).trim();
    data.strCode = match.group(ndx++).trim();
    data.strCall = match.group(ndx++).trim();
    String apt = getOptGroup(match.group(ndx++));
    data.strPlace = match.group(ndx++).trim();
    String sAddr = match.group(ndx++).trim().replace("@", " & ");
    parseAddress(sAddr, data);
    data.strApt = append(apt, "-", data.strApt);
    data.strCity = match.group(ndx++).trim();
    data.strCross = match.group(ndx++).trim();
    data.strSupp = match.group(ndx++).trim();
    return true;
  }
}
