package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyEParser extends FieldProgramParser {

  public WASnohomishCountyEParser() {
    super("SNOHOMISH COUNTY", "WA", 
          "ADDRCITY APT CITY CALL CH UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "NoReply@snopac911.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISP")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern BAD_ADDR_PTN = Pattern.compile(">>.*|[A-Z]+");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      // Eliminate WASnohomishCountyB alerts
      if (BAD_ADDR_PTN.matcher(field).matches()) abort();
      
      field = field.replace('@', '/');
      super.parse(field, data);
    }
  }
}
