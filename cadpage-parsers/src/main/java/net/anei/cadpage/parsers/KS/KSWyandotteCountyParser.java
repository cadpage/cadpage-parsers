package net.anei.cadpage.parsers.KS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KSWyandotteCountyParser extends FieldProgramParser {
  
  public KSWyandotteCountyParser() {
    super("WYANDOTTE COUNTY", "KS", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! PRI:PRI! DATE:DATETIME! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@kckfd.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("HH/dd/yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

}
