package net.anei.cadpage.parsers.MT;

import java.util.regex.*;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MTParkCountyParser extends FieldProgramParser {
  public MTParkCountyParser() {
    super("PARK COUNTY", "MT",
           "ADDR PLACE_X CALL CALL ( ID CITY! | CITY ID! ) CH SKIP");
  }
  
  @Override
  public String getFilter() {
    return "page@parkcounty.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE_X")) return new PlaceXField();
    if (name.equals("ID")) return new IdField("COMM\\d{2}CAD\\d{6}", true);
    if (name.equals("CH")) return new ChannelField("PTAC\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern PLACE_X_PATTERN
    = Pattern.compile("^(.*)X2\\[(.*)\\]");
  private class PlaceXField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = PLACE_X_PATTERN.matcher(field);
      if (m.matches()) {
        String loc = m.group(1).trim();
        data.strCross = m.group(2).trim();
        if (!loc.equals(data.strAddress)) data.strPlace = loc;
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
}
