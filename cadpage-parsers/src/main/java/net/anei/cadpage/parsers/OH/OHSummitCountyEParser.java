package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHSummitCountyEParser  extends FieldProgramParser {
  
  public OHSummitCountyEParser() {
    super("SUMMIT COUNTY", "OH",
           "DATE:DATE! TIME:TIME! FIRE:UNIT! NAME:PLACE! ADDRESS:ADDR! CITY:CITY! DESCRIPTION:CALL! CROSS_1:X! CROSS_2:X EMPTY");
  }

  @Override
  public String getFilter() {
    return "cad@fairlawn.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_REMOVE_EXT;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page (\\d\\d-\\d{6})");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    return parseFields(body.split(" - "), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  public class MyAddressField extends AddressField{

    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
}
