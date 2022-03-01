package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXArcherCountyParser extends FieldProgramParser {

  public TXArcherCountyParser() {
    super("ARCHER COUNTY", "TX",
          "CALL ADDR CITY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadalerts@kologik.com";
  }

  private static Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d:\\d\\d) @ .*");
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

  private static final Pattern INFO_PTN = Pattern.compile("\\w+ - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

}
