package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INWayneCountyBParser extends FieldProgramParser {

  public INWayneCountyBParser () {
    super("WAYNE COUNTY", "IN",
          "UNIT DATETIME CALL ADDRCITYST APT PLACE X ( NAME! PHONE/Z INFO END | INFO END )");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PFX_PTN = Pattern.compile("(?:APT|LOT|RM) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PFX_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }



  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String part : INFO_DATE_TIME_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "401 E MAIN ST",                        "+39.828242,-84.895781"
  });
}
