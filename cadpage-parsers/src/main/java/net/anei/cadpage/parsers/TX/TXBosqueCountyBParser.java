package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBosqueCountyBParser extends FieldProgramParser {

  public TXBosqueCountyBParser() {
    super("BOSQUE COUNTY", "TX",
          "CALL ADDR CITY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadalerts@kologik.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) @ .*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strTime = match.group(1);

    return parseFields(body.split(";"), data);
  }

  @Override
  public String getProgram() {
    return "TIME " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_DELIM = Pattern.compile(" *\\| *");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("[A-Z]+ - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_DELIM.split(field)) {
        Matcher match = INFO_HDR_PTN.matcher(part);
        if (match.lookingAt()) part = part.substring(match.end());
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
