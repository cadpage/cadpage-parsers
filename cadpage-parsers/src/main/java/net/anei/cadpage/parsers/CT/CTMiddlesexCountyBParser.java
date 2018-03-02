package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;


public class CTMiddlesexCountyBParser extends DispatchRedAlertParser {
  
  public CTMiddlesexCountyBParser() {
    super("MIDDLESEX COUNTY","CT");
  }
  
  @Override
  public String getFilter() {
    return "cromwellfd@rednmxcad.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("Call: 1-860-316-4028 to Respond.",  " ");
    return super.parseMsg(subject, body, data);
  }
  
}
