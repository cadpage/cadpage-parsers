package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAJacksonCountyBParser extends FieldProgramParser {

  public GAJacksonCountyBParser() {
    super("JACKSON COUNTY", "GA",
          "CALL ADDRCITY PHONE PLACE! ID INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadpage@jacksoncountygov.com";
  }

  private static final Pattern MARKER = Pattern.compile("CAD Page:(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) {3,}");
  private static final Pattern DELIM = Pattern.compile(" /+|\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Text Message")) return false;

    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PHONE")) return new PhoneField("\\d{7,10}|", true);
    return super.getField(name);
  }
}
