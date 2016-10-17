package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAJamesCityCountyParser extends DispatchOSSIParser {
  
  public VAJamesCityCountyParser() {
    super("JAMES CITY COUNTY", "VA",
        "( ADDR/SZ PLACE CALL DATETIME | ADDR/SZ CALL DATETIME | CALL ADDR/S! ) X X? INFO+");
  }
    
  @Override
  public String getFilter() {
    return "CAD@jamescitycountyva.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

      // Reject any message that does not start with "CAD:"
 
    if(!body.startsWith("CAD:")) return false;
    
    // Check to see if the subject contains field data and if so, combine with message body
    if(subject.length() > 0 && !subject.equals("Text Message")) {
      body = body.substring(0,4) + subject + body.substring(3);
    }
    
    // Parse the fields
    if (parseMsg(body, data)) return true;
    
    // If parsing fails, return general alert
    return data.parseGeneralAlert(this,body);
  }
  

  @Override
  public Field getField(String name) {
//    if (name.equals("DATETIME")) return new DateTimeField("\\d{2}\\/\\d{2}\\/\\d{4} \\d{2}:\\d{2}:\\d{2}", true);
    return super.getField(name);
  }
}
