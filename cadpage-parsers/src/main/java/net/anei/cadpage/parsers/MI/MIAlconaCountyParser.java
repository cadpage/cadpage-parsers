package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MIAlconaCountyParser extends DispatchB2Parser {

  public MIAlconaCountyParser() {
    super(CITY_LIST,"ALCONA COUNTY", "MI", B2_OPT_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "ALCONACAD@alcona-county.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "ALCONACAD:");
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean isPageMsg(String body) {
    return body.contains(" Cad:");
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "FIRE ANY",
      "MEDICAL",
      "MISC",
      "PERSONAL INJURY ACCIDENT",
      "POWER LINES",
      "SUICIDE"
  );
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "CEDAR LAKE",
    "POOR FARM"
  };
  
  static final String[] CITY_LIST = new String[]{

      "ALCONA",
      "ALVIN",
      "BACKUS BEACH",
      "BARTON CITY",
      "BLACK RIVER",
      "BRYANT",
      "CHEVIERS",
      "CURRAN",
      "CURTISVILLE",
      "GLENNIE",
      "GREENBUSH",
      "GUSTIN",
      "HARRISVILLE",
      "HUBBARD LAKE",
      "KILLMASTER",
      "KURTZ",
      "LARSON BEACH",
      "LINCOLN",
      "LOST LAKE WOODS",
      "MIKADO",
      "SPRINGPORT",
      "SPRUCE",
      "WALLACE",
     
      //TOWNSHIPS
      
      "ALCONA TOWNSHIP",
      "ALCONA TWP",
      "CALEDONIA TOWNSHIP",
      "CALEDONIA TWP",
      "CURTIS TOWNSHIP",
      "CURTIS TWP",
      "GREENBUSH TOWNSHIP",
      "GREENBUSH TWP",
      "GUSTIN TOWNSHIP",
      "GUSTIN TWP",
      "HARRISVILLE TOWNSHIP",
      "HARRISVILLE TWP",
      "HAWES TOWNSHIP",
      "HAWES TWP",
      "HAYNES TOWNSHIP",
      "HAYNES TWP",
      "MIKADO TOWNSHIP",
      "MIKADO TWP",
      "MILLEN TOWNSHIP",
      "MILLEN TWP",
      "MITCHELL TOWNSHIP",
      "MITCHELL TWP",
      
      // Iosco County
      "OSCODA"
      
  };
}
