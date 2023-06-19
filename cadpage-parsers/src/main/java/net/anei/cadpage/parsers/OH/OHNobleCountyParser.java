package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHNobleCountyParser extends DispatchA1Parser {

  public OHNobleCountyParser() {
    super(CITY_LIST, "NOBLE COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "@noblecountysheriffsoffice.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) {
      if (body.startsWith("Alert:")) {
        int pt = body.indexOf('\n');
        String tmp = body.substring(pt+1).trim();
        if (tmp.startsWith("ALRM LVL:")) {
          subject = body.substring(0,pt).trim();
          body = tmp;
        }
      }
      else if (body.startsWith("ALRM LVL:")) subject = "Alert:";
    }
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Villages
      "BATESVILLE",
      "BELLE VALLEY",
      "CALDWELL",
      "DEXTER CITY",
      "SARAHSVILLE",
      "SUMMERFIELD",

      // Townships
      "BEAVER",
      "BROOKFIELD",
      "BUFFALO",
      "CENTER",
      "ELK",
      "ENOCH",
      "JACKSON",
      "JEFFERSON",
      "MARION",
      "NOBLE",
      "OLIVE",
      "SENECA",
      "SHARON",
      "STOCK",
      "WAYNE",

      // Unincorporated communities
      "AVA",
      "CARLISLE",
      "CROOKED TREE",
      "DUDLEY",
      "DUNGANNON",
      "EAST UNION",
      "ELK",
      "FULDA",
      "GEM",
      "HARRIETTSVILLE",
      "HIRAMSBURG",
      "HONESTY",
      "HOSKINSVILLE",
      "KEITH",
      "KENNONSBURG",
      "MIDDLEBURG",
      "MOUNDSVILLE",
      "MT EPHRAIM",
      "MT EPHRIAM",
      "OLIVE GREEN",
      "ROCHESTER",
      "SHARON",
      "SOUTH OLIVE",
      "STEAMTOWN",
      "WHIGVILLE",

      // Belmont County

      // Monroe County

      // Morgan County

      // Muskingum County

      // Guernsey County
      "BYESVILLE",
      "CUMBERLAND",
      "LORE CITY",
      "QUAKER CITY",
      "PLEASANT CITY",
      "SENECAVILLE",

      // Washington County
      "LOWER SALEM",
      "MACKSBURG",
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "MT EPHRIAM",     "MT EPHRAIM"
  });
}
