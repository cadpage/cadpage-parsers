package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyD2Parser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyD2Parser() {
    super("CALL SKIP INFO+? DATE! TIME");
  }
  
  @Override
  public String getFilter() {
    return "pfdfire@fdcms.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Address is passed in subject
    if (!subject.contains(",")) return false;
    parseChesterAddress(subject, data);
    
    // And all of the should treat line breaks as spaces
    body = body.replace('\n', ' ');
    
    // Split fields by asterisk and parse
    return parseFields(body.split("\\*"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "ADDR CITY " + super.getProgram();
  }

} 
