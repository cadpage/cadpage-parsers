package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAMontgomeryCountyGParser extends FieldProgramParser {
  
  public PAMontgomeryCountyGParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA",
           "INC#:ID! CALL! Addr:ADDR Box:BOX! ADC:MAP! MUN:CITY! Units:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch-";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Dispatch-")) return false;
    data.strSource = subject.substring(9).trim();
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[EF]\\d+");
    return super.getField(name);
  }
}
