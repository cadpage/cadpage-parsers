package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA58Parser;

public class CACathedralCityParser extends DispatchA58Parser {

  public CACathedralCityParser() {
    super("PSPD Auto Paging", CITY_CODES, "CATHEDRAL CITY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "hostmaster@cathedralcity.gov,cyrunpaging@gmail.com,no-reply@mark43.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CCPD Alliance Message") || subject.equals("Event Notification")) subject = "PSPD Auto Paging";
    return super.parseMsg(subject, body, data);
  }

  private static final Pattern DIR_OF_PTN = Pattern.compile("[NSEW]O .*");
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (DIR_OF_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }

  private static Properties CITY_CODES = buildCodeTable(new String[] { 
      "CTHDRL CTY",         "Cathedral City", 
      "CATHEDRAL CITY CA",  "Cathedral City"
  });

}
