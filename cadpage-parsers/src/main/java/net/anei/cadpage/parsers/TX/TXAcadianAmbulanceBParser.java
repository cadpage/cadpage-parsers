package net.anei.cadpage.parsers.TX;

/*
Acadian Ambulance, TX

*/

public class TXAcadianAmbulanceBParser extends TXBurnetCountyParser {

  public TXAcadianAmbulanceBParser() {
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
