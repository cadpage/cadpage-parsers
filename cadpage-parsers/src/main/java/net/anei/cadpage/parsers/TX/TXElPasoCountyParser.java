package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXElPasoCountyParser extends FieldProgramParser {

  public TXElPasoCountyParser() {
    super("EL PASO COUNTY", "TX",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "shane.soulor@valservices.co";
  }

  private static final Pattern DELIM = Pattern.compile(">?\n[>\n]*");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(DELIM.split(body), data)) return false;
    if (data.strSupp.equals("None")) data.strSupp = "";
    return true;
  }
}
