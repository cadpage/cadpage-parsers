package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOPerryCountyBParser extends DispatchBCParser {

  public MOPerryCountyBParser() {
    super("PERRY COUNTY", "MO");
    removeWords("STE");
  }

  @Override
  public String getFilter() {
    return "PERRY@OMNIGO.COM,noreply@omnigo.com";
  }

  private static final Pattern STE_PTN = Pattern.compile("\\bSTE\\b");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // SAINTE is abbreviated STE which is confused as an suite identifier
    // so we will expand it before trying anything else
    body = STE_PTN.matcher(body).replaceAll("SAINTE");
    return super.parseHtmlMsg(subject, body, data);
  }
}
