package net.anei.cadpage.parsers.TX;

/*
Acadian Ambulance, TX

*/

public class TXAcadianAmbulanceParser extends TXBurnetCountyParser {

  public TXAcadianAmbulanceParser() {
    super("", "TX");
  }
  
  @Override
  public String getLocName() {
    return "Acadian Ambulance, TX";
  }

  @Override
  public String getFilter() {
    return "bastropactive911@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
