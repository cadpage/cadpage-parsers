package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Somerset County, NJ
 */
public class NJSomersetCountyBParser extends FieldProgramParser {

  private static final Pattern MASTER = Pattern.compile("TYPE - ([A-Za-z0-9]+) *- *(.*?) LOC - (.*) XST - (.*?) NAR -(?: (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)~(.*))?");
  
  public NJSomersetCountyBParser() {
    super("SOMERSET COUNTY", "NJ",
        "TYPE:CODE! CALL! LOC:ADDR! XST:X! NAR:INFO");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("fCAD")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = match.group(1);
    data.strCall = match.group(2).trim();
    String addr = match.group(3).replace('~', ' ').trim();
    parseAddress(addr, data);
    data.strCross = match.group(4).trim();
    if (data.strCross.equals("~")) data.strCross = "";
    data.strDate = getOptGroup(match.group(5));
    data.strTime = getOptGroup(match.group(6));
    data.strSupp = getOptGroup(match.group(7));
    return true;
  }
}



