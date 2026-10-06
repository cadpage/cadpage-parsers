package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.FieldProgramParser;

public class CORioBlancoCountyBParser extends FieldProgramParser {

  public CORioBlancoCountyBParser() {
    super("RIO BLANCO COUNTY", "CO",
        "ADDRCITYST");
  }

  @Override
  public String getFilter() {
    return "smtp@rbc.us";
  }

  private static final Pattern MARK_END_PTN = Pattern.compile("\n-- \n|\n\\*{4}|\n\n");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d[- ]+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MARK_END_PTN.matcher(body);
    if (match.find()) body = body.substring(0, match.start()).trim();
    if (subject.endsWith("TEST PAGE:")) {
      setFieldList("CALL INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCall = subject.substring(0, subject.length()-1).trim();
      data.strSupp = body;
      return true;
    }

    if (subject.endsWith(" PAGE FOR:")) {
      setFieldList("SRC ADDR APT CITY ST CALL INFO");
      data.strSource = subject.substring(0, subject.length()-10).trim();
      body = stripFieldEnd(body, "None");
      String[] flds = INFO_BRK_PTN.split(body);
      if (!parseFields(flds[0], data)) return false;
      if (flds.length > 1) data.strCall = flds[1];
      for (int jj = 2; jj<flds.length; jj++) {
        data.strSupp = append(data.strSupp, "\n", flds[jj]);
      }
      return true;
    }
    return false;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " INFO";
  }
}
