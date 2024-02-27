package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;


public class NCWayneCountyCParser extends DispatchH01Parser {

  public NCWayneCountyCParser() {
    super("WAYNE COUNTY", "NC",
          "JUNK+? ( RESPONSE! Sequence_Number:ID! Status:SKIP! Response_Type:CALL! Handling_Unit:UNIT! Address:ADDR! Latitude:GPS1! Longitude:GPS2! NOTES+ " +
                  "| RR_MARK/R! Location:ADDR! Zone:MAP! Response_Type:CALL! CreationTime:DATETIME! RR_NOTES+ )");
  }

  @Override
  public String getFilter() {
    return "waynecounty911@waynegov.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("JUNK")) return new  SkipField("[^\\p{ASCII}]*", true);
    if (name.equals("RESPONSE")) return new SkipField("Response", true);
    if (name.equals("RR_MARK")) return new SkipField("Completed Incident Report", true);
    return super.getField(name);
  }
}
