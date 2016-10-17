package net.anei.cadpage.parsers.IN;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Madison County, IN (C)
 */
public class INMadisonCountyCParser extends FieldProgramParser {
  
  public INMadisonCountyCParser() {
    super("MADISON COUNTY", "IN",
          "CALL:CALL! DATE:DATETIME! PLACE:PLACE! ADDR:ADDRCITY! INFO:INFO? ( MAP:MAP! CITY:CITY! ID:ID! PRI:PRI! UNIT:UNIT! X:X! SOURCE:SKIP! | UNIT:UNIT! X:X! MAP:MAP! ) CALLER-NAME:NAME! CALLER-PHONE:PHONE! INCIDENT#:SKIP! OTHER_INCIDENT#:SKIP? DISTRICT:SKIP? BEAT:MAP! LOCATION_DETAILS:INFO/N+ NARRATIVE:INFO/N+ RADIO_CHANNEL:CH");
  }
  
  @Override
  public String getFilter() {
    return "cfs@madisoncountypaging.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CFS")) return false;
    body = body.replace("\nINTERSECTION:", "\nX:");
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa"), true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return CORD_PTN.matcher(address).replaceAll("");
  }
  private static final Pattern CORD_PTN = Pattern.compile("\\bCORD\\b");
}
