package net.anei.cadpage.parsers.NM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NMDonaAnaCountyDParser extends FieldProgramParser {

  public NMDonaAnaCountyDParser() {
    super("DONA ANA COUNTY", "NM", 
          "CALL ID ADDRCITY UNIT! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "paging@mvrda.org";
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) - +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISP")) return false;
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    body = body.substring(match.end());
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram();
  }

}
