package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXCollinCountyCParser extends DispatchA82Parser {

  public TXCollinCountyCParser() {
    this("COLLIN COUNTY", "TX");
  }

  protected TXCollinCountyCParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "icsuser@co.collin.tx.us,ccsodispatch@co.collin.tx.us,inforad@co.walker.tx.us,prosperdispatch@prospertx.gov,ispage@murphytx.org,CADPaging-NOREPLY@flower-mound.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\nConfidentiality notice:");
    if (pt >= 0)  body = body.substring(0,pt).trim();

    if (!super.parseMsg("", body, data)) return false;

    if (!subject.isEmpty() && !subject.equals("CFS Page")) {
      subject = stripFieldStart(subject, "Message from Dispatch");
      data.strCall = append(subject, " - ", data.strCall);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }
}
