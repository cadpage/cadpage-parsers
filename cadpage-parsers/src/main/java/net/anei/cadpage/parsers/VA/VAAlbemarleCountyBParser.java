package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Albemarle County, VA (B)
 */

public class VAAlbemarleCountyBParser extends FieldProgramParser {
  
  public VAAlbemarleCountyBParser() {
    super("ALBEMARLE COUNTY", "VA",
          "SRC! INC:ID! TYP:CALL! UNITS:UNIT! AD:ADDRCITY% APT:APT% CROSS_STREETS:X% NAME:NAME% NATURE:CALL/SDS% NARRATIVE:INFO/N+ ESN:BOX% DT:DATETIME% END");
    setupProtectedNames("LEWIS AND CLARK");
  }
  
  @Override
  public String getFilter() {
    return "cad2@acuecc.org ";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    
    // Apt is usually duplicated at end of address
    if (data.strApt.length() > 0) {
      data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strApt);
    }
    return true;
  }
  
  private static DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z ]+", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("[", "").replace("]", "").trim();
      if (field.startsWith("Incident not yet created")) return;
      super.parse(field, data);
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override 
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
}
