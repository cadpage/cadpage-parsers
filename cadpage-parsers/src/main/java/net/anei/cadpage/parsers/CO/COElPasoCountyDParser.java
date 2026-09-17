package net.anei.cadpage.parsers.CO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

public class COElPasoCountyDParser extends DispatchA11Parser {

  public COElPasoCountyDParser() {
    super(CITY_CODES, "EL PASO COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "PCSO_Comm@pueblocounty.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // They seem to be dropping the PAGED identifier, so we have to put it back
    if (!body.contains("\nPAGED\n")) {
      int pt =  body.indexOf('\n');
      if (pt < 0) return false;
      pt = body.indexOf('\n', pt+1);
      if (pt < 0) return false;
      body = body.substring(0,pt) + "\nPAGED" + body.substring(pt);
    }
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AVO", "Avondale",
      "BEU", "Beulah",
      "BOO", "Boone",
      "CAN", "Canon City",
      "COL", "Colorado City",
      "COS", "Colo Springs",
      "DEN", "Denver",
      "FCO", "Fort Collins",
      "FLO", "Florence",
      "FOU", "Fountain",
      "FOW", "Fowler",
      "HOM", "Homeless",
      "LAJ", "La Junta",
      "LAM", "Lamar",
      "LAV", "La Veta",
      "MAN", "Manzanola",
      "MID", "Midway",
      "OLN", "Olney Springs",
      "ORD", "Ordway",
      "PB1", "Pueblo",
      "PB2", "Pueblo PO Box",
      "PB3", "Pueblo",
      "PB4", "Pueblo",
      "PB5", "Pueblo",
      "PB6", "Pueblo",
      "PB8", "Pueblo",
      "PEN", "Penrose",
      "PW",  "Pueblo West",
      "RES", "Reservoir",
      "ROC", "Rocky Ford",
      "RYE", "Rye",
      "SAN", "San Isabel",
      "TRI", "Trinidad",
      "WAL", "Walsenburg",
      "WES", "Westcliffe",
      "WET", "Wetmore"
  });
}
