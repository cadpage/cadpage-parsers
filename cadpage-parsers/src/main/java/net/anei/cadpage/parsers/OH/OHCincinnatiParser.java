package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHCincinnatiParser extends FieldProgramParser {

  public OHCincinnatiParser() {
    super("CINCINNATI", "OH", 
          "Unit:UNIT? Incident:CALL! Address:ADDR! Alarm_Level:PRI! TAC_Channel:CH! Location:PLACE! END");
  }
  
  @Override
  public String getFilter() {
    return "inforad@cincinnati-oh.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD page")) return false;
    body = body.replace("-Location:", " Location:").replaceAll(" TAC channel:", " TAC Channel:");
    return super.parseMsg(body, data);
  }
}
