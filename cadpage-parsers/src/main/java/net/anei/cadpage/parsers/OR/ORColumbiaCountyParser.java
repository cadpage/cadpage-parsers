package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORColumbiaCountyParser extends FieldProgramParser {

  public ORColumbiaCountyParser() {
    super("COLUMBIA COUNTY", "OR",
          "ID_CALL_UNIT CITY ADDR APT! MAP? X:X! COMMENTS:INFO? INFO/N+");
  }

  private static final Pattern DELIM = Pattern.compile(" ;| (?=Map |X:|COMMENTS:)|(?<=\\S)(?=Map )|[ ,](?=\\[\\d{1,2}\\] )");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_CALL_UNIT")) return new MyIdCallUnitCityField();
    if (name.equals("APT")) return new AptField("Apartment:? *(.*)", true);
    if (name.equals("MAP")) return new MapField("Map +(.*)", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static Pattern ID_CALL_UNIT_PTN = Pattern.compile("([A-Z]{0,5}\\d{7,9}) +([A-Z0-9]+) - ([-/ A-Z0-9]+) :([A-Z0-9,]+)");

  private class MyIdCallUnitCityField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CALL_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();

      data.strCallId = match.group(1);
      data.strCode = match.group(2);
      data.strCall = match.group(3).trim();
      data.strUnit = match.group(4).trim();
    }

    @Override
    public String getFieldNames() {
      return "ID CODE CALL UNIT CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("No X Street", "");
      field = stripFieldStart(field,  "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);

    }
  }
}
