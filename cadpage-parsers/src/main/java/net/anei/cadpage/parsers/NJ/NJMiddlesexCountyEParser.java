package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class NJMiddlesexCountyEParser extends DispatchA48Parser {

  public NJMiddlesexCountyEParser() {
    super(null, "MIDDLESEX COUNTY", "NJ", FieldType.X);
  }

  @Override
  public String getFilter() {
    return "@Rutgers.edu";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean badDate = false;
    if (subject.startsWith("RipAndRun_NBFD_")) {
      badDate = true;
      subject = "As of 99/99/99 99:99:99";
    }
    if (!super.parseMsg(subject, body, data)) return false;
    if (badDate) data.strDate = data.strTime = "";
    return true;
  }

}
