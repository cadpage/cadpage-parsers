package net.anei.cadpage.parsers.ID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDBlaineCountyBParser extends FieldProgramParser {

  public IDBlaineCountyBParser() {
    super("BLAINE COUNTY", "ID",
          "CALL ADDRCITYST! X INFO/N+");
  }

  @Override
  public String getFilter() {
    return "zdispatch@co.blaine.id.us";
  }

  private static final Pattern MASTER = Pattern.compile(".*?[\n ]*\nBCEC: ([A-Z]{2}\\d{6}-\\d{3}) -- ");
  @Override
  protected boolean parseMsg(String body , Data data) {

    Matcher match = MASTER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId =  match.group(1);
    body = body.substring(match.end()).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
}
