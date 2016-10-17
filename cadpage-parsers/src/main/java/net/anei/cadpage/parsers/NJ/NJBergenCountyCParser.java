package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJBergenCountyCParser extends FieldProgramParser {

  public NJBergenCountyCParser() {
    super("BERGEN COUNTY", "NJ", 
          "CAD#:ID! Date:DATE! Time:TIME! Address:ADDR! Type_of_Fire_Response:CALL! Type_of_Service:CITY! Notes:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "srpd2srfd@saddleriver.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fire Page")) return false;

    return super.parseFields(body.split("\\n+"), data);
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{5}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "In ");
      super.parse(field, data);
    }
  }
}
