package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHMahoningCountyCParser extends DispatchA19Parser {
  
  public OHMahoningCountyCParser() {
    super(CITY_CODES, "MAHONING COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "noreply@canfield.local";
  }
  
  private static final Pattern CITY_ZIP_PTN = Pattern.compile("(.*) \\d{5}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = CITY_ZIP_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1).trim();
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AUS", "AUSTINTOWN TWP",
      "BEA", "BEAVER TWP",
      "BER", "BERLIN TWP",
      "BOA", "BOARDMAN TWP",
      "CAN", "CANFIELD TWP",
      "COI", "COITSVILLE TWP",
      "ELL", "ELLSWORTH TWP",
      "GOS", "GOSHEN TWP",
      "GRE", "GREEN TWP",
      "JAC", "JACKSON TWP",
      "MIL", "MILTON TWP",
      "POL", "POLAND TWP",
      "SMI", "SMITH TWP",
      "SPR", "SPRINGFIELD TWP"
  });
}
