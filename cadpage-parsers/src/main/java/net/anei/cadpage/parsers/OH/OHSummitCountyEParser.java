package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHSummitCountyEParser  extends FieldProgramParser {
  
  public OHSummitCountyEParser() {
    super("SUMMIT COUNTY", "OH",
           "ID:ID! FIRE:UNIT! NAME:PLACE! ADDRESS:ADDR! CITY:CITY! DESCRIPTION:CALL! CROSS_1:X! CROSS_2:X! DATE:DATE TIME:TIME");
  }

  @Override
  public String getFilter() {
    return "administrator@ci.fairlawn.oh.us,administrator@fairlawn.us>";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_REMOVE_EXT;
  }
  
  static final Pattern DATE_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4}");
  static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Page ")) return false;
    int i = body.indexOf("\n");
    if (i >= 0){
      body = body.substring(0, i);
      body = body.trim();
    }
    if (!parseFields(body.split("\\|"), data)) return false;
    if (data.strDate.length() > 0 && !DATE_PTN.matcher(data.strDate).matches()) data.strDate = "";
    if (data.strTime.length() > 0 && !TIME_PTN.matcher(data.strTime).matches()) data.strTime = "";
    if (data.strTime.length() == 0) data.strDate = "";
    return true;
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{6}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  public class MyAddressField extends AddressField{

    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      field = field.replaceAll("\\@", " \\& ");
      super.parse(field, data);
    }
  }
}
