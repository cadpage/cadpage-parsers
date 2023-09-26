
package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Navajo County, AZ
 */

public class AZNavajoCountyAParser extends FieldProgramParser {

  public AZNavajoCountyAParser() {
    super("NAVAJO COUNTY", "AZ",
          "SRC? ADDRCITY ID CALL INFO! INFO+? X GOOGLE END");
  }

  @Override
  public String getFilter() {
    return "@showlowaz.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("Dispatched Call[, ]+(?:Unit: (\\S+) )?\\([A-Z]+\\)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strUnit = getOptGroup(match.group(1));
    return parseFields(body.split("\n\n"), data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}");
    if (name.equals("ID")) return new IdField("(?:\\S+ +)?(\\d{12}|0)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GOOGLE")) return new MyGoogleField();
    return super.getField(name);
  }

  private static final Pattern CROSS_ST_PTN = Pattern.compile("CROSS ST\\..*|[A-Z0-9 ]+/[A-Z0-9 ]+");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:Cellular E911 Call|Lat|Lon|Service Class):.*");

  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // If this is the last field, see if it looks like a cross street
      if (isLastField(+1)) {
        if (CROSS_ST_PTN.matcher(field).matches()) return false;
        if (checkAddress(field) == STATUS_STREET_NAME) return false;
      }

      // if next to last field, see if last field looks like the Google marker
      else if (isLastField(+2)) {
        String next = getRelativeField(+1);
        if (next.startsWith("Google") ||  next.equals("Goo")) return false;
      }
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        if (!INFO_JUNK_PTN.matcher(line).matches()) {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "CROSS ST.");
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("&ll=([-+]?\\d{2,3}\\.\\d{3,},[-+]?\\d{2,3}\\.\\d{3,})\\b");

  private class MyGoogleField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Goo")) abort();
      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) setGPSLoc(match.group(1), data);
    }
  }
}
