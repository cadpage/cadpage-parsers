package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAKlickitatCountyParser extends DispatchA19Parser {

  public WAKlickitatCountyParser() {
    super(CITY_CODES, "KLICKITAT COUNTY", "WA");
   }

  @Override
  public String getFilter() {
    return "@alert.active911.com,noreply@klickitatcounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("THE DALLES")) data.strState = "OR";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Properties CITY_CODES =  buildCodeTable(new String[] {
      "ALD", "PROSER", //???
      "APP", "APPLETON",
      "BCK", "BICKLETON",
      "BIN", "BINGEN",
      "BZ",  "BZ CORNERS",
      "CEN", "CENTERVILLE",
      "DAL", "DALLESPORT",
      "GLD", "GOLDENDALE",
      "GLN", "GLENWOOD",
      "HPR", "HIGH PRARIE",
      "HUS", "HUSUM",
      "KLI", "KLICKITAT",
      "LYL", "LYLE",
      "MAR", "MARRYHILL",
      "MUR", "MURDOCK",
      "RSV", "ROOSEVELT",
      "SKA", "SKAMANIA COUNTY",
      "SNO", "SNOWDEN",
      "THD", "THE DALLES",
      "TIM", "TIMBER VALLEY",
      "TL",  "TROUT LAKE",
      "WAH", "WAHKIACUS",
      "WHK", "WAHKIACUS",
      "WS",  "WHITE SALMON",
      "WSH", "WISHRAM"

  });
}
