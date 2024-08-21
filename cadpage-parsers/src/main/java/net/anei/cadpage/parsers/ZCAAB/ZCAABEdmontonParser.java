package net.anei.cadpage.parsers.ZCAAB;

/*
Edmonton, AB, Canadaa
Alias for Red Deer County

*/
public class ZCAABEdmontonParser extends ZCAABRedDeerCountyAParser {
  
  public ZCAABEdmontonParser() {
    super("EDMONTON");
  }
  
  public String getFilter() {
    return "FireDispatch@stalbert.ca";
  }
}
