package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHMahoningCountyCParser extends DispatchA19Parser {

  public OHMahoningCountyCParser() {
    this("MAHONING COUNTY", "OH");
  }

  public OHMahoningCountyCParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "OHMahoningCountyC";
  }


  @Override
  public String getFilter() {
    return "noreply@canfield.local,noreply@boardman.gov,@alert.active911.com,cadnotification@flex.comS,dispatch@mahoningcountyoh.gov,ssinn@austintowntwp.com,canfieldpd@canfield.gov";
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
      "BMN", "BOARDMAN",
      "BOA", "BOARDMAN TWP",
      "CAN", "CANFIELD TWP",
      "COI", "COITSVILLE TWP",
      "CRA", "CRAIG BEACH",
      "DEE", "DEERFIELD TWP",
      "ELL", "ELLSWORTH TWP",
      "GOS", "GOSHEN TWP",
      "GRE", "GREEN TWP",
      "GRN", "GREEN TWP",
      "JAC", "JACKSON TWP",
      "LEE", "LEETONIA",
      "MIL", "MILTON TWP",
      "PAL", "PALMYRA TWP",
      "POL", "POLAND TWP",
      "SAL", "SALEM",
      "SMI", "SMITH TWP",
      "SPR", "SPRINGFIELD TWP",
      "WAS", "WASHINGTONVILLE"

  });
}
