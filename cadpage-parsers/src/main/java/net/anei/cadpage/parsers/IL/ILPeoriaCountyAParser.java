package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA6Parser;



public class ILPeoriaCountyAParser extends DispatchA6Parser {
  
  private static final Pattern JR_PATTERN = Pattern.compile("(?<= )Jr=(?=[A-Z]{2}\\b)");
  
  public ILPeoriaCountyAParser() {
    super(CITY_CODES, "PEORIA COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "firepage@ci.peoria.il.us,firepage@peoriagov.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = JR_PATTERN.matcher(body).replaceFirst("");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = PK_PTN.matcher(address).replaceAll("PKWY");
    return super.adjustMapAddress(address);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[] {
    "BA", "BARTONVILLE",
    "BE", "BELLEVUE",
    "BR", "BRIMFIELD",
    "CH", "CHILLICOTHE",
    "CO", "PEORIA COUNTY",
    "DU", "DUNLAP",
    "EL", "ELMWOOD",
    "FC", "FULTON COUNTY",
    "GL", "GLASFORD",
    "HC", "HANNA CITY",
    "KC", "KNOX COUNTY",
    "KM", "KINGSTON MINES",
    "MA", "MAPLETON",
    "NO", "NORWOOD",
    "PA", "PEORIA",
    "PH", "PEORIA HEIGHTS",
    "PR", "PRINCEVILLE",
    "WP", "WEST PEORIA"
  });
}
