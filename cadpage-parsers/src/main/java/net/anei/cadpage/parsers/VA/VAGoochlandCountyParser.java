package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAGoochlandCountyParser extends DispatchOSSIParser {

  public VAGoochlandCountyParser() {
    super("GOOCHLAND COUNTY", "VA",
           "( CANCEL ADDR | FYI? CALL ADDR! X X ) INFO+");
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = COTTONTAIL_CHSE_PTN.matcher(addr).replaceAll("COTTONTAIL CHASE RD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern COTTONTAIL_CHSE_PTN = Pattern.compile("\\bCOTTONTAIL CHSE(?: RD)?\\b", Pattern.CASE_INSENSITIVE);
}
