package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class CTEastLymeParser extends DispatchA32Parser {
  
  public CTEastLymeParser() {
    super(CITY_LIST, "EAST LYME","CT");
  }
  
  @Override
  public String getFilter() {
    return "taylorq83@gmail.com";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "WATERFORD",
    "EAST LYME"
  };
}
