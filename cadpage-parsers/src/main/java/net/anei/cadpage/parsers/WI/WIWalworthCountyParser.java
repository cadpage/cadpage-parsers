package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class WIWalworthCountyParser extends DispatchA63Parser {

  public WIWalworthCountyParser() {
    super(CITY_LIST, "WALWORTH COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "lgpdrms@genevaonline.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseMsg(body, data);
  }
  
  private static final String[] CITY_LIST = new String[] {
      
      // Cities[edit]
      "BURLINGTON",
      "DELAVAN",
      "DELAVAN",
      "ELKHORN",
      "LAKE GENEVA",
      "WHITEWATER",
      
      // Villages
      "BLOOMFIELD",
      "DARIEN",
      "EAST TROY",
      "FONTANA-ON-GENEVA LAKE",
      "GENOA CITY",
      "MUKWONAGO",
      "SHARON",
      "WALWORTH",
      "WILLIAMS BAY",
      
      // Towns
      "BLOOMFIELD",
      "DARIEN",
      "DELAVAN",
      "EAST TROY",
      "GENEVA",
      "LAFAYETTE",
      "LA GRANGE",
      "LINN",
      "LYONS",
      "RICHMOND",
      "SHARON",
      "SPRING PRAIRIE",
      "SUGAR CREEK",
      "TROY",
      "WALWORTH",
      "WHITEWATER",

      // Census-designated places
      "ALLENS GROVE",
      "BIG FOOT PRAIRIE",
      "COMO",
      "DELAVAN LAKE",
      "LAKE IVANHOE",
      "LAKE LORRAINE",
      "LAUDERDALE LAKES",
      "LYONS",
      "POTTER LAKE",
      "SPRINGFIELD",
      "TURTLE LAKE FLOATING",
      "TURTLE LAKE",

      // Unincorporated communities
      "ABELLS CORNERS",
      "ADAMS",
      "BARDWELL",
      "BOWERS",
      "EAST DELAVAN",
      "FAIRFIELD",
      "INLET",
      "HEART PRAIRIE",
      "HILBURN",
      "HONEY CREEK",
      "HONEY LAKE",
      "LA GRANGE",
      "LAKE BEULAH",
      "LAKE COMO",
      "LAKE LAWN",
      "LAUDERDALE",
      "LAUDERDALE SHORES",
      "LINTON",
      "LITTLE PRAIRIE",
      "MILLARD",
      "NORTH BLOOMFIELD",
      "PELL LAKE",
      "POWERS LAKE",
      "RICHMOND",
      "SPRING PRAIRIE",
      "TIBBETS",
      "TROY",
      "TROY CENTER",
      "VOREE",
      "ZENDA",
      
      //Ghost towns
      "ARMY LAKE",
      "MAYHEWS"
  };
}
