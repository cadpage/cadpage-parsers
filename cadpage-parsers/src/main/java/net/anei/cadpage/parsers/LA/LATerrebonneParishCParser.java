package net.anei.cadpage.parsers.LA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LATerrebonneParishCParser extends FieldProgramParser {

  public LATerrebonneParishCParser() {
    super("TERREBONNE PARISH", "LA",
          "CODE CALL PLACE ADDRCITYST X ID! END");
  }

  @Override
  public String getFilter() {
    return "tpe911@tpe911.org,no-reply@csprosuite.centralsquarecloudgov.com";
  }

  private static final Pattern DELIM = Pattern.compile("(?<= )\\| +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.endsWith("|")) body += " ";
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("\\d+\\S*", true);
    if (name.equals("ID")) return new IdField("CFS\\d{8}", true);
    return super.getField(name);
  }
}
