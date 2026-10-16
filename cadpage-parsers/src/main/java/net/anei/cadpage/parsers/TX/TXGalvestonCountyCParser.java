package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TXGalvestonCountyCParser extends DispatchH03Parser {

  public TXGalvestonCountyCParser() {
    super("GALVESTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "GCSO.GCSO@Premier-one.local";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.startsWith("Active911 Alert for ")) {
      data.strSource = subject.substring(20).trim();
    }
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
