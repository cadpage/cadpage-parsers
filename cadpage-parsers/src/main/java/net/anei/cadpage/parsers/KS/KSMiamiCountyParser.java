package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KSMiamiCountyParser extends FieldProgramParser {

  public KSMiamiCountyParser() {
    super("MIAMI COUNTY", "KS", "ADDR/SC! Description:INFO! INFO+");
  }

  public static Pattern UNITS = Pattern.compile("((?:\\d{3} +)+)(.*Description:.*)", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("SO Call Info")) return false;

    // If it starts with Primary Incident it's a Run Report
    if (body.startsWith("Primary Incident:")) {
      // Run Report
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }

    // Normal page message if it starts with 1 or more \\d{3} unit codes
    Matcher uMat = UNITS.matcher(body);
    if (uMat.matches()) {
      data.strUnit = uMat.group(1).trim();
      body = uMat.group(2);
      return super.parseMsg(body, data);
    }

    // If we've made it all the otherwise it's a General Alert
    data.strCall = "GENERAL ALERT";
    data.strPlace = body;
    return true;
  }
  
  @Override public String getProgram() {
    return "UNIT " + super.getProgram() + " PLACE";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BREAK_PTN = Pattern.compile("\\s*\n\\s*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BREAK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
