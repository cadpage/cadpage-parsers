package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NYNightwatchSecurityParser extends FieldProgramParser {
  
  private static final Pattern WRAP_PTN = Pattern.compile("\n(?![ \n])"); 
  private static final Pattern BLANK_KEYWORD = Pattern.compile(" +(?=Date:|Time:)");

  public NYNightwatchSecurityParser() {
    super("STATEN ISLAND", "NY",
           "Reason_for_Call:CALL! Date:DATE! Order_Number:ID! Time:TIME! Operator:SKIP Name:NAME! Callback_Number:PHONE! Description_Of_Incident:INFO Description_Of_Incident:INFO Location:ADDR! Dispatch_Notes:INFO Message:INFO INFO+? EMPTY"); 
 }
  
  @Override
  public String getFilter() {
    return "scheduler@specialtyansweringservice.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // General alert (there's not ours) is a special case
    if (subject.equals("General Alert")) {
      data.strCall = "** General Alert **";
      parseAddress(body, data);
      return true;
    }
    
    // Next we need to correct for some internal wraparround
    body = WRAP_PTN.matcher(body).replaceAll(" ");
    
    // And add line breaks in front of the Date: and Time: fields
    body = BLANK_KEYWORD.matcher(body).replaceAll("\n");
    
    // From here, everything is pretty basic
    return parseFields(body.split("\n"), data);
  }
  
  // Address field isn't usually a real address, and it occasionally
  // contains # that should not be interpreted as appartment numbers
  // So we need to protect them
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("#", "%%%");
      super.parse(field, data);
      data.strAddress = data.strAddress.replace("%%%", "#");
    }
  }
  
  // Empty field marks ends of Message: information
  private class EmptyField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return (field.length() == 0);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("EMPTY")) return new EmptyField();
    return super.getField(name);
  }
}
	