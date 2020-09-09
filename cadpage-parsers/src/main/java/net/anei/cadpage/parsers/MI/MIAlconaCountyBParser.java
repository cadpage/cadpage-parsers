package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MIAlconaCountyBParser extends DispatchA48Parser {

  public MIAlconaCountyBParser() {
    super(CITY_LIST, "ALCONA COUNTY", "MI", FieldType.INFO, A48_NO_CODE);
  }

  @Override
  public String getFilter() {
    return "@alcona-county.net";
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }


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
