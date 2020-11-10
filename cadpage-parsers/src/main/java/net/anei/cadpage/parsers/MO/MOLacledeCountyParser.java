package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOLacledeCountyParser extends DispatchA33Parser {

  public MOLacledeCountyParser() {
    super("LACLEDE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "LACLEDECOES@OMNIGO.COM";
  }

  private static final Pattern CROSS_TYPE_PTN = Pattern.compile("(.*?)(?:CON|COUNTY|RURAL|ION|EEN|UT|HWES|(?<=\\b(?:AVE?|DR|LN|MO|RD|ST|\\d{1,3}|^))[EH])");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CROSS_TYPE_PTN.matcher(data.strCross);
    if (match.matches()) data.strCross = match.group(1).trim();

    if (data.strState.isEmpty()) {
      if (data.strCross.equals("MO")) {
        data.strState = data.strCross;
        data.strCross = "";
      } else if (data.strCross.endsWith(", MO")) {
        data.strCity = append(data.strCity, " ", data.strCross.substring(0, data.strCross.length()-4).trim());
        data.strState = "MO";
        data.strCross = "";
      }
    }
    return true;
  }

}
