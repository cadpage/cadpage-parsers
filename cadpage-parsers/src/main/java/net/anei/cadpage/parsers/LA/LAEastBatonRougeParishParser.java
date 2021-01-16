package net.anei.cadpage.parsers.LA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAEastBatonRougeParishParser extends FieldProgramParser {

  public LAEastBatonRougeParishParser() {
    super("EAST BATON ROUGE PARISH", "LA",
          "CALL ADDR APT CITY X MAP CH INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "informcad@stgeorgefire.com";
  }

  private static final Pattern DELIM = Pattern.compile(" *,\\[\\d+\\](?: \\[\\d+\\])? *| ,");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Inform CAD Paging")) return false;
    if (!parseFields(DELIM.split(body), data)) return false;
    if (data.strCity.equals("PARISH")) data.strCity = "";
    return true;
  }

}
