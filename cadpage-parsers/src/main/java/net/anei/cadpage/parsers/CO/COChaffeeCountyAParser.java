package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COChaffeeCountyAParser extends DispatchA55Parser {


  public COChaffeeCountyAParser() {
    super("CHAFFEE COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equals("GARFIELD/MONARCH")) city = "SALIDA";
    return city;
  }
}
