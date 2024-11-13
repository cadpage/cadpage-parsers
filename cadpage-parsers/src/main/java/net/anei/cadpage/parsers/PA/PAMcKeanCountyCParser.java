package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PAMcKeanCountyCParser extends DispatchA57Parser {

  public PAMcKeanCountyCParser() {
    this("MCKEAN COUNTY", "PA");
  }

  public PAMcKeanCountyCParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "noreply@ntr911sa.com";
  }
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(.*) -(\\d+)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;

    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strCode = match.group(2);
    }

    data.strCity = stripFieldEnd(data.strCity, " BORO");

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CALL CODE");
  }

}
