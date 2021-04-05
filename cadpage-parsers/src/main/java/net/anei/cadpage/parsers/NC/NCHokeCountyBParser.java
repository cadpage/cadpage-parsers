package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCHokeCountyBParser extends FieldProgramParser {

  public NCHokeCountyBParser() {
    super("HOKE COUNTY", "NC",
          "CALL ADDRCITYST X PLACE INFO! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,74121";
  }

  private static final Pattern MARKER = Pattern.compile("HOKE CO(?:UNTY)? 911:");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        body = body.substring(match.end()).trim();
        break;
      }

      if (subject.equals("HOKE COUNTY 911") || subject.equals("HOKE CO 911")) break;

      return false;
    } while (false);

    return parseFields(body.split("\\*\\*", -1), data);
  }
}
