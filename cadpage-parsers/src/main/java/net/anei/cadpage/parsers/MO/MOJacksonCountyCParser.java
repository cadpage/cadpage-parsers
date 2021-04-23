package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJacksonCountyCParser extends FieldProgramParser {

  public MOJacksonCountyCParser() {
    super("JACKSON COUNTY", "MO",
          "( Date:DATETIME! ID! Type:CALL! Location:ADDR! CITY! Latitude:GPS1! Longitude:GPS2! Mapgrid:MAP! Units_Responding:UNIT! UNIT/C+ " +
          "| CALL_ADDR JUNK? CITY X SKIP MAP? BOX! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "firecomm@cityofls.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident")) return false;
    return parseFields(body.split("[,\n]"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("#(\\d+)", true);
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("JUNK")) return new SkipField(".*/.*", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MapField("\\d{3}[A-Z]", true);
    if (name.equals("BOX")) return new BoxField("\\d{2,4}", true);
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d-\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2).replace('-', '/') + '/' + match.group(1);
      data.strTime = match.group(3);
    }
  }

  private static final Pattern CALL_ADDR_PTN = Pattern.compile("([- A-Z0-9]+): (.*)");
  private class MyCallAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1);
      super.parse(match.group(2).trim(), data);
    }

    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("[None selected]")) return;
      super.parse(field, data);
    }
  }

}
